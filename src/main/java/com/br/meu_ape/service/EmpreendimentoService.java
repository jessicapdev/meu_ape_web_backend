package com.br.meu_ape.service;

import com.br.meu_ape.dto.*;
import com.br.meu_ape.model.Apartamento;
import com.br.meu_ape.model.Empreendimento;
import com.br.meu_ape.model.ImagemItem;
import com.br.meu_ape.model.Imagens;
import com.br.meu_ape.model.projection.EmpreendimentoEmpreendimentoProjection;
import com.br.meu_ape.model.projection.EmpreendimentoHomeProjection;
import com.br.meu_ape.model.projection.EmpreendimentoImagemProjection;
import com.br.meu_ape.model.projection.EmpreendimentoPerfilProjection;
import com.br.meu_ape.repository.EmpreendimentoRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmpreendimentoService {

    @Autowired
    private EmpreendimentoRepository empreendimentoRepository;
    private final GridFsTemplate gridFsTemplate;
    @Autowired
    private MongoTemplate mongoTemplate;

    public EmpreendimentoService(GridFsTemplate gridFsTemplate) {
        this.gridFsTemplate = gridFsTemplate;
    }

    public Empreendimento criar(EmpreendimentoDTO dto) {
        Empreendimento emp = converterParaEntidade(dto);
        return empreendimentoRepository.save(emp);
    }

    public Page<EmpreendimentoHomeProjection> listarComFiltros(EmpreendimentoFiltroDTO filtro) {
        return empreendimentoRepository.buscarComFiltros(filtro);
    }

    public Page<Empreendimento> listarTodos(Pageable pageable) {
        return empreendimentoRepository.findAll(pageable);
    }

    public Page<EmpreendimentoHomeProjection> listarParaHome(Pageable pageable) {
        return empreendimentoRepository.findHomeBy(pageable);
    }

    public Page<EmpreendimentoPerfilProjection> listarParaPerfil(Pageable pageable) {
        return empreendimentoRepository.findPerfilBy(pageable);
    }

    public EmpreendimentoImagemProjection listarImagemById(String id) {
        return empreendimentoRepository.findProjectedById(id)
                .orElseThrow(() -> new RuntimeException("Empreendimento não encontrado com o ID: " + id));
    }

    public Empreendimento processarAtualizarImagens(String id,
                                                    MultipartFile banner,
                                                    MultipartFile mapa,
                                                    List<MultipartFile> plantas,
                                                    List<MultipartFile> galeria,
                                                    ImagensConfigDTO config) {

        Empreendimento empreendimento = empreendimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empreendimento não encontrado"));

        Imagens imagens = empreendimento.getImagens();
        if (imagens == null) imagens = new Imagens();

        if (!config.manterBanner() && imagens.getBanner() != null) {
            deletarDoGridFS(imagens.getBanner().getFileId());
            imagens.setBanner(null);
        }
        if (banner != null && !banner.isEmpty()) {
            if (imagens.getBanner() != null) deletarDoGridFS(imagens.getBanner().getFileId());
            String fileId = salvarNoGridFS(banner, "banner-" + id);

            String titulo = config.bannerMeta() != null ? config.bannerMeta().titulo() : null;
            String desc = config.bannerMeta() != null ? config.bannerMeta().descricao() : null;
            imagens.setBanner(new ImagemItem(fileId, titulo, desc));

        } else if (config.manterBanner() && imagens.getBanner() != null && config.bannerMeta() != null) {
            // Atualiza os metadados do banner existente
            imagens.getBanner().setTitulo(config.bannerMeta().titulo());
            imagens.getBanner().setDescricao(config.bannerMeta().descricao());
        }

        // --- MAPA ---
        if (!config.manterMap() && imagens.getMap() != null) {
            deletarDoGridFS(imagens.getMap().getFileId());
            imagens.setMap(null);
        }
        if (mapa != null && !mapa.isEmpty()) {
            if (imagens.getMap() != null) deletarDoGridFS(imagens.getMap().getFileId());
            String fileId = salvarNoGridFS(mapa, "mapa-" + id);

            String titulo = config.mapMeta() != null ? config.mapMeta().titulo() : null;
            String desc = config.mapMeta() != null ? config.mapMeta().descricao() : null;
            imagens.setMap(new ImagemItem(fileId, titulo, desc));

        } else if (config.manterMap() && imagens.getMap() != null && config.mapMeta() != null) {
            // Atualiza os metadados do mapa existente
            imagens.getMap().setTitulo(config.mapMeta().titulo());
            imagens.getMap().setDescricao(config.mapMeta().descricao());
        }

        // --- PLANTAS ---
        List<ImagemItem> plantasFinais = new ArrayList<>();
        List<ImagemItem> plantasAtuais = imagens.getPlantas() != null ? imagens.getPlantas() : new ArrayList<>();
        List<String> plantasParaManter = config.plantasMantidas() != null ? config.plantasMantidas() : new ArrayList<>();

        // Processa as plantas antigas (mantém e atualiza meta, ou deleta)
        plantasAtuais.forEach(plantaAntiga -> {
            if (plantasParaManter.contains(plantaAntiga.getFileId())) {
                ImagemItem atualizada = atualizarMetadados(plantaAntiga, config.plantasMeta());
                plantasFinais.add(atualizada);
            } else {
                deletarDoGridFS(plantaAntiga.getFileId());
            }
        });

        // Processa as plantas novas
        if (plantas != null && !plantas.isEmpty()) {
            plantas.stream().filter(f -> !f.isEmpty()).forEach(f -> {
                String fileId = salvarNoGridFS(f, "planta-" + id);
                ImagemItem novoItem = criarNovoItemComMetadados(fileId, f.getOriginalFilename(), config.plantasMeta());
                plantasFinais.add(novoItem);
            });
        }
        imagens.setPlantas(plantasFinais);

        // --- GALERIA ---
        List<ImagemItem> galeriaFinal = new ArrayList<>();
        List<ImagemItem> galeriaAtual = imagens.getGaleria() != null ? imagens.getGaleria() : new ArrayList<>();
        List<String> galeriaParaManter = config.galeriaMantida() != null ? config.galeriaMantida() : new ArrayList<>();

        // Processa a galeria antiga
        galeriaAtual.forEach(imgAntiga -> {
            if (galeriaParaManter.contains(imgAntiga.getFileId())) {
                ImagemItem atualizada = atualizarMetadados(imgAntiga, config.galeriaMeta());
                galeriaFinal.add(atualizada);
            } else {
                deletarDoGridFS(imgAntiga.getFileId());
            }
        });

        // Processa a galeria nova
        if (galeria != null && !galeria.isEmpty()) {
            galeria.stream().filter(f -> !f.isEmpty()).forEach(f -> {
                String fileId = salvarNoGridFS(f, "galeria-" + id);
                ImagemItem novoItem = criarNovoItemComMetadados(fileId, f.getOriginalFilename(), config.galeriaMeta());
                galeriaFinal.add(novoItem);
            });
        }
        imagens.setGaleria(galeriaFinal);

        empreendimento.setImagens(imagens);
        return empreendimentoRepository.save(empreendimento);
    }

    private ImagemItem atualizarMetadados(ImagemItem itemAntigo, List<ImagemMetadadosDTO> metaList) {
        if (metaList != null) {
            for (ImagemMetadadosDTO meta : metaList) {
                if (meta.reference() != null && meta.reference().equals(itemAntigo.getFileId())) {
                    return new ImagemItem(itemAntigo.getFileId(), meta.titulo(), meta.descricao());
                }
            }
        }
        return itemAntigo; // Se não enviou alteração, mantém como estava
    }


    private ImagemItem criarNovoItemComMetadados(String fileId, String fileName, List<ImagemMetadadosDTO> metaList) {
        if (metaList != null) {
            for (ImagemMetadadosDTO meta : metaList) {
                if (meta.reference() != null && meta.reference().equals(fileName)) {
                    return new ImagemItem(fileId, meta.titulo(), meta.descricao());
                }
            }
        }
        return new ImagemItem(fileId, null, null); // Salva sem metadados se não achar
    }

    private String salvarNoGridFS(MultipartFile file, String baseName) {
        try {
            String fileName = baseName + "-" + System.currentTimeMillis() + ".webp";

            DBObject metaData = new BasicDBObject();
            metaData.put("type", "image");
            metaData.put("contentType", "image/webp");
            metaData.put("originalName", file.getOriginalFilename());

            ObjectId fileId = gridFsTemplate.store(
                    file.getInputStream(),
                    fileName,
                    "image/webp",
                    metaData
            );

            return fileId.toString();
        } catch (IOException e) {
            throw new RuntimeException("Falha ao salvar imagem no GridFS: " + file.getOriginalFilename(), e);
        }
    }

    private void deletarDoGridFS(String fileId) {
        if (fileId != null && !fileId.trim().isEmpty()) {
            try {
                gridFsTemplate.delete(new Query(Criteria.where("_id").is(new ObjectId(fileId))));
            } catch (IllegalArgumentException e) {
                System.err.println("ID de arquivo inválido para exclusão: " + fileId);
            }
        }
    }

    public GridFSFile buscarArquivoNoGridFS(String fileId) {
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileId)));

        if (file == null) {
            throw new RuntimeException("Arquivo não encontrado no GridFS: " + fileId);
        }
        return file;
    }

    public EmpreendimentoEmpreendimentoProjection listarEmpreendimentoById(String id) {
        return empreendimentoRepository.findEmpreendimentoProjectedById(id)
                .orElseThrow(() -> new RuntimeException("Empreendimento não encontrado com o ID: " + id));
    }

    public Optional<Empreendimento> buscarPorId(String id) {
        return empreendimentoRepository.findById(id);
    }

    public Empreendimento atualizarDados(String id, EmpreendimentoUpdateDTO dto) {
        Empreendimento empreendimento = empreendimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empreendimento não encontrado"));

        empreendimento.setTitulo(dto.titulo());
        empreendimento.setDescricao(dto.descricao());
        empreendimento.setTiposImoveis(dto.tiposImoveis());
        empreendimento.setEndereco(dto.endereco());
        empreendimento.setConstrutora(dto.construtora());
        empreendimento.setCidade(dto.cidade());
        empreendimento.setBairro(dto.bairro());
        empreendimento.setStatus(dto.status());
        empreendimento.setAreaMin(dto.areaMin());
        empreendimento.setAreaMax(dto.areaMax());
        empreendimento.setBanheiros(dto.banheiros());
        empreendimento.setQuartos(dto.quartos());
        empreendimento.setVagas(dto.vagas());
        empreendimento.setTimeline(dto.timeline());
        empreendimento.setPrecoMin(dto.precoMin());
        empreendimento.setPrecoMax(dto.precoMax());
        empreendimento.setDiferenciais(dto.diferenciais());

        return empreendimentoRepository.save(empreendimento);
    }

    public Empreendimento buscarDetalhe(String id) {
        Optional<Empreendimento> optional = empreendimentoRepository.findById(id);
        return optional.orElse(null);
    }

    public Empreendimento atualizar(String id, EmpreendimentoDTO dto) {
        Optional<Empreendimento> optional = empreendimentoRepository.findById(id);

        if (optional.isPresent()) {
            Empreendimento emp = optional.get();
            atualizarEntidade(emp, dto);
            return empreendimentoRepository.save(emp);
        }
        return null;
    }

    public void deletar(String id) {
        empreendimentoRepository.deleteById(id);
    }

    public List<Empreendimento> buscarPorCidade(String cidade) {
        return empreendimentoRepository.findByCidadeIgnoreCase(cidade);
    }

    public List<Empreendimento> buscarPorStatus(String status) {
        return empreendimentoRepository.findByStatusIgnoreCase(status);
    }

    public void incrementarViews(String id) {
        Optional<Empreendimento> optional = empreendimentoRepository.findById(id);
        if (optional.isPresent()) {
            Empreendimento emp = optional.get();
            emp.setViews(emp.getViews() + 1);
            empreendimentoRepository.save(emp);
        }
    }

    public List<Apartamento> listarApartamentos(String id) {
        return empreendimentoRepository.findApartamentosByEmpreendimentoId(id)
                .map(Empreendimento::getApartamentos)
                .orElseThrow(() -> new RuntimeException("Empreendimento não encontrado"));
    }

    public void adicionarApartamento(String id, Apartamento novoApartamento) {
        if (novoApartamento.getId() == null || novoApartamento.getId().isEmpty()) {
            novoApartamento.setId(java.util.UUID.randomUUID().toString());
        }

        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().push("apartamentos", novoApartamento);

        mongoTemplate.updateFirst(query, update, Empreendimento.class);
    }

    public void deletarApartamento(String empreendimentoId, String apartamentoId) {
        Query query = new Query(Criteria.where("_id").is(empreendimentoId));
        Update update = new Update().pull("apartamentos", Query.query(Criteria.where("id").is(apartamentoId)));

        mongoTemplate.updateFirst(query, update, Empreendimento.class);
    }

    private Empreendimento converterParaEntidade(EmpreendimentoDTO dto) {
        Empreendimento emp = new Empreendimento();
        atualizarEntidade(emp, dto);
        return emp;
    }

    private void atualizarEntidade(Empreendimento emp, EmpreendimentoDTO dto) {
        emp.setTitulo(dto.getTitulo());
        emp.setStatus(dto.getStatus());
        emp.setEndereco(dto.getEndereco());
        emp.setCidade(dto.getCidade());
        emp.setBairro(dto.getBairro());
        emp.setConstrutora(dto.getConstrutora());
        emp.setAreaMin(dto.getAreaMin());
        emp.setAreaMax(dto.getAreaMax());
        emp.setBanheiros(dto.getBanheiros());
        emp.setQuartos(dto.getQuartos());
        emp.setVagas(dto.getVagas());
        emp.setPrecoMin(dto.getPrecoMin());
        emp.setPrecoMax(dto.getPrecoMax());
        emp.setImagens(dto.getImagens());
        emp.setApartamentos(dto.getApartamentos());
        emp.setDiferenciais(dto.getDiferenciais());
        emp.setTiposImoveis(dto.getTiposImoveis());
        emp.setDescricao(dto.getDescricao());
        emp.setTimeline(dto.getTimeline());
        emp.setViews(dto.getViews());
        emp.setDias(dto.getDias());
    }
}
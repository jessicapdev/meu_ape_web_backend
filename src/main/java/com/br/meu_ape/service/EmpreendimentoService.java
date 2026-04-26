package com.br.meu_ape.service;

import com.br.meu_ape.dto.EmpreendimentoDTO;
import com.br.meu_ape.dto.EmpreendimentoFiltroDTO;
import com.br.meu_ape.dto.EmpreendimentoUpdateDTO;
import com.br.meu_ape.model.Apartamento;
import com.br.meu_ape.model.Empreendimento;
import com.br.meu_ape.model.Imagens;
import com.br.meu_ape.model.projection.EmpreendimentoEmpreendimentoProjection;
import com.br.meu_ape.model.projection.EmpreendimentoHomeProjection;
import com.br.meu_ape.model.projection.EmpreendimentoImagemProjection;
import com.br.meu_ape.model.projection.EmpreendimentoPerfilProjection;
import com.br.meu_ape.repository.EmpreendimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpreendimentoService {

    @Autowired
    private EmpreendimentoRepository empreendimentoRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

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

    public Empreendimento atualizarImagens(String id, Imagens novasImagens) {
        Empreendimento empreendimento = empreendimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empreendimento não encontrado com o ID: " + id));

        empreendimento.setImagens(novasImagens);

        return empreendimentoRepository.save(empreendimento);
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
        emp.setViews(dto.getViews());
        emp.setDias(dto.getDias());
    }
}
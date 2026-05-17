package com.br.meu_ape.controller;

import com.br.meu_ape.dto.EmpreendimentoDTO;
import com.br.meu_ape.dto.EmpreendimentoFiltroDTO;
import com.br.meu_ape.dto.EmpreendimentoUpdateDTO;
import com.br.meu_ape.model.Apartamento;
import com.br.meu_ape.model.Empreendimento;
import com.br.meu_ape.model.projection.EmpreendimentoEmpreendimentoProjection;
import com.br.meu_ape.model.projection.EmpreendimentoHomeProjection;
import com.br.meu_ape.model.projection.EmpreendimentoPerfilProjection;
import com.br.meu_ape.service.EmpreendimentoService;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/empreendimentos")
@CrossOrigin(origins = "*")
public class EmpreendimentoController {

    public static final MediaType IMAGE_WEBP = MediaType.valueOf("image/webp");
    private final GridFsTemplate gridFsTemplate;

    @Autowired
    private EmpreendimentoService empreendimentoService;

    public EmpreendimentoController(GridFsTemplate gridFsTemplate) {
        this.gridFsTemplate = gridFsTemplate;
    }

    @PostMapping
    public ResponseEntity<Empreendimento> criar(@RequestBody EmpreendimentoDTO dto) {
        Empreendimento novo = empreendimentoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    @PostMapping("/buscar")
    public ResponseEntity<Page<EmpreendimentoHomeProjection>> buscarComFiltros(@RequestBody EmpreendimentoFiltroDTO filtro) {
        Page<EmpreendimentoHomeProjection> resultado = empreendimentoService.listarComFiltros(filtro);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/home")
    public ResponseEntity<Page<EmpreendimentoHomeProjection>> listarHome(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "titulo") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Sort sort = "desc".equalsIgnoreCase(sortDirection)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(empreendimentoService.listarParaHome(pageable));
    }

    @GetMapping("/perfil")
    public ResponseEntity<Page<EmpreendimentoPerfilProjection>> listarPerfil(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "titulo") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Sort sort = "desc".equalsIgnoreCase(sortDirection)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(empreendimentoService.listarParaPerfil(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empreendimento> buscarPorId(@PathVariable String id) {
        Optional<Empreendimento> emp = empreendimentoService.buscarPorId(id);
        return emp.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empreendimento> atualizarDadosCadastrais(
            @PathVariable String id,
            @RequestBody EmpreendimentoUpdateDTO dto) {

        return ResponseEntity.ok(empreendimentoService.atualizarDados(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        empreendimentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/imagens")
    public ResponseEntity<Resource> buscarImagemPorId(@PathVariable String id) {
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
        if (file == null) return ResponseEntity.notFound().build();

        GridFsResource resource = gridFsTemplate.getResource(file);

        return ResponseEntity.ok()
                .contentType(IMAGE_WEBP)
                .header(HttpHeaders.CACHE_CONTROL, "public, max-age=31536000")
                .body(resource);
    }

    @PutMapping(value = "/{id}/imagens", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Empreendimento> atualizarImagens(
            @PathVariable String id,
            @RequestParam(value = "banner", required = false) MultipartFile banner,
            @RequestParam(value = "mapa", required = false) MultipartFile mapa,
            @RequestParam(value = "plantas", required = false) List<MultipartFile> plantas,
            @RequestParam(value = "galeria", required = false) List<MultipartFile> galeria) {

        empreendimentoService.processarAtualizarImagens(id, banner, mapa, plantas, galeria);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/dados")
    public ResponseEntity<EmpreendimentoEmpreendimentoProjection> buscarEmpreendimentoById(
            @PathVariable String id) {
        return ResponseEntity.ok(empreendimentoService.listarEmpreendimentoById(id));
    }

    @GetMapping("/{id}/detalhe")
    public ResponseEntity<Empreendimento> buscarDetalhe(@PathVariable String id) {
        Empreendimento emp = empreendimentoService.buscarDetalhe(id);
        if (emp != null) {
            empreendimentoService.incrementarViews(id);
            return ResponseEntity.ok(emp);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/apartamento")
    public ResponseEntity<List<Apartamento>> listarApartamentos(@PathVariable String id) {
        return ResponseEntity.ok(empreendimentoService.listarApartamentos(id));
    }

    @PutMapping("/{id}/apartamento")
    public ResponseEntity<Void> adicionarApartamento(
            @PathVariable String id,
            @RequestBody Apartamento apartamento) {

        empreendimentoService.adicionarApartamento(id, apartamento);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/apartamento/{idApartamento}")
    public ResponseEntity<Void> adicionarApartamento(
            @PathVariable String id,
            @PathVariable String idApartamento) {

        empreendimentoService.deletarApartamento(id, idApartamento);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/opcoes/tipos-imoveis")
    public ResponseEntity<List<String>> getTiposImoveis() {
        List<String> tipos = Arrays.asList(
                "Apartamento",
                "Casa",
                "Cobertura/Rooftop",
                "Duplex",
                "Garden",
                "Loteamento",
                "Penthouse",
                "Studio",
                "Townhouse",
                "Triplex",
                "Up House"
        );
        return ResponseEntity.ok(tipos);
    }

    @GetMapping("/opcoes/tipos-status")
    public ResponseEntity<List<String>> getTiposStatus() {
        List<String> tipos = Arrays.asList(
                "Em construção",
                "Pronto",
                "Lançamento",
                "Pre lançamento"
        );
        return ResponseEntity.ok(tipos);
    }

    @GetMapping("/opcoes/diferenciais")
    public ResponseEntity<List<String>> getDiferenciais() {
        List<String> diferenciais = Arrays.asList(
                "Área de lazer",
                "Área verde privativa",
                "Bicicletário",
                "Lojas no térreo",
                "Complexo multiuso",
                "Coworking",
                "Elevadores com biometria",
                "Espaço delivery",
                "Lazer elevado acima do térreo",
                "Lazer na cobertura",
                "Pet place",
                "Piscina climatizada",
                "Piscina",
                "Ponto para carro elétrico",
                "Portaria virtual",
                "Quadra de tênis",
                "Torre única",
                "Um por andar",
                "Vaga box",
                "Vaga coberta",
                "Vaga determinadas",
                "Vista para o mar",
                "Wi-fi nas áreas comuns"
        );
        return ResponseEntity.ok(diferenciais);
    }

    @GetMapping("/opcoes/construtoras")
    public ResponseEntity<List<String>> getConstrutoras() {
        List<String> diferenciais = Arrays.asList(
            "Gafisa",
            "Cyrela",
            "MRV",
            "Even",
            "Eztec",
            "Trisul",
            "Mitre",
            "Direcional",
            "Cury",
            "Tenda",
            "Moura Dubeux",
            "Tecnisa",
            "Helbor",
            "Yuni",
            "Setin",
            "Lopes",
            "Vitacon",
            "You,inc",
            "Kallas",
            "Plano&Plano",
            "Lavvi",
            "Benx",
            "Diálogo",
            "Tarjab",
            "SKR",
            "AAM"
        );
        return ResponseEntity.ok(diferenciais);
    }

    @PostMapping("/{id}/view")
    public ResponseEntity<Void> incrementarView(@PathVariable String id) {
        empreendimentoService.incrementarViews(id);
        return ResponseEntity.ok().build();
    }
}
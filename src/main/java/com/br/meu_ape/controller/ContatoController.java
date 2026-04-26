package com.br.meu_ape.controller;

import com.br.meu_ape.dto.ContatoDTO;
import com.br.meu_ape.model.Contato;
import com.br.meu_ape.service.ContatoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/contatos")
public class ContatoController {

    private final ContatoService contatoService;

    public ContatoController(ContatoService contatoService) {
        this.contatoService = contatoService;
    }

    @PostMapping
    public ResponseEntity<Contato> receberContato(@RequestBody ContatoDTO contatoDTO) {
        contatoService.salvarContato(contatoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<Void> atualizarStatusContato(
            @PathVariable String id,
            @Valid @RequestBody Boolean lido) {

        contatoService.atualizarContato(id, lido);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Contato>> listarContato() {
        return ResponseEntity.ok(contatoService.listarContatos());
    }
}
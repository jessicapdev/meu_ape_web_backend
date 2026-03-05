package com.br.meu_ape.controller;

import com.br.meu_ape.dto.ContatoDTO;
import com.br.meu_ape.model.Contato;
import com.br.meu_ape.service.ContatoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
}
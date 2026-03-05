package com.br.meu_ape.service;

import com.br.meu_ape.dto.ContatoDTO;
import com.br.meu_ape.model.Contato;
import com.br.meu_ape.repository.ContatoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ContatoService {

    private final ContatoRepository contatoRepository;

    public ContatoService(ContatoRepository contatoRepository) {
        this.contatoRepository = contatoRepository;
    }

    public Contato salvarContato(ContatoDTO dto) {
        Contato contato = new Contato();
        contato.setNome(dto.nome());
        contato.setEmail(dto.email());
        contato.setTelefone(dto.telefone());
        contato.setAssunto(dto.assunto());
        contato.setMensagem(dto.mensagem());
        contato.setDataRecebimento(LocalDateTime.now());
        contato.setLido(false);

        return contatoRepository.save(contato);
    }
}

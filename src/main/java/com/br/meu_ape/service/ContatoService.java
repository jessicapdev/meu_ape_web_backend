package com.br.meu_ape.service;

import com.br.meu_ape.dto.ContatoDTO;
import com.br.meu_ape.model.Contato;
import com.br.meu_ape.repository.ContatoRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContatoService {

    private final ContatoRepository contatoRepository;

    public ContatoService(ContatoRepository contatoRepository) {
        this.contatoRepository = contatoRepository;
    }

    public List<Contato> listarContatosLidos(Boolean lido) {
        return contatoRepository.findAllByLido(lido);
    }

    public List<Contato> listarContatos() {
        return contatoRepository.findAll();
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

    public void atualizarContato(String id, @Valid Boolean lido) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID do contato é obrigatório");
        }
        if (lido == null) {
            throw new IllegalArgumentException("Campo 'lido' não pode ser nulo");
        }

        var contato = contatoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado: " + id));

        contato.setLido(lido);
        contatoRepository.save(contato);
    }
}

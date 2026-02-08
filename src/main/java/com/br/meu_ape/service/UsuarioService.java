package com.br.meu_ape.service;

import com.br.meu_ape.config.JwtTokenProvider;
import com.br.meu_ape.exception.GlobalExceptionHandler;
import com.br.meu_ape.model.Usuario;
import com.br.meu_ape.model.UsuarioRole;
import com.br.meu_ape.model.response.UsuarioResponse;
import com.br.meu_ape.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    public Usuario find(String id) {
        Optional<Usuario> obj = repo.findById(id);
        return obj.orElseThrow(() -> new GlobalExceptionHandler.ObjectNotFoundException(id,
                "Objeto não encontrado! Id: " + id + ", Tipo: " + Usuario.class.getName()));
    }

    public Usuario insert (Usuario obj){
        obj.setId(null);
        obj.setUsuarioRoles(List.of(UsuarioRole.ROLE_CLIENT));
        var senha = obj.getSenha();
        obj.setSenha(passwordEncoder.encode(senha));
        if(repo.existsByEmail(obj.getEmail())){
            throw new GlobalExceptionHandler.CustomException("O usuário já existe", HttpStatus.CONFLICT);
        } else {
            obj = repo.save(obj);
        }
        return obj;
    }

    public Usuario update (Usuario obj){
        Usuario newObj = find(obj.getId());
        updateData(newObj, obj);
        return repo.save(newObj);
    }

    public void delete (String id){
        find(id);
        try{
            repo.delete(find(id));
        }
        catch (DataIntegrityViolationException e){
            throw new DataIntegrityViolationException("Não é possivel excluir porque há contas associados");
        }
    }

    public List<Usuario> findAll(){
        return repo.findAll();
    }

    public Page<Usuario> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        return repo.findAll(pageRequest);
    }

    private void updateData(Usuario newObj, Usuario obj){
        if(obj.getNome() != null){
            newObj.setNome(obj.getNome());
        }if(obj.getEmail() != null){
            newObj.setEmail(obj.getEmail());
        }if(obj.getFoto() != null){
            newObj.setFoto(obj.getFoto());
        }
    }

    public UsuarioResponse findUser(String email) {
        Optional<Usuario> usuario = repo.findByEmail(email);
        UsuarioResponse usuarioResponse = new UsuarioResponse();

        if (usuario.isEmpty()) {
            throw new GlobalExceptionHandler.CustomException("O usuário não existe", HttpStatus.NOT_FOUND);
        }
        usuarioResponse.parseUsuario(usuario.get());
        return usuarioResponse;

    }

    public Usuario whoami(HttpServletRequest req) {
        return repo.findByNome(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
    }

}

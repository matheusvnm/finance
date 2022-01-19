package com.finance.controllers;


import com.finance.config.security.JwtService;
import com.finance.domain.Usuario;
import com.finance.dto.UsuarioDto;
import com.finance.exception.UserAlreadyExistsException;
import com.finance.form.UsuarioForm;
import com.finance.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/registrar")
public class RegistroController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private JwtService jwtService;

    @Transactional
    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody @Valid UsuarioForm usuarioForm,
                                       UriComponentsBuilder uriComponentsBuilder) {
        Usuario usuario = usuarioForm.converter(jwtService);
        if (!usuarioRepository.existsByEmail(usuario.getEmail())) {
            usuarioRepository.save(usuario);
            URI uri = uriComponentsBuilder.path("/registrar/{id}")
                    .buildAndExpand(usuario.getId())
                    .toUri();
            return ResponseEntity.created(uri)
                    .body(new UsuarioDto(usuario));
        } else {
            return ResponseEntity.badRequest().body("O usuário com este e-mail já existe");
        }
    }
}

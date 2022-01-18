package com.finance.controllers;


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

import javax.validation.Valid;

@RestController
@RequestMapping("/registrar")
public class RegistroController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody @Valid UsuarioForm usuarioForm) throws UserAlreadyExistsException {
        Usuario usuario = usuarioForm.converter();
        if (usuarioRepository.existsByEmail(usuario.getEmail()))
            throw new UserAlreadyExistsException("O usuário com este e-mail já existe");
        usuarioRepository.save(usuario);
        return ResponseEntity.ok(new UsuarioDto(usuario));
    }
}

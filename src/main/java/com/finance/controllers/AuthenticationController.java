package com.finance.controllers;

import com.finance.config.security.JwtService;
import com.finance.dto.TokenDto;
import com.finance.form.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Profile(value = {"prod", "test"})
public class AuthenticationController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtService jwtService;

    @PostMapping
    public ResponseEntity<?> authenticate(@RequestBody @Valid LoginForm loginForm) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = loginForm.converter();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    usernamePasswordAuthenticationToken);
            String token = jwtService.gerarToken(authentication);
            return ResponseEntity.ok(new TokenDto(token, "Bearer"));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest()
                    .body("Usuário com esse e-mail e senha não encontrado!");
        }

    }
}

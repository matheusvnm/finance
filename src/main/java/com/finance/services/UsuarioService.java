package com.finance.services;

import com.finance.config.security.JwtService;
import com.finance.domain.Receita;
import com.finance.domain.Usuario;
import com.finance.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtService jwtService;

    public Usuario recuperarUsuario(HttpServletRequest request) {
        String token = jwtService.getToken(request);
        Long idUsuario = jwtService.getIdUsuario(token);
        return usuarioRepository.getById(idUsuario);
    }

    public URI saveAndBuildUri(Usuario usuario, UriComponentsBuilder uriComponentsBuilder, String urlPath) {
        usuarioRepository.save(usuario);
        return uriComponentsBuilder.path(urlPath)
                .buildAndExpand(usuario.getId())
                .toUri();
    }
}

package com.finance.config.security;

import com.finance.domain.Usuario;
import com.finance.exception.UserNotFoundException;
import com.finance.repository.UsuarioRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;


    public AuthenticationFilter(JwtService jwtService, UsuarioRepository usuarioRepository) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = jwtService.getToken(request);
        Boolean isValido = jwtService.isTokenValido(token);
        if (isValido) {
            try {
                autenticarUsuario(token);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        filterChain.doFilter(request, response);
    }

    private void autenticarUsuario(String token) throws UserNotFoundException {
        Long idUsuario = jwtService.getIdUsuario(token);
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(idUsuario);
        if (usuarioOptional.isEmpty()) {
            throw new UserNotFoundException("Usuario não encontrado");
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(usuarioOptional.get(), null,
                        usuarioOptional.get()
                                .getAuthorities());
        SecurityContextHolder.getContext()
                .setAuthentication(usernamePasswordAuthenticationToken);
    }
}

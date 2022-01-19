package com.finance.controllers;

import com.finance.config.security.JwtService;
import com.finance.domain.Receita;
import com.finance.domain.Usuario;
import com.finance.dto.ReceitaDto;
import com.finance.form.ReceitaForm;
import com.finance.repository.ReceitaRepository;
import com.finance.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

@RestController
@RequestMapping("/receitas")
public class ReceitaController {

    @Autowired
    private ReceitaRepository receitaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private JwtService jwtService;

    @Transactional
    @PostMapping
    public ResponseEntity<?> criarReceita(@RequestBody @Valid ReceitaForm receitaForm,
                                          UriComponentsBuilder uriComponentsBuilder,
                                          HttpServletRequest request) {
        Usuario usuario = recuperarUsuario(request);
        Receita receita = receitaForm.converter(usuario);
        if (descricaoDataReceitaValida(receita)) {
            receitaRepository.save(receita);
            URI uri = uriComponentsBuilder.path("/receita/{id}")
                    .buildAndExpand(receita.getId())
                    .toUri();
            return ResponseEntity.created(uri)
                    .body(new ReceitaDto(receita));
        }
        return ResponseEntity.badRequest()
                .body("Já existe uma receita com está descrição no mês de " + receita.getData()
                        .getMonth().getDisplayName(TextStyle.FULL, new Locale("pt", "BR")));
    }

    private Usuario recuperarUsuario(HttpServletRequest request) {
        String token = jwtService.getToken(request);
        Long idUsuario = jwtService.getIdUsuario(token);
        return usuarioRepository.getById(idUsuario);
    }

    private boolean descricaoDataReceitaValida(Receita receita) {
        LocalDate startDate = receita.getDataInicialDoMes();
        LocalDate endDate = receita.getDataFinalDoMes();
        return !receitaRepository.existsReceitaByDataBetweenAndDescricaoEquals(startDate, endDate,
                receita.getDescricao());
    }
}

package com.finance.controllers;

import com.finance.domain.Receita;
import com.finance.domain.Usuario;
import com.finance.dto.ReceitaDto;
import com.finance.form.ReceitaForm;
import com.finance.services.ReceitaService;
import com.finance.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/receitas")
public class ReceitaController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ReceitaService receitaService;

    @GetMapping
    public Page<Receita> listarTodasReceitas(
            @PageableDefault(sort = "descricao", direction = Sort.Direction.ASC) Pageable pageble,
            HttpServletRequest request, @RequestParam(required = false) String descricao) {
        Long usuarioId = usuarioService.recuperarUsuarioId(request);
        if (descricao == null)
            return receitaService.buscarTodasReceitasDoUsuario(usuarioId, pageble);
        return receitaService.buscarTodasReceitasDoUsuarioComDescricao(usuarioId, pageble, descricao);
    }

    @GetMapping("/{ano}/{mes}")
    public Page<Receita> listarTodasDespesasPorMesAno(
            @PageableDefault(sort = "descricao", direction = Sort.Direction.ASC) Pageable pageble,
            HttpServletRequest request, @PathVariable Integer ano, @PathVariable Integer mes) {
        Long usuarioId = usuarioService.recuperarUsuarioId(request);
        return receitaService.buscarTodasReceitasDoUsuarioPorData(usuarioId, pageble, mes, ano);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalharReceita(@PathVariable Long id, HttpServletRequest request) {
        Usuario usuario = usuarioService.recuperarUsuario(request);
        Optional<Receita> receitaOptional = receitaService.buscarUmaReceitaDoUsuario(usuario, id);
        return receitaOptional.isPresent() ? ResponseEntity.ok(
                receitaOptional.get()) : ResponseEntity.notFound()
                .build();
    }

    @Transactional
    @PostMapping
    public ResponseEntity<?> criarReceita(@RequestBody @Valid ReceitaForm receitaForm,
                                          UriComponentsBuilder uriComponentsBuilder,
                                          HttpServletRequest request) {
        Usuario usuario = usuarioService.recuperarUsuario(request);
        Receita receita = receitaForm.converter(usuario);
        if (receitaService.isDescricaoAndDataValida(receita)) {
            URI uri = receitaService.saveAndBuildUri(receita, uriComponentsBuilder,
                    "/receitas/{id}");
            return ResponseEntity.created(uri)
                    .body(new ReceitaDto(receita));
        }

        return ResponseEntity.badRequest()
                .body("Já existe uma receita com está descrição no mês de " + receita.getData()
                        .getMonth()
                        .getDisplayName(TextStyle.FULL, new Locale("pt", "BR")));
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizaReceita(@PathVariable Long id,
                                             @RequestBody @Valid ReceitaForm receitaForm,
                                             HttpServletRequest request) {
        Usuario usuario = usuarioService.recuperarUsuario(request);
        Optional<Receita> receitaOptional = receitaService.buscarUmaReceitaDoUsuario(usuario, id);
        return receitaOptional.isPresent() ? ResponseEntity.ok(
                receitaForm.atualizarReceita(receitaOptional.get())) : ResponseEntity.notFound()
                .build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteReceita(@PathVariable Long id) {
        return receitaService.deleteReceita(id);
    }
}

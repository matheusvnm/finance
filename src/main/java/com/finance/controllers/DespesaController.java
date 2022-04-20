package com.finance.controllers;

import com.finance.domain.Despesa;
import com.finance.domain.Receita;
import com.finance.domain.Usuario;
import com.finance.dto.DespesaDto;
import com.finance.dto.ReceitaDto;
import com.finance.form.DespesaForm;
import com.finance.services.DespesaService;
import com.finance.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/despesas")
public class DespesaController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DespesaService despesaService;


    @GetMapping
    public Page<Despesa> listarTodasDespesas(
            @PageableDefault(sort = "descricao", direction = Sort.Direction.ASC) Pageable pageble,
            HttpServletRequest request, @RequestParam(required = false) String descricao) {
        Usuario usuario = usuarioService.recuperarUsuario(request);
        if (descricao == null)
            return despesaService.buscarTodasDespesasDoUsuario(usuario, pageble);

        return despesaService.buscarTodasDespesasDoUsuarioComDescricao(usuario, pageble,
                descricao);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalharDespesa(@PathVariable Long id, HttpServletRequest request) {
        Usuario usuario = usuarioService.recuperarUsuario(request);
        Optional<Despesa> despesaOptional = despesaService.buscarDespesasDoUsuario(usuario, id);
        return despesaOptional.isPresent() ? ResponseEntity.ok(
                despesaOptional.get()) : ResponseEntity.notFound()
                .build();
    }


    @Transactional
    @PostMapping
    public ResponseEntity<?> criarDespesa(@RequestBody @Valid DespesaForm despesaForm,
                                          UriComponentsBuilder uriComponentsBuilder,
                                          HttpServletRequest request) {
        Usuario usuario = usuarioService.recuperarUsuario(request);
        Despesa despesa = despesaForm.converter(usuario);
        if (despesaService.isDescricaoAndDataValida(despesa)) {
            URI uri = despesaService.saveAndBuildUri(despesa, uriComponentsBuilder,
                    "/despesas/{id}");
            return ResponseEntity.created(uri)
                    .body(new DespesaDto(despesa));
        }

        return ResponseEntity.badRequest()
                .body("Já existe uma despesa com está descrição no mês de " + despesa.getData()
                        .getMonth()
                        .getDisplayName(TextStyle.FULL, new Locale("pt", "BR")));

    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizaDespesa(@PathVariable Long id,
                                             @RequestBody @Valid DespesaForm despesaForm,
                                             HttpServletRequest request) {
        Usuario usuario = usuarioService.recuperarUsuario(request);
        Despesa despesa = despesaForm.converter(usuario);
        if (despesaService.isDescricaoAndDataValida(despesa)) {
            Optional<Despesa> despesasDoUsuarioOptional = despesaService.buscarDespesasDoUsuario(
                    usuario,
                    id);

            if (despesasDoUsuarioOptional.isPresent()) {
                Despesa despesaEditada = despesaForm.atualizarDespesa(
                        despesasDoUsuarioOptional.get());
                return ResponseEntity.ok(new DespesaDto(despesaEditada));
            } else
                return ResponseEntity.notFound()
                        .build();
        }
        return ResponseEntity.badRequest()
                .body("Já existe uma despesa com está descrição no mês de " + despesa.getData()
                        .getMonth()
                        .getDisplayName(TextStyle.FULL, new Locale("pt", "BR")));

    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteReceita(@PathVariable Long id) {
        return despesaService.deleteDespesa(id);
    }
}
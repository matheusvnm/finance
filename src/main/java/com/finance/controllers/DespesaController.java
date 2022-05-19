package com.finance.controllers;

import com.finance.domain.Despesa;
import com.finance.domain.Usuario;
import com.finance.dto.DespesaDto;
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
        Long usuarioId = usuarioService.recuperarUsuarioId(request);
        if (descricao == null)
            return despesaService.buscarTodasDespesasDoUsuario(usuarioId, pageble);
        return despesaService.buscarTodasDespesasDoUsuarioComDescricao(usuarioId, pageble,
                descricao);
    }

    @GetMapping("/{ano}/{mes}")
    public Page<Despesa> listarTodasDespesasPorMesAno(
            @PageableDefault(sort = "descricao", direction = Sort.Direction.ASC) Pageable pageble,
            HttpServletRequest request, @PathVariable Integer ano, @PathVariable Integer mes) {
        Long usuarioId = usuarioService.recuperarUsuarioId(request);
        return despesaService.buscarTodasDespesasDoUsuarioPorData(usuarioId, pageble, mes, ano);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalharDespesa(@PathVariable Long id, HttpServletRequest request) {
        Usuario usuario = usuarioService.recuperarUsuario(request);
        Optional<Despesa> despesaOptional = despesaService.buscarUmaDespesasDoUsuario(usuario, id);
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
        if (!despesaService.isDescricaoAndDataValida(despesa)) {
            return ResponseEntity.badRequest()
                    .body("Já existe uma despesa com está descrição no mês de " + despesa.getData()
                            .getMonth()
                            .getDisplayName(TextStyle.FULL, new Locale("pt", "BR")));
        }
        URI uri = despesaService.saveAndBuildUri(despesa, uriComponentsBuilder, "/despesas/{id}");
        return ResponseEntity.created(uri)
                .body(new DespesaDto(despesa));

    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizaDespesa(@PathVariable Long id,
                                             @RequestBody @Valid DespesaForm despesaForm,
                                             HttpServletRequest request) {
        Usuario usuario = usuarioService.recuperarUsuario(request);
        Despesa despesa = despesaForm.converter(usuario);
        Optional<Despesa> despesaAntiga = despesaService.buscarUmaDespesaDoUsuario(
                usuario.getId(), id);

        if (despesaAntiga.isEmpty()) {
            return ResponseEntity.notFound()
                    .build();
        }

        if (despesaService.existeDespesaComDescricaoIgualMasIdDiferentes(despesa, id)) {
            return ResponseEntity.badRequest()
                    .body("Não foi possível atualizar a despesa pois"
                            + " já existe uma despesa com está descrição no mês de "
                            + despesaAntiga.get()
                            .getData()
                            .getMonth()
                            .getDisplayName(TextStyle.FULL, new Locale("pt", "BR")));
        }

        return ResponseEntity.ok(
                new DespesaDto(despesaForm.atualizarDespesa(despesaAntiga.get())));

    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteReceita(@PathVariable Long id) {
        return despesaService.deleteDespesa(id);
    }
}
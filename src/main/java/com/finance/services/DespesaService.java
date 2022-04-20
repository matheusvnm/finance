package com.finance.services;

import com.finance.domain.Despesa;
import com.finance.domain.Usuario;
import com.finance.repository.DespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Service
public class DespesaService {

    @Autowired
    private DespesaRepository despesaRepository;


    public boolean isDescricaoAndDataValida(Despesa despesa) {
        return !despesa.existeDespesaComMesmaDescricaoNoMes(despesaRepository);
    }

    public Page<Despesa> buscarTodasDespesasDoUsuario(Usuario usuario, Pageable pageable) {
        return despesaRepository.findAllByUsuarioId(pageable, usuario.getId());
    }


    public Optional<Despesa> buscarDespesasDoUsuario(Usuario usuario, Long id) {
        return usuario.getDespesas()
                .stream()
                .filter(despesa -> despesa.getId()
                        .equals(id))
                .findFirst();
    }

    public ResponseEntity<?> deleteDespesa(Long id) {
        Optional<Despesa> despesaOptinal = despesaRepository.findById(id);
        if (despesaOptinal.isPresent()) {
            despesaRepository.deleteById(id);
            return ResponseEntity.ok()
                    .build();
        }
        return ResponseEntity.notFound()
                .build();
    }

    public URI saveAndBuildUri(Despesa despesa, UriComponentsBuilder uriComponentsBuilder,
                               String urlPath) {
        despesaRepository.save(despesa);
        return uriComponentsBuilder.path(urlPath)
                .buildAndExpand(despesa.getId())
                .toUri();
    }

    public Page<Despesa> buscarTodasDespesasDoUsuarioComDescricao(Usuario usuario, Pageable pageble,
                                                                  String descricao) {
        return despesaRepository.findAllByUsuarioIdAndDescricaoContaining(pageble, usuario.getId(),
                descricao);
    }
}

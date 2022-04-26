package com.finance.services;

import com.finance.domain.Receita;
import com.finance.domain.Usuario;
import com.finance.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Service
public class ReceitaService {

    @Autowired
    private ReceitaRepository receitaRepository;

    public boolean isDescricaoAndDataValida(Receita receita) {
        return !receita.existeReceitaComMesmaDescricaoNoMes(receitaRepository);
    }

    public URI saveAndBuildUri(Receita receita, UriComponentsBuilder uriComponentsBuilder,
                               String urlPath) {
        receitaRepository.save(receita);
        return uriComponentsBuilder.path(urlPath)
                .buildAndExpand(receita.getId())
                .toUri();
    }


    public Page<Receita> buscarTodasReceitasDoUsuario(Long usuarioId, Pageable pageable) {
        return receitaRepository.findAllByUsuarioId(pageable, usuarioId);
    }

    public Optional<Receita> buscarUmaReceitaDoUsuario(Usuario usuario, Long id) {
        return usuario.getReceitas()
                .stream()
                .filter(receita -> receita.getId()
                        .equals(id))
                .findFirst();
    }

    public ResponseEntity<?> deleteReceita(Long id) {
        Optional<Receita> receitaOptional = receitaRepository.findById(id);
        if (receitaOptional.isPresent()) {
            receitaRepository.deleteById(id);
            return ResponseEntity.ok()
                    .build();
        }
        return ResponseEntity.notFound()
                .build();
    }

    public Page<Receita> buscarTodasReceitasDoUsuarioComDescricao(Long usuarioId, Pageable pageble,
                                                                  String descricao) {
        return receitaRepository.findAllByUsuarioIdAndDescricaoContaining(pageble, usuarioId,
                descricao);
    }

    public Page<Receita> buscarTodasReceitasDoUsuarioPorData(Long usuarioId, Pageable pageble,
                                                             Integer mes, Integer ano) {
        return receitaRepository.findAllByUsuarioIdAndData_MesAndData_Ano(usuarioId, mes, ano,
                pageble);
    }
}

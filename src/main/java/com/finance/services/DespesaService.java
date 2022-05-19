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

    public boolean existeDespesaComDescricaoIgualMasIdDiferentes(Despesa despesa, Long id) {
        return despesa.existeDespesaComDescricaoIgualMasIdDiferentes(despesaRepository, id);
    }

    public Page<Despesa> buscarTodasDespesasDoUsuario(Long usuarioId, Pageable pageable) {
        return despesaRepository.findAllByUsuarioId(pageable, usuarioId);
    }


    public Optional<Despesa> buscarUmaDespesasDoUsuario(Usuario usuario, Long id) {
        return usuario.getDespesas()
                .stream()
                .filter(despesa -> despesa.getId()
                        .equals(id))
                .findFirst();
    }

    public Optional<Despesa> buscarUmaDespesaDoUsuario(Long usuarioId, Long id) {
        return despesaRepository.findFirstByIdAndUsuarioId(id, usuarioId);
    }

    //FIXME Serviços não devem lidar com a entidade de respostas
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

    public Page<Despesa> buscarTodasDespesasDoUsuarioComDescricao(Long usuarioId, Pageable pageble,
                                                                  String descricao) {
        return despesaRepository.findAllByUsuarioIdAndDescricaoContaining(pageble, usuarioId,
                descricao);
    }

    public Page<Despesa> buscarTodasDespesasDoUsuarioPorData(Long usuarioId, Pageable pageble,
                                                             Integer mes, Integer ano) {
        return despesaRepository.findAllByUsuarioIdAndData_MesAndData_Ano(usuarioId, mes, ano, pageble);
    }
}

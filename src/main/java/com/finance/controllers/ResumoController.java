package com.finance.controllers;

import com.finance.domain.Resumo;
import com.finance.services.ResumoService;
import com.finance.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/resumo")
public class ResumoController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ResumoService resumoService;

    @GetMapping("/{ano}/{mes}")
    public Resumo obterResumo(HttpServletRequest request, @PathVariable Integer ano, @PathVariable Integer mes){
        Long usuarioId = usuarioService.recuperarUsuarioId(request);
        return resumoService.obterResumo(usuarioId, mes, ano);
    }
}

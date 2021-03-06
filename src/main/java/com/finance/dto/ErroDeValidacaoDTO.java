package com.finance.dto;

public class ErroDeValidacaoDTO {

    private final String campo;
    private final String erro;

    public ErroDeValidacaoDTO(String campo, String erro) {
        this.campo = campo;
        this.erro = erro;
    }

    public String getCampo() {
        return campo;
    }

    public String getErro() {
        return erro;
    }
}

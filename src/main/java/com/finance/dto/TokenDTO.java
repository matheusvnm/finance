package com.finance.dto;

public class TokenDTO {
    private String token;
    private String tipoToken;

    public TokenDTO(String token, String bearer) {
        this.token = token;
        this.tipoToken = bearer;
    }

    public String getToken() {
        return token;
    }

    public String getTipoToken() {
        return tipoToken;
    }
}

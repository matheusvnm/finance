package com.finance.dto;

public class TokenDto {
    private String token;
    private String tipoToken;

    public TokenDto(String token, String bearer) {
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

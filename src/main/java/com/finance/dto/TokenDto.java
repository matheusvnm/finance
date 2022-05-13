package com.finance.dto;

public class TokenDto {
    private String token;
    private String tipoToken;

    public TokenDto(String token, String tipoToken) {
        this.token = token;
        this.tipoToken = tipoToken;
    }

    public TokenDto() {
    }

    public String getToken() {
        return token;
    }

    public String getTipoToken() {
        return tipoToken;
    }
}

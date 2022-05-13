package com.finance.exception;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(String usuario_nao_encontrado) {
        super(usuario_nao_encontrado);
    }
}

package com.finance.exception;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(String usuario_não_encontrado) {
        super(usuario_não_encontrado);
    }
}

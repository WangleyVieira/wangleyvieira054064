package com.wangley.musicapi.exception;

public class ResourceNotFoundException extends RuntimeException {

    /* Construtor para criar a exceção com uma mensagem personalizada */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

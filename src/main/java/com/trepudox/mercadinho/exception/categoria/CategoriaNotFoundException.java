package com.trepudox.mercadinho.exception.categoria;

import com.trepudox.mercadinho.exception.NotFoundException;

public class CategoriaNotFoundException extends NotFoundException {

    public CategoriaNotFoundException(String msg) {
        super(msg);
    }

    public CategoriaNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

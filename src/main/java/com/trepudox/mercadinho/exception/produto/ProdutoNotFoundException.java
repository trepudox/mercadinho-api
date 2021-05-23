package com.trepudox.mercadinho.exception.produto;

import com.trepudox.mercadinho.exception.NotFoundException;

public class ProdutoNotFoundException extends NotFoundException {

    public ProdutoNotFoundException(String msg) {
        super(msg);
    }

    public ProdutoNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

}

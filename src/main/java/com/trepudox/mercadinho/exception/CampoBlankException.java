package com.trepudox.mercadinho.exception;

public class CampoBlankException extends Exception {

    public CampoBlankException() {
        super("Esse campo não pode estar nulo ou em branco!");
    }

    public CampoBlankException(String nomeCampo) {
        super("O campo '" + nomeCampo + "' não pode estar nulo ou em branco!");
    }

    public CampoBlankException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

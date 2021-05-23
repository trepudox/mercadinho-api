package com.trepudox.mercadinho.exception;

import com.trepudox.mercadinho.exception.produto.ProdutoNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        if (e instanceof ProdutoNotFoundException)
            status = HttpStatus.NOT_FOUND;

        ApiExceptionClass erro = new ApiExceptionClass(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")),
               status , e.getMessage());

        return ResponseEntity.status(status).body(erro);
    }
}

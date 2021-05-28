package com.trepudox.mercadinho.exception;

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
        ZonedDateTime timestamp = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String msg = e.getMessage();

        if (e instanceof NotFoundException)
            status = HttpStatus.NOT_FOUND;

        if (msg.startsWith("Failed to convert value of type 'java.lang.String' to required type "))
            msg = "Impossível converter o tipo de dado enviado.";



        String type = status.is1xxInformational() ? "Informational" : status.is2xxSuccessful() ? "Successful" :
                status.is3xxRedirection() ? "Redirection" : status.is4xxClientError() ? "Error" : "Internal Error";

        ApiExceptionClass erro = new ApiExceptionClass(timestamp, msg, type);

        return ResponseEntity.status(status).body(erro);
    }
}

package com.trepudox.mercadinho.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Getter @Setter
@AllArgsConstructor
public class ApiExceptionClass {

    private ZonedDateTime timestamp;
    private HttpStatus httpStatus;
    private String message;

}

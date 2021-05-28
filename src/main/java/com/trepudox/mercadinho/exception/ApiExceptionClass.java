package com.trepudox.mercadinho.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter @Setter
@AllArgsConstructor
public class ApiExceptionClass {

    private ZonedDateTime timestamp;
    private String message;
    private String type;

}

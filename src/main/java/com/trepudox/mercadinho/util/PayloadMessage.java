package com.trepudox.mercadinho.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PayloadMessage {
    private ZonedDateTime timestamp;
    private String message;
    private String type;

    public PayloadMessage(String message, HttpStatus status) {
        this.timestamp = ZonedDateTime.now();
        this.message = message;
        this.type = status.is1xxInformational() ? "Informational" : status.is2xxSuccessful() ? "Successful" :
                status.is3xxRedirection() ? "Redirection" : status.is4xxClientError() ? "Error" : "Internal Error";
    }
}

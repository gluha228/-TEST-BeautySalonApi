package com.powerit.beautysalonapi.security.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class FailedAuthentificationResponse {

    private String message;

}

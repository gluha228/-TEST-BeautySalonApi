package com.powerit.beautysalonapi.security.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FailedAuthenticationResponse {

    private String message;

}

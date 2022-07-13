package com.powerit.beautysalonapi.security.controllers.dto;

import lombok.Data;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class AuthRequestDTO {

    @NotNull(message = "Email should not be null/blank!")
    @Email(message = "Invalid mail provided!") // TODO: add regexp validation
    @Size(min = 5, message = "Invalid mail length!")
    private String username;

    @NotNull(message = "Password should not be null/blank!")
    @Size(min = 5, message = "Invalid password length.")
    private String password;
}

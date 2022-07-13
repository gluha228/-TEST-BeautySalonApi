package com.powerit.beautysalonapi.security.controllers.dto;

import lombok.Data;
import org.springframework.lang.NonNull;

@Data
public class LoginForm {
    private String username;
    private String password;
}

package com.powerit.beautysalonapi.security.controllers;

import com.powerit.beautysalonapi.mock.MyUserDetails;
import com.powerit.beautysalonapi.security.controllers.dto.AuthRequestDTO;
import com.powerit.beautysalonapi.security.controllers.dto.FailedAuthenticationResponse;
import com.powerit.beautysalonapi.security.exceptions.UserAlreadyExistsException;
import com.powerit.beautysalonapi.security.toImpl.RoleExService;
import com.powerit.beautysalonapi.security.toImpl.UserDetailsExService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;

@RestController
public class RegistrationController {

    private final UserDetailsExService userDetailsExService;
    private final PasswordEncoder passwordEncoder;
    private final RoleExService roleExService;

    public RegistrationController(UserDetailsExService userDetailsExService, PasswordEncoder passwordEncoder, RoleExService roleExService) {
        this.userDetailsExService = userDetailsExService;
        this.passwordEncoder = passwordEncoder;
        this.roleExService = roleExService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<Object> registration(@Valid @RequestBody AuthRequestDTO loginForm) {

        Object authResponse = null;

        HttpStatus authStatus = HttpStatus.CREATED;

        try {
            userDetailsExService.addUser(
                    new MyUserDetails(
                            loginForm.getEmail(),
                            passwordEncoder.encode(loginForm.getPassword()),
                            new HashSet<>(List.of(roleExService.getRoleByName("ROLE_USER")))));

        } catch (UserAlreadyExistsException e) {

            authResponse = new FailedAuthenticationResponse("user already exists");

            authStatus = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(authResponse, authStatus);
    }
}

package com.powerit.beautysalonapi.security.controllers;

import com.powerit.beautysalonapi.security.controllers.dto.AuthRequestDTO;
import com.powerit.beautysalonapi.security.controllers.dto.AuthResponse;

import com.powerit.beautysalonapi.security.controllers.dto.FailedAuthenticationResponse;
import com.powerit.beautysalonapi.security.controllers.dto.TokenDto;
import com.powerit.beautysalonapi.security.token.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

@RestController
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Autowired
    public LoginController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Object> getToken(@Valid @RequestBody AuthRequestDTO authRequestDTO) {

        Object authResponse;

        HttpStatus authStatus = HttpStatus.ACCEPTED;

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequestDTO.getEmail(),
                            authRequestDTO.getPassword()));

            authResponse = new AuthResponse(
                    tokenService.generateAccessToken(authRequestDTO.getEmail()),
                    tokenService.generateRefreshToken(authRequestDTO.getEmail()));

        } catch (AuthenticationException authenticationException) {

            if (Objects.equals(authenticationException.getMessage(), "Bad credentials"))
                authResponse = new FailedAuthenticationResponse("Invalid Password");
            else
                authResponse = new FailedAuthenticationResponse("Invalid Username");

            authStatus = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(authResponse, authStatus);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> refreshAccessToken(@NonNull @RequestBody TokenDto tokenDto) {
        logger.info("refreshing token: " + tokenDto.getToken());
        return ResponseEntity.ok(new TokenDto(
                tokenService.generateAccessToken(tokenService.validateAndParseRefresh(tokenDto.getToken()))));
    }
}

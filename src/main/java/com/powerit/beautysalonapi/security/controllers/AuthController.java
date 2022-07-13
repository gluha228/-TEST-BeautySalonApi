package com.powerit.beautysalonapi.security.controllers;

import com.powerit.beautysalonapi.mock.MyUserDetails;
import com.powerit.beautysalonapi.security.controllers.dto.AuthRequestDTO;
import com.powerit.beautysalonapi.security.controllers.dto.AuthResponse;

import com.powerit.beautysalonapi.security.controllers.dto.FailedAuthentificationResponse;
import com.powerit.beautysalonapi.security.controllers.dto.TokenDto;
import com.powerit.beautysalonapi.security.toImpl.RoleExService;
import com.powerit.beautysalonapi.security.toImpl.UserDetailsExService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsExService userDetailsExService;
    private final RoleExService roleExService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService,
                          PasswordEncoder passwordEncoder, UserDetailsExService userDetailsExService,
                          RoleExService roleExService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsExService = userDetailsExService;
        this.roleExService = roleExService;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> getToken(@Valid @RequestBody AuthRequestDTO authRequestDTO) {

        Object authResponse;

        HttpStatus authStatus = HttpStatus.ACCEPTED;

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(),
                            authRequestDTO.getPassword()));

            authResponse = new AuthResponse(
                    tokenService.generateAccessToken(authRequestDTO.getUsername()),
                    tokenService.generateRefreshToken(authRequestDTO.getUsername()));

        } catch (AuthenticationException authenticationException) {

            authResponse = new FailedAuthentificationResponse("Authentication Failed");
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

    @PostMapping("/register")
    public ResponseEntity<Boolean> registration(@RequestBody AuthRequestDTO loginForm) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userDetailsExService.addUser(
                new MyUserDetails(loginForm.getUsername(), passwordEncoder.encode(loginForm.getPassword()),
                        new HashSet<>(List.of(roleExService.getRoleByName("ROLE_USER"))))));
    }
}

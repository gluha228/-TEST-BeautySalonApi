package com.powerit.beautysalonapi.security.controllers;

import com.powerit.beautysalonapi.mock.MyUserDetails;
import com.powerit.beautysalonapi.security.controllers.dto.AuthResponse;
import com.powerit.beautysalonapi.security.controllers.dto.LoginForm;
import com.powerit.beautysalonapi.security.controllers.dto.TokenDto;
import com.powerit.beautysalonapi.security.controllers.validator.CustomValidator;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.Binding;
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
    private final CustomValidator customValidator;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService,
                          PasswordEncoder passwordEncoder, UserDetailsExService userDetailsExService,
                          RoleExService roleExService, CustomValidator customValidator) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsExService = userDetailsExService;
        this.roleExService = roleExService;
        this.customValidator = customValidator;
    }

    //забавно, но без @Validated работает точно так же
    //чет такой способ валидации не очень зашел(или я плохо разобрался), как по мне даже удобнее сделать свой
    //"валидатор-класс" не наследующийся ни от чего, а просто кидающий ексепшоны(и аннотации не нужно будет приписывать)
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> getToken(@Validated @RequestBody LoginForm loginForm, BindingResult bindingResult) {
        customValidator.validate(loginForm, bindingResult);
        logger.info("auth: " + loginForm);
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword())
            );
        } catch (AuthenticationException e) {
            String message = e.getMessage();
            if (message.startsWith("UserDetails")) message = "invalid username";
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, message);
        }
        return ResponseEntity.ok(new AuthResponse(
                tokenService.generateAccessToken(loginForm.getUsername()),
                tokenService.generateRefreshToken(loginForm.getUsername())
        ));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> refreshAccessToken(@NonNull @RequestBody TokenDto tokenDto) {
        logger.info("refreshing token: " + tokenDto.getToken());
        return ResponseEntity.ok(new TokenDto(
                tokenService.generateAccessToken(tokenService.validateAndParseRefresh(tokenDto.getToken()))));
    }

    @PostMapping("/register")
    public ResponseEntity<Boolean> registration(@RequestBody LoginForm loginForm) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userDetailsExService.addUser(
                new MyUserDetails(loginForm.getUsername(), passwordEncoder.encode(loginForm.getPassword()),
                        new HashSet<>(List.of(roleExService.getRoleByName("ROLE_USER"))))));
    }
}

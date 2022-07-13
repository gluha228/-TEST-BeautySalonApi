package com.powerit.beautysalonapi.security.token;

import org.springframework.stereotype.Component;

@Component
public class TokenService {

    private final String refreshSecret = "refreshSecret";
    private final String accessSecret = "accessSecret";
    private final long refreshLifeMins = 600;
    private final long accessLifeMins = 15;
    private final TokenChecker tokenChecker;
    private final TokenGenerator tokenGenerator;

    public TokenService(TokenChecker tokenChecker, TokenGenerator tokenGenerator) {
        this.tokenChecker = tokenChecker;
        this.tokenGenerator = tokenGenerator;
    }

    public String generateRefreshToken(String username) {
        return tokenGenerator.generateToken(username, refreshSecret, refreshLifeMins);
    }

    public String generateAccessToken(String username) {
        return tokenGenerator.generateToken(username, accessSecret, accessLifeMins);
    }

    public String validateAndParseAccess(String token) {
        return tokenChecker.getUsernameFromToken(token, accessSecret);
    }

    public String validateAndParseRefresh(String token) {
        return tokenChecker.getUsernameFromToken(token, refreshSecret);
    }
}

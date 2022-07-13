package com.powerit.beautysalonapi.security.token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenGenerator {
    private final Logger logger = LoggerFactory.getLogger(TokenGenerator.class);

    public String generateToken(String username, String secret, long tokenLifeMins) {
        logger.info("create token for user: " + username);
        return Jwts.builder().setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenLifeMins*60*1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }
}

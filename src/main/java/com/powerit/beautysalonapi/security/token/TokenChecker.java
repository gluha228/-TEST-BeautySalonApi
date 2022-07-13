package com.powerit.beautysalonapi.security.token;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class TokenChecker {

    private final Logger logger = LoggerFactory.getLogger(TokenChecker.class);

    public String getUsernameFromToken(String token, String secret) {
        logger.info("get user from token: " + token);
        Claims claims;
        try {
            claims =  Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (SignatureException e) {
            logger.warn("wrong signature");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "wrong token signature");
        } catch (MalformedJwtException e) {
            logger.warn("invalid token");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid token");
        } catch (ExpiredJwtException e) {
            logger.warn("expired token");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "expired token");
        }
        if (claims == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "token parse fail");
        logger.info("token validated successfully");
        return claims.getSubject();
    }
}

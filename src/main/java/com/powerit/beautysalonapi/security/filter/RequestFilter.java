package com.powerit.beautysalonapi.security.filter;

import com.powerit.beautysalonapi.security.toImpl.UserDetailsExService;
import com.powerit.beautysalonapi.security.token.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RequestFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserDetailsExService userDetailsService;
    private final Logger logger = LoggerFactory.getLogger(RequestFilter.class);
    private final DynamicAntMatcherAnalog dynamicAntMatcherAnalog;

    @Autowired
    public RequestFilter(TokenService tokenChecker, UserDetailsExService userDetailsService, DynamicAntMatcherAnalog dynamicAntMatcherAnalog) {
        this.tokenService = tokenChecker;
        this.userDetailsService = userDetailsService;
        this.dynamicAntMatcherAnalog = dynamicAntMatcherAnalog;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {

            String header = request.getHeader("Authorization");
            logger.info("request: " + request.getRequestURI());
            if (!request.getRequestURI().startsWith("/auth") && header != null && header.startsWith("Bearer")) {
                String token = header.substring(7);
                String username = tokenService.validateAndParseAccess(token);
                logger.info("user: " + username);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (userDetails == null) {
                    logger.info("no such enabled user");
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "no such enabled user");
                }
                dynamicAntMatcherAnalog.authorize(userDetails.getAuthorities(), request.getRequestURI());

                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
            } else logger.info("auth header lack");
            System.out.println("path: " + request.getRequestURI());
            filterChain.doFilter(request, response);
        } catch (ResponseStatusException e) {
            response.setStatus(e.getRawStatusCode());
            response.sendError(e.getRawStatusCode(), e.getMessage() + "gg");
        }
    }
}

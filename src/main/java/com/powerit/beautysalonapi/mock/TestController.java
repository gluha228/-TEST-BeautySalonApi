package com.powerit.beautysalonapi.mock;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/test")
    public String getAnon(Principal principal) {
        System.out.println(principal);
        return "hola boba anon";
    }

    @GetMapping("/test_onlyadmin")
    @PreAuthorize("hasRole('ADMIN')")
    public String getAdmin(Principal principal) {
        System.out.println(principal.getName() + "|" + principal);
        return "hola boba admin";
    }

    @GetMapping("/test_onlyuser")
    @PreAuthorize("hasRole('USER')")
    public String getUser(Principal principal) {
        System.out.println(principal.getName() + "|" + principal);
        return "hola boba user";
    }
}

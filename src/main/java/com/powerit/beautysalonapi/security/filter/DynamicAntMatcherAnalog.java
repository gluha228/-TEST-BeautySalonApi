package com.powerit.beautysalonapi.security.filter;

import com.powerit.beautysalonapi.mock.RoleService;
import com.powerit.beautysalonapi.security.toImpl.RoleEx;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Component
public class DynamicAntMatcherAnalog {

    private final long refreshMinutes = 15;
    private List<RoleEx> rolesLocalStorage;
    private final RoleService roleService;

    public DynamicAntMatcherAnalog(RoleService roleService) {
        this.roleService = roleService;
        actualize();
    }

    //сделано для имитации паттернов антматчера, типа /admin/*
    private boolean antMatcher(String requestURL, String matchingURL) {
        System.out.println("ant: " + requestURL + " " + matchingURL);
        if (Objects.equals(requestURL, matchingURL)) return true;
        for (int i = 0; requestURL.charAt(i) == matchingURL.charAt(i); i++) {
            if (matchingURL.charAt(i) == '*') return true;
        }
        return false;
    }

    //от предоставленных авторитис берёт только их название, так что если hibernate relation role <-> permission будет
    //настроен с Lazy Loading, то пермишены не будут грузиться из бд просто так
    public void authorize(Collection<? extends GrantedAuthority> roles, String path) {
        if (rolesLocalStorage.stream()
                //если роль в местном хранилище совпадает с одной из предоставленных
                .filter(it -> roles.stream().anyMatch(role -> Objects.equals(role.getAuthority(), it.getAuthority())))
                //если среди этих ролей есть хоть одна с нужным path
                .anyMatch(it -> it.getPermissions().stream().anyMatch(perm -> antMatcher(path, perm.getPath())))
        ) return;
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    private void actualize() {
        new Thread(() -> {
            while (true) {
                rolesLocalStorage = roleService.getAll();
                System.out.println("rolesLocalStorage updated");
                try {
                    Thread.sleep(refreshMinutes * 60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

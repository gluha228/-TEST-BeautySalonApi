package com.powerit.beautysalonapi.security.toImpl;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface RoleEx extends GrantedAuthority {
    List<Permission> getPermissions();
}

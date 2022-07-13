package com.powerit.beautysalonapi.security.toImpl;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public interface UserDetailsEx extends UserDetails {
    @Override
    Collection<? extends RoleEx> getAuthorities();
}

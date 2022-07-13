package com.powerit.beautysalonapi.security.toImpl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailsExService extends UserDetailsService{
    @Override
    UserDetailsEx loadUserByUsername(String username) throws UsernameNotFoundException;
    boolean addUser(UserDetailsEx userDetailsEx);
}

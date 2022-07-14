package com.powerit.beautysalonapi.security.toImpl;

import com.powerit.beautysalonapi.security.exceptions.UserAlreadyExistsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailsExService extends UserDetailsService{
    @Override
    UserDetailsEx loadUserByUsername(String username) throws UsernameNotFoundException;
    void addUser(UserDetailsEx userDetailsEx) throws UserAlreadyExistsException;
}

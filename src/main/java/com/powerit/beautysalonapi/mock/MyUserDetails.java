package com.powerit.beautysalonapi.mock;

import com.powerit.beautysalonapi.security.toImpl.RoleEx;
import com.powerit.beautysalonapi.security.toImpl.UserDetailsEx;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

@AllArgsConstructor
@Getter
@Setter
public class MyUserDetails implements UserDetailsEx {

    private String username;
    private String password;
    private HashSet<RoleEx> authorities;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

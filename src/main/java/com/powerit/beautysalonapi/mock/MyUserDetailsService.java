package com.powerit.beautysalonapi.mock;

import com.powerit.beautysalonapi.security.exceptions.UserAlreadyExistsException;
import com.powerit.beautysalonapi.security.toImpl.UserDetailsEx;
import com.powerit.beautysalonapi.security.toImpl.UserDetailsExService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
public class MyUserDetailsService implements UserDetailsExService {

    private final List<UserDetailsEx> usersDetails = new ArrayList<>();
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Autowired
    public MyUserDetailsService(PasswordEncoder passwordEncoder, RoleService roleService) {
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        addUser(new MyUserDetails("admin@gmail.com",
                this.passwordEncoder.encode("adminul"),
                new HashSet<>(List.of(roleService.getRoleByName("ROLE_ADMIN")))));
    }

    @Override
    public void addUser(UserDetailsEx user) throws UserAlreadyExistsException {
        System.out.println("add user" + user.getUsername());
        if (loadUserByUsername(user.getUsername()) != null) throw new UserAlreadyExistsException();
        usersDetails.add(user);
    }

    @Override
    public UserDetailsEx loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return usersDetails.stream().filter(it -> Objects.equals(it.getUsername(), username)).toList().get(0);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

}
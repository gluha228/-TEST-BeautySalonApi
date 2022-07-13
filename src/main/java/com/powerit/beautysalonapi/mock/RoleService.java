package com.powerit.beautysalonapi.mock;

import com.powerit.beautysalonapi.security.toImpl.Permission;
import com.powerit.beautysalonapi.security.toImpl.RoleEx;
import com.powerit.beautysalonapi.security.toImpl.RoleExService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class RoleService implements RoleExService {

    private List<RoleEx> roles = List.of(
            new MyRole("ROLE_USER",  List.of(new PermissionImpl("/api/test_onlyuser"))),
            new MyRole("ROLE_ADMIN", List.of(new PermissionImpl("/api/test_onlyadmin")))
            );

    @Override
    public RoleEx getRoleByName(String name) {
        return roles.stream().filter(it -> Objects.equals(it.getAuthority(), name)).toList().get(0);
    }
    @Override
    public List<RoleEx> getAll() {
        return roles;
    }

    @Override
    public boolean save(RoleEx role) {
        if (roles.contains(role)) return false;
        return roles.add(role);
    }
}

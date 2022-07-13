package com.powerit.beautysalonapi.security.toImpl;

import com.powerit.beautysalonapi.mock.MyRole;

import java.util.List;

public interface RoleExService {
    RoleEx getRoleByName(String name);

    List<RoleEx> getAll();

    boolean save(RoleEx role);
}

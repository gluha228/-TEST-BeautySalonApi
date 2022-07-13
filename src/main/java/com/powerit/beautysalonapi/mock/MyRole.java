package com.powerit.beautysalonapi.mock;

import com.powerit.beautysalonapi.security.toImpl.Permission;
import com.powerit.beautysalonapi.security.toImpl.RoleEx;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@ToString
@Getter
public class MyRole implements RoleEx {

    private String name;
    private List<Permission> permissions;

    @Override
    public String getAuthority() {
        return name;
    }


}

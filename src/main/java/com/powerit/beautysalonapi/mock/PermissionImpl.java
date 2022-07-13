package com.powerit.beautysalonapi.mock;

import com.powerit.beautysalonapi.security.toImpl.Permission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class PermissionImpl implements Permission {
    private String path;
}

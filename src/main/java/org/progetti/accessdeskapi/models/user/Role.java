package org.progetti.accessdeskapi.models.user;

import lombok.Getter;

import java.util.Set;

@Getter
public enum Role {
    EMPLOYEE(Set.of(
         Authorities.READ_REQUEST,
         Authorities.CREATE_REQUEST

    )),
    MANAGER(Set.of(
            Authorities.READ_ALL_REQUEST,
            Authorities.MODIFY_REQUEST,
            Authorities.DELETE_REQUEST

    ));

    private final Set<Authorities> permissions;

    Role(Set<Authorities> permissions) {
        this.permissions = permissions;
    }
}

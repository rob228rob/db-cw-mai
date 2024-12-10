package com.mai.db_cw_rbl.infrastructure.exceptions;

public class UserRoleNotFoundException extends RuntimeException {
    public UserRoleNotFoundException(String roleNotFound) {
        super(roleNotFound);
    }
}

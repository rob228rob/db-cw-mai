package com.mai.db_cw_rbl.InfrastructurePackage.CommonPackage.CustomExceptions;

public class UserRoleNotFoundException extends RuntimeException {
    public UserRoleNotFoundException(String roleNotFound) {
        super(roleNotFound);
    }
}

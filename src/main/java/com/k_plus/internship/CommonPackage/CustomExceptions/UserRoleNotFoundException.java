package com.k_plus.internship.CommonPackage.CustomExceptions;

public class UserRoleNotFoundException extends RuntimeException {
    public UserRoleNotFoundException(String roleNotFound) {
        super(roleNotFound);
    }
}

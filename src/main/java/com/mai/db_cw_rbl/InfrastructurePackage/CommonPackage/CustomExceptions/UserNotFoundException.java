package com.mai.db_cw_rbl.InfrastructurePackage.CommonPackage.CustomExceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String s) {
        super(s);
    }
}

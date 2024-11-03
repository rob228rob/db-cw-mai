package com.mai.db_cw_rbl.InfrastructurePackage.CommonPackage.CustomExceptions;

public class InvalidUserInfoException extends RuntimeException {
    public InvalidUserInfoException(String message) {
        super(message);
    }
}

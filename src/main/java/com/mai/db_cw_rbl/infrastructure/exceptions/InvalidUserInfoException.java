package com.mai.db_cw_rbl.infrastructure.exceptions;

public class InvalidUserInfoException extends RuntimeException {
    public InvalidUserInfoException(String message) {
        super(message);
    }
}

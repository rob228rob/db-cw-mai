package com.mai.db_cw_rbl.infrastructure.exceptions;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String msg) {
        super(msg);
    }
}

package com.mai.db_cw_rbl.infrastructure.exceptions;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String msg) {
        super(msg);
    }
}

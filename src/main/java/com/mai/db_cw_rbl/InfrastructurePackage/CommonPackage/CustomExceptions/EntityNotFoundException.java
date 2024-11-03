package com.mai.db_cw_rbl.InfrastructurePackage.CommonPackage.CustomExceptions;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String msg) {
        super(msg);
    }
}

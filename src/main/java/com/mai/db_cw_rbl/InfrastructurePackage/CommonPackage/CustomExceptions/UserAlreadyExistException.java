package com.mai.db_cw_rbl.InfrastructurePackage.CommonPackage.CustomExceptions;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String msg) {
        super(msg);
    }
}

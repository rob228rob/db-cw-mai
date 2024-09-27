package com.k_plus.internship.CommonPackage.CustomExceptions;

public class InvalidUserInfoException extends RuntimeException {
    public InvalidUserInfoException(String message) {
        super(message);
    }
}

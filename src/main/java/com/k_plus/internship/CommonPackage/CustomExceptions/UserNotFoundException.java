package com.k_plus.internship.CommonPackage.CustomExceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String s) {
        super(s);
    }
}

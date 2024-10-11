package com.k_plus.internship.CommonPackage.CustomExceptions;

public class OptionNotFoundException extends RuntimeException {
    public OptionNotFoundException(String s) {
        super(s);
    }
}

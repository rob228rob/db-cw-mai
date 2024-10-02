package com.k_plus.internship.CommonPackage.CustomExceptions;

public class QuestionNotFoundException extends RuntimeException {
    public QuestionNotFoundException(String s) {
        super(s);
    }
}

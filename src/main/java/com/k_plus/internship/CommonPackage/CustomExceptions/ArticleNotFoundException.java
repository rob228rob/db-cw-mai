package com.k_plus.internship.CommonPackage.CustomExceptions;

public class ArticleNotFoundException extends RuntimeException {
    public ArticleNotFoundException(String s) {
        super(s);
    }
}

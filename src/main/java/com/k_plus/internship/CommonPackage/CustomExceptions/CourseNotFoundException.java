package com.k_plus.internship.CommonPackage.CustomExceptions;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(String s) {
        super(s);
    }
}

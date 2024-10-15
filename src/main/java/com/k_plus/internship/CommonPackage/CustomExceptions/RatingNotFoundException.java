package com.k_plus.internship.CommonPackage.CustomExceptions;

import java.util.UUID;

public class RatingNotFoundException extends RuntimeException {
    public RatingNotFoundException(UUID userId) {
        super("rating with id " + userId + " not found");
    }

    public RatingNotFoundException(String message) {
        super(message);
    }
}

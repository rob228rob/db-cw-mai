package com.k_plus.internship.CommonPackage;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.k_plus.internship.CommonPackage.CustomExceptions.ArticleNotFoundException;
import com.k_plus.internship.CommonPackage.CustomExceptions.CourseNotFoundException;
import com.k_plus.internship.CommonPackage.CustomExceptions.InvalidUserInfoException;
import com.k_plus.internship.CommonPackage.CustomExceptions.TestingNotFoundException;
import com.k_plus.internship.CommonPackage.CustomExceptions.UserNotFoundException;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> notFoundException(UserNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(exception.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(TestingNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> notFoundException(TestingNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(exception.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(ArticleNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> notFoundException(ArticleNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(exception.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(InvalidUserInfoException.class)
    public ResponseEntity<ErrorResponseDto> invalidUserInfoException(InvalidUserInfoException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(exception.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> badCredentialsException(BadCredentialsException exception) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponseDto(exception.getMessage(), HttpStatus.FORBIDDEN.value()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDto> runtimeException(RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(exception.getMessage() + "\n" + exception.getStackTrace(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> runtimeException(CourseNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(exception.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

}

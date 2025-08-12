package com.fluffyknightz.ebook_library.exception;

import io.awspring.cloud.s3.S3Exception;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 Bad Request: validation errors, missing parameters, unreadable messages, illegal args
    @ExceptionHandler({MethodArgumentNotValidException.class, MissingServletRequestParameterException.class, HttpMessageNotReadableException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorDetails> handleBadRequest(Exception ex, HttpServletRequest request) {
        String message;
        if (ex instanceof MethodArgumentNotValidException validationEx) {
            message = validationEx.getBindingResult().getFieldErrors().stream().map(
                    DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
        } else {
            message = ex.getMessage();
        }
        return buildResponse(request, HttpStatus.BAD_REQUEST, message);
    }

    // 404 Not Found: resource or endpoint not found
    @ExceptionHandler({ResourceNotFoundException.class, NoHandlerFoundException.class})
    public ResponseEntity<ErrorDetails> handleNotFound(Exception ex, HttpServletRequest request) {
        String message = ex.getMessage();
        return buildResponse(request, HttpStatus.NOT_FOUND, message);
    }

    // 403 Forbidden: access denied
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetails> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return buildResponse(request, HttpStatus.FORBIDDEN, ex.getMessage());
    }

    // 405 Method Not Allowed
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorDetails> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                 HttpServletRequest request) {
        String message = "Method " + ex.getMethod() + " not supported. Supported: " + String.join(", ",
                                                                                                  Objects.requireNonNull(
                                                                                                                 ex.getSupportedHttpMethods())
                                                                                                         .stream()
                                                                                                         .map(HttpMethod::name)
                                                                                                         .toList());
        return buildResponse(request, HttpStatus.METHOD_NOT_ALLOWED, message);
    }

    // 409 Conflict: duplicate key or data integrity
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorDetails> handleConflict(DuplicateKeyException ex, HttpServletRequest request) {
        return buildResponse(request, HttpStatus.CONFLICT, ex.getMessage());
    }

    // 405 Unauthorized: Login Fail
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDetails> handleBadCredential(HttpServletRequest request) {
        return buildResponse(request, HttpStatus.UNAUTHORIZED, "Username or Password is incorrect. Please try again.");
    }

    // 403 Forbidden: Account disabled
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorDetails> handleDisabled(HttpServletRequest request) {
        return buildResponse(request, HttpStatus.FORBIDDEN, "Your Account is disabled.");
    }


    @ExceptionHandler(S3Exception.class)
    public ResponseEntity<ErrorDetails> handleS3(Exception ex, HttpServletRequest request) {
        return buildResponse(request, HttpStatus.BAD_GATEWAY,
                             "Failed to perform S3 operation. Errors: " + ex.getMessage());
    }

    // 500 Internal Server Error: fallback for any other exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleAll(Exception ex, HttpServletRequest request) {
        return buildResponse(request, HttpStatus.INTERNAL_SERVER_ERROR,
                             "An unexpected error occurred: " + ex.getMessage());
    }

    // Helper to build ErrorDetails and ResponseEntity
    private ResponseEntity<ErrorDetails> buildResponse(HttpServletRequest request, HttpStatus status, String message) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), status.value(), message, request.getRequestURI());
        return new ResponseEntity<>(errorDetails, status);
    }
}
package com.olivmaher.urlshortener.exception;

import com.olivmaher.urlshortener.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex){
        ErrorResponse error = new ErrorResponse(404, ex.getMessage());
        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    ResponseEntity<ErrorResponse> handleDuplicateResource(DuplicateResourceException ex){
        ErrorResponse error = new ErrorResponse(400, ex.getMessage());
        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(UnauthorizedException.class)
    ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex){
        ErrorResponse error = new ErrorResponse(403, ex.getMessage());
        return ResponseEntity.status(403).body(error);
    }
    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex){
        ErrorResponse error = new ErrorResponse(401, ex.getMessage());
        return ResponseEntity.status(401).body(error);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> handMethodArgumentNotValid(MethodArgumentNotValidException ex){
        String message = ex.getBindingResult().getFieldErrors().getFirst().getDefaultMessage();
        ErrorResponse error = new ErrorResponse(400, message);
        return ResponseEntity.status(400).body(error);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse(500, "Something went wrong");
        return ResponseEntity.status(500).body(error);
    }
}

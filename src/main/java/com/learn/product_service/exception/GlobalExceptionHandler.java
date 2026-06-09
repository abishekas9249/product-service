package com.learn.product_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handles @Valid failures
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex){

        Map<String,String> errors=new HashMap<>();
        ex.getBindingResult().getFieldErrors()
        .forEach(e->errors.put(e.getField(),e.getDefaultMessage()));

        return ResponseEntity.badRequest()
        .body(new ErrorResponse(400,"Validation Failed",errors, LocalDateTime.now()));
    }

    //Handles product Not Found
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFound(ProductNotFoundException ex){
        return ResponseEntity.status(404)
        .body(new ErrorResponse(404, ex.getMessage(), null,LocalDateTime.now()));
    }

    //Catch-all for unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex){
        return ResponseEntity.status(500)
        .body(new ErrorResponse(500,"Internal Server Error",null,LocalDateTime.now()));
    }

}

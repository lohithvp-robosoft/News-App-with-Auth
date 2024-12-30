package com.example.newsappwithauth.exception;

import com.example.newsappwithauth.dto.response.ResponseDTO;
import com.example.newsappwithauth.utils.ResponseUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Log4j2
public class ExceptionHandler {

    @Autowired
    ResponseUtil responseUtil;

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {EmailAlreadyExistsException.class})
    public <T> ResponseEntity<ResponseDTO<T>> handleEmptyEmployeeListException(EmailAlreadyExistsException exception) {
        return responseUtil.errorResponse(exception.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ResponseDTO<?>> handleGenericException(Exception ex) {
//        return responseUtil.errorResponse()
        return new ResponseEntity<>(new ResponseDTO<>(-1, 500, "Internal Server Error: " + ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {UserNotFound.class})
    public ResponseEntity<ResponseDTO<?>> handleUserNotFoundException(Exception ex) {
        return new ResponseEntity<>(new ResponseDTO<>(-1, 404, "User Not found", null), HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException  ex) {
        Map<String, String> errors = new HashMap<>();
        // Extract field errors
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return new ResponseEntity<>(new ResponseDTO<>(-1, 400, "Validation error occurred", errors), HttpStatus.BAD_REQUEST);
    }
}

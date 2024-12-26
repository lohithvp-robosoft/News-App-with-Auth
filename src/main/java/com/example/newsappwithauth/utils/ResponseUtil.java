package com.example.newsappwithauth.utils;

import com.example.newsappwithauth.dto.response.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseUtil {

    public <T> ResponseEntity<ResponseDTO<T>> successResponse(T responseData) {
        return new ResponseEntity<>(new ResponseDTO<>(0, 200, "Success", responseData), HttpStatus.OK);
    }

    public ResponseEntity<ResponseDTO<?>> errorResponse() {
        return new ResponseEntity<>(new ResponseDTO<>(-1, 404, "Fail", null), HttpStatus.NOT_FOUND);
    }

    public <T> ResponseEntity<ResponseDTO<T>> errorResponse(String message) {
        return new ResponseEntity<>(new ResponseDTO<>(-1, 404, message, null), HttpStatus.NOT_FOUND);
    }

    public <T> ResponseEntity<ResponseDTO<T>> errorResponse(String message, int statusCode) {
        return new ResponseEntity<>(new ResponseDTO<>(-1, statusCode, message, null), HttpStatusCode.valueOf(statusCode));
    }
}

package com.example.newsappwithauth.utils;

import com.example.newsappwithauth.dto.response.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseUtil {

    public <T> ResponseEntity<ResponseDTO<T>> successResponse(T responseData) {
        return new ResponseEntity<>(new ResponseDTO<>(0, 200, "Success", responseData), HttpStatus.OK);
    }

    public <T> ResponseEntity<ResponseDTO<?>> errorResponse(T responseData) {
        return new ResponseEntity<>(new ResponseDTO<>(-1, 404, "Fail", null), HttpStatus.NOT_FOUND);
    }
}

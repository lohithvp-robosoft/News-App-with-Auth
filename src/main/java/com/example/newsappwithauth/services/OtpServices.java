package com.example.newsappwithauth.services;

import com.example.newsappwithauth.dto.response.ResponseDTO;
import org.springframework.http.ResponseEntity;

public interface OtpServices {

    ResponseEntity<ResponseDTO<Object>> sendOtp(String email);

    String generateOtp();

//    void sendOtpEmail(String email, String otp);

    void storeOtpInRedis(String email, String otp);

    boolean validateOtp(String email, String otp);
}

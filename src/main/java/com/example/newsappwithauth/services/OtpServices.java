package com.example.newsappwithauth.services;

import com.example.newsappwithauth.dto.response.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface OtpServices {

    ResponseEntity<ResponseDTO<Object>> sendOtp(String email, String subject, String content);

    String generateOtp();

    void storeOtpInRedis(String email, String otp);

    boolean validateOtp(String email, String otp);
}

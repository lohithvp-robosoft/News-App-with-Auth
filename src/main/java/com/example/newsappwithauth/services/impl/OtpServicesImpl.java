package com.example.newsappwithauth.services.impl;

import com.example.newsappwithauth.dto.response.ResponseDTO;
import com.example.newsappwithauth.services.OtpServices;
import com.example.newsappwithauth.utils.EmailHandler;
import com.example.newsappwithauth.utils.ResponseUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
public class OtpServicesImpl implements OtpServices {

    private static final long OTP_EXPIRATION_TIME = 5 * 60 * 1000;

    @Autowired
    private EmailHandler emailHandler;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ResponseUtil responseUtil;

    @Override
    public ResponseEntity<ResponseDTO<Object>> sendOtp(String email, String subject, String content) {
        String otp = generateOtp();
        emailHandler.sendMail(email, subject, content + " " + otp);
        storeOtpInRedis(email, otp);
        return responseUtil.successResponse(null, "Successfully sent the email to " + email);
    }

    @Override
    public String generateOtp() {
        Random random = new Random();
        return String.valueOf(random.nextInt(100_000, 999_999));
    }

    @Override
    public void storeOtpInRedis(String email, String otp) {
        redisTemplate.opsForValue().set(email, otp, OTP_EXPIRATION_TIME, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean validateOtp(String email, String otp) {
        String storedOtp = redisTemplate.opsForValue().get(email);
        return storedOtp != null && storedOtp.equals(otp);
    }
}

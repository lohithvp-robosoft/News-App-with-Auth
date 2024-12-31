package com.example.newsappwithauth.controller;

import com.example.newsappwithauth.dto.request.ForgotPasswordRequest;
import com.example.newsappwithauth.dto.request.ResetPasswordRequest;
import com.example.newsappwithauth.dto.request.UserRequest;
import com.example.newsappwithauth.dto.response.NewsArticleResponse;
import com.example.newsappwithauth.dto.response.ResponseDTO;
import com.example.newsappwithauth.dto.response.UserLoginResponse;
import com.example.newsappwithauth.dto.response.UserRegisterResponse;
import com.example.newsappwithauth.services.OtpServices;
import com.example.newsappwithauth.services.UserServices;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserServices userServices;

    @Autowired
    private OtpServices otpServices;

    @Value("${mail.registration.subject}")
    private String mailRegSubject;

    @Value("${mail.registration.content}")
    private String mailRegContent;

    @Value("${mail.changePassword.subject}")
    private String mailChangePasswordSubject;

    @Value("${mail.changePassword.content}")
    private String mailChangePasswordContent;

    @PostMapping("/v1/send-reg-otp")
    public ResponseEntity<ResponseDTO<Object>> sendOtpForRegistration(@Valid @RequestBody UserRequest userRequest) {
        return otpServices.sendOtp(userRequest.getEmail(), mailRegSubject, mailRegContent);
    }

    @PostMapping("/v1/register")
    public ResponseEntity<ResponseDTO<UserRegisterResponse>> registerUser(@Valid @RequestBody UserRequest userRegister, @RequestParam String otp) {
        return userServices.registerUser(userRegister, otp);
    }

    @PostMapping("/v1/login")
    public ResponseEntity<ResponseDTO<UserLoginResponse>> loginUser(@Valid @RequestBody UserRequest userRequest) {
        return userServices.loginUser(userRequest);
    }

    @GetMapping("/v1/add-bookmark/{newsId}")
    public ResponseEntity<ResponseDTO<Object>> addABookMark(@PathVariable Long newsId, HttpServletRequest request) {
        return userServices.toggleBookMarkArticle(newsId, request);
    }

    @GetMapping("/v1/all-bookmark")
    public ResponseEntity<ResponseDTO<List<NewsArticleResponse>>> getAllBookMarkOfUser(HttpServletRequest request) {
        return userServices.getAllBookMarkOfUser(request);
    }

    @PostMapping("/v1/forgot-password")
    public ResponseEntity<ResponseDTO<Object>> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        return otpServices.sendOtp(forgotPasswordRequest.getEmail(), mailChangePasswordSubject, mailChangePasswordContent);
    }

    @PostMapping("/v1/reset-password")
    public ResponseEntity<ResponseDTO<Object>> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest, @RequestParam String otp) {
        return userServices.resetPassword(resetPasswordRequest, otp);
    }
}

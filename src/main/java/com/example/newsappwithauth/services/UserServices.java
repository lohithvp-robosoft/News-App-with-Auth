package com.example.newsappwithauth.services;

import com.example.newsappwithauth.dto.request.ResetPasswordRequest;
import com.example.newsappwithauth.dto.request.UserLoginRequest;
import com.example.newsappwithauth.dto.request.UserRequest;
import com.example.newsappwithauth.dto.response.NewsArticleResponse;
import com.example.newsappwithauth.dto.response.ResponseDTO;
import com.example.newsappwithauth.dto.response.UserLoginResponse;
import com.example.newsappwithauth.dto.response.UserRegisterResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserServices {
    ResponseEntity<ResponseDTO<UserRegisterResponse>> registerUser(UserRequest userRequest, String otp);

    ResponseEntity<ResponseDTO<UserLoginResponse>> loginUser(UserLoginRequest userRequest);

    ResponseEntity<ResponseDTO<Object>> toggleBookMarkArticle(Long newsId, HttpServletRequest request);

    ResponseEntity<ResponseDTO<List<NewsArticleResponse>>> getAllBookMarkOfUser(HttpServletRequest request);

    ResponseEntity<ResponseDTO<Object>> resetPassword(ResetPasswordRequest resetPasswordRequest, String otp);

}

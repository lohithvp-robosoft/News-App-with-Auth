package com.example.newsappwithauth.services;

import com.example.newsappwithauth.dto.request.UserRequest;
import com.example.newsappwithauth.dto.response.ResponseDTO;
import com.example.newsappwithauth.dto.response.UserLoginResponse;
import com.example.newsappwithauth.dto.response.UserRegisterResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserServices {
    ResponseEntity<ResponseDTO<UserRegisterResponse>> registerUser(UserRequest userRequest);

    ResponseEntity<ResponseDTO<UserLoginResponse>> loginUser(UserRequest userRequest);
}

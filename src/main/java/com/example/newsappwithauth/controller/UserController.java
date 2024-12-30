package com.example.newsappwithauth.controller;

import com.example.newsappwithauth.dto.request.UserRequest;
import com.example.newsappwithauth.dto.response.NewsArticleResponse;
import com.example.newsappwithauth.dto.response.ResponseDTO;
import com.example.newsappwithauth.dto.response.UserLoginResponse;
import com.example.newsappwithauth.dto.response.UserRegisterResponse;
import com.example.newsappwithauth.services.UserServices;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserServices userServices;

    @PostMapping("/v1/Register")
    public ResponseEntity<ResponseDTO<UserRegisterResponse>> registerUser(@Valid @RequestBody UserRequest userRegister) {
        return userServices.registerUser(userRegister);
    }

    @PostMapping("/v1/Login")
    public ResponseEntity<ResponseDTO<UserLoginResponse>> loginUser(@Valid @RequestBody UserRequest userRequest) {
        return userServices.loginUser(userRequest);
    }

    @GetMapping("/v1/bookmark/{newsId}")
    public ResponseEntity<ResponseDTO<Object>> addABookMark(@PathVariable Long newsId, HttpServletRequest request){
        return userServices.toggleBookMarkArticle(newsId, request);
    }

    @GetMapping("/v1/all-bookmark")
    public ResponseEntity<ResponseDTO<List<NewsArticleResponse>>> getAllBookMarkOfUser(HttpServletRequest request){
        return userServices.getAllBookMarkOfUser(request);
    }
}

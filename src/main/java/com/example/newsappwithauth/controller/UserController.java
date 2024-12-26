package com.example.newsappwithauth.controller;

import com.example.newsappwithauth.dto.request.UserRequest;
import com.example.newsappwithauth.dto.response.ResponseDTO;
import com.example.newsappwithauth.dto.response.UserRegisterResponse;
import com.example.newsappwithauth.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserServices userServices;

    @PostMapping("/Register")
    public ResponseEntity<ResponseDTO<UserRegisterResponse>> registerUser(@RequestBody UserRequest userRegister){
        return userServices.registerUser(userRegister);
    }
}

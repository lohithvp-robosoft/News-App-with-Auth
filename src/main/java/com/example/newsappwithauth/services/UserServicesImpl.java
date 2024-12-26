package com.example.newsappwithauth.services;

import com.example.newsappwithauth.dto.request.UserRequest;
import com.example.newsappwithauth.dto.response.ResponseDTO;
import com.example.newsappwithauth.dto.response.UserRegisterResponse;
import com.example.newsappwithauth.modal.User;
import com.example.newsappwithauth.repository.UserRepository;
import com.example.newsappwithauth.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
//import org.springframework.security.core.userdetails.User;

@Service
public class UserServicesImpl implements UserServices{

    @Autowired
    UserRepository userRepository;

    @Autowired
    ResponseUtil responseUtil;

    @Override
    public ResponseEntity<ResponseDTO<UserRegisterResponse>> registerUser(UserRequest userRequest) {
        User newUser = new User(userRequest);
        userRepository.save(newUser);
        UserRegisterResponse userRegisterResponse = new UserRegisterResponse(newUser);
        return responseUtil.successResponse(userRegisterResponse);
    }
}

package com.example.newsappwithauth.dto.response;

import com.example.newsappwithauth.modal.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserLoginResponse {
    private String email;
    private List<Role> roles;
    private  String token;
}

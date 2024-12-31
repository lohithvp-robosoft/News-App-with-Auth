package com.example.newsappwithauth.dto.response;

import com.example.newsappwithauth.modal.Role;
import com.example.newsappwithauth.modal.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserRegisterResponse {
    private String email;
    private String userName;
    private List<Role> roles;

    public UserRegisterResponse(User user) {
        this.email = user.getEmail();
        this.userName = user.getUsername();
        this.roles = user.getRoles();
    }
}

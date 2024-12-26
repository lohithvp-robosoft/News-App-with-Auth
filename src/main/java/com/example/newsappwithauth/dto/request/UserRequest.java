package com.example.newsappwithauth.dto.request;

import com.example.newsappwithauth.modal.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserRequest {
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @Size(min = 6, message = "Password must have at least 6 characters")
    @NotBlank(message = "Password cannot be blank")
    @Pattern(
            regexp = "^(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?])(?=.*\\d.*\\d).+$",
            message = "Password must include at least 2 numbers and 1 special character"
    )
    private String password;

    private List<Role> roles;

    public UserRequest(String email, String password, List<Role> roles) {
        this.email = email;
        this.password = password;
        if (roles.isEmpty()) {
            roles.add(Role.ROLE_USER);
        }
        this.roles = roles;
    }
}

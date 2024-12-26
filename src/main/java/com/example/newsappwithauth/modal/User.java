package com.example.newsappwithauth.modal;

import com.example.newsappwithauth.dto.request.UserRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Log4j2
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public User(UserRequest userRequest){
        this.email = userRequest.getEmail();
        this.password = userRequest.getPassword();

        if (userRequest.getRoles() == null || userRequest.getRoles().isEmpty()) {
            log.info("No roles provided, setting default role");
            this.roles.add(Role.USER);
        } else {
            this.roles = userRequest.getRoles();
        }

        log.info("Assigned roles: {}", this.roles);
    }

    public User(String email, String password, List<Role> roles) {
        this.email = email;
        this.password = password;
        if (roles == null ||roles.isEmpty()) {
            log.info("No roles provided, setting default role");
            this.roles.add(Role.USER);
        } else {
            this.roles = roles;
        }
    }
}

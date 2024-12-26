package com.example.newsappwithauth.services;

import com.example.newsappwithauth.dto.request.UserRequest;
import com.example.newsappwithauth.dto.response.ResponseDTO;
import com.example.newsappwithauth.dto.response.UserLoginResponse;
import com.example.newsappwithauth.dto.response.UserRegisterResponse;
import com.example.newsappwithauth.exception.EmailAlreadyExistsException;
import com.example.newsappwithauth.jwt.JwtUtils;
import com.example.newsappwithauth.modal.Role;
import com.example.newsappwithauth.modal.User;
import com.example.newsappwithauth.repository.UserRepository;
import com.example.newsappwithauth.utils.ResponseUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
public class UserServicesImpl implements UserServices{

    @Autowired
    UserRepository userRepository;

    @Autowired
    ResponseUtil responseUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public ResponseEntity<ResponseDTO<UserRegisterResponse>> registerUser(UserRequest userRequest) {
        log.info("Request Body User Role {}",userRequest.getRoles());
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email is already taken. Please choose another one.");
        }
        User newUser = new User(userRequest.getEmail(),passwordEncoder.encode(userRequest.getPassword()),userRequest.getRoles());
        log.info("User Role : {}",newUser.getRoles());
        userRepository.save(newUser);
        UserRegisterResponse userRegisterResponse = new UserRegisterResponse(newUser);
        return responseUtil.successResponse(userRegisterResponse);
    }

    @Override
    public ResponseEntity<ResponseDTO<UserLoginResponse>> loginUser(UserRequest userRequest) {
        Optional<User> optionalUser = userRepository.findByEmail(userRequest.getEmail());
//        log.info("User : {} ,{}, {}",optionalUser.get().getEmail(),optionalUser.get().getPassword(),optionalUser.get().getRoles());
        if(!optionalUser.isPresent()){
            return responseUtil.errorResponse("User Not Found");
        }

        User user =optionalUser.get();
        if(!passwordEncoder.matches(userRequest.getPassword(), user.getPassword())){
            return responseUtil.errorResponse("Invalid credentials",401);
        }

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles().stream().map(Role::name).toArray(String[]::new))
                .build();

        String token = jwtUtils.generateTokenFromUserDetails(userDetails, user.getId().toString());
        return responseUtil.successResponse(new UserLoginResponse(user.getEmail(),user.getRoles(),token));
    }
}

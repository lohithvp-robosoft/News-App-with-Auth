package com.example.newsappwithauth.services;

import com.example.newsappwithauth.dto.request.UserRequest;
import com.example.newsappwithauth.dto.response.NewsArticleResponse;
import com.example.newsappwithauth.dto.response.ResponseDTO;
import com.example.newsappwithauth.dto.response.UserLoginResponse;
import com.example.newsappwithauth.dto.response.UserRegisterResponse;
import com.example.newsappwithauth.exception.EmailAlreadyExistsException;
import com.example.newsappwithauth.exception.UserNotFound;
import com.example.newsappwithauth.jwt.JwtUtils;
import com.example.newsappwithauth.modal.Bookmark;
import com.example.newsappwithauth.modal.NewsArticle;
import com.example.newsappwithauth.modal.Role;
import com.example.newsappwithauth.modal.User;
import com.example.newsappwithauth.repository.BookmarkRepository;
import com.example.newsappwithauth.repository.NewsRepository;
import com.example.newsappwithauth.repository.UserRepository;
import com.example.newsappwithauth.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class UserServicesImpl implements UserServices {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ResponseUtil responseUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Override
    public ResponseEntity<ResponseDTO<UserRegisterResponse>> registerUser(UserRequest userRequest) {
        log.info("Request Body User Role {}", userRequest.getRoles());
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email is already taken. Please choose another one.");
        }
        User newUser = new User(userRequest.getEmail(), passwordEncoder.encode(userRequest.getPassword()), userRequest.getRoles());
        log.info("User Role : {}", newUser.getRoles());
        userRepository.save(newUser);
        UserRegisterResponse userRegisterResponse = new UserRegisterResponse(newUser);
        return responseUtil.successResponse(userRegisterResponse);
    }

    @Override
    public ResponseEntity<ResponseDTO<UserLoginResponse>> loginUser(UserRequest userRequest) {
        Optional<User> optionalUser = userRepository.findByEmail(userRequest.getEmail());
//        log.info("User : {} ,{}, {}",optionalUser.get().getEmail(),optionalUser.get().getPassword(),optionalUser.get().getRoles());
        if (!optionalUser.isPresent()) {
            return responseUtil.errorResponse("User Not Found");
        }

        User user = optionalUser.get();
        if (!passwordEncoder.matches(userRequest.getPassword(), user.getPassword())) {
            return responseUtil.errorResponse("Invalid credentials", 401);
        }

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles().stream().map(Role::name).toArray(String[]::new))
                .build();

        String token = jwtUtils.generateTokenFromUserDetails(userDetails, user.getId().toString());
        return responseUtil.successResponse(new UserLoginResponse(user.getEmail(), user.getRoles(), token));
    }

    @Override
    public ResponseEntity<ResponseDTO<Object>> toggleBookMarkArticle(Long newsId, HttpServletRequest request) {
        String token = jwtUtils.getJwtFromHeader(request);
        log.debug("Token :{}", token);
        Optional<User> optionalUser = userRepository.findById(Long.valueOf(jwtUtils.getUserIdFromJwtToken(token)));
        log.debug("User :", optionalUser.get());
        if (!jwtUtils.validateJwtToken(token)) {
            return responseUtil.errorResponse("Invalid JWT token");
        }
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Optional<Bookmark> optionalBookmark = bookmarkRepository.findByUser_IdAndNewsArticle_Id(user.getId(), newsId);
            if (optionalBookmark.isPresent()) {
                bookmarkRepository.delete(optionalBookmark.get());
                return responseUtil.successResponse(null, String.format("Removed News Article of id %d from the bookmark of user %d", newsId, user.getId()));
            }
            Optional<NewsArticle> optionalNewsArticle = newsRepository.findById(newsId);
            if (optionalNewsArticle.isPresent()) {
                NewsArticle newsArticle = optionalNewsArticle.get();
                Bookmark newBookmark = new Bookmark(user, newsArticle);
                bookmarkRepository.save(newBookmark);
                return responseUtil.successResponse(null, String.format("Added News Article of id %d to user %d", newsArticle.getId(), user.getId()));
            }
        }
        return responseUtil.errorResponse("Could not create the bookmark");
    }

    @Override
    public ResponseEntity<ResponseDTO<List<NewsArticleResponse>>> getAllBookMarkOfUser(HttpServletRequest request) {
        String token = jwtUtils.getJwtFromHeader(request);
        log.info("Token {}", token);
        Optional<User> optionalUser = userRepository.findById(Long.valueOf(jwtUtils.getUserIdFromJwtToken(token)));
        if (optionalUser.isPresent()) {
            long userId = optionalUser.get().getId();
            List<Bookmark> bookmarkList = bookmarkRepository.findByUser_Id(userId);
            if (bookmarkList.isEmpty()) {
                return responseUtil.successResponse(null, "There is no bookmark");
            } else {
                List<NewsArticleResponse> newsArticleList = new ArrayList<>();
                for (Bookmark bookmark : bookmarkList) {
                    NewsArticle newsArticle = newsRepository.findById(bookmark.getNewsArticle().getId()).get();
                    newsArticleList.add(new NewsArticleResponse(newsArticle));
                }
                return responseUtil.successResponse(newsArticleList);
            }
        }
        return responseUtil.errorResponse("Could not get the book mark");
    }

    @Override
    public ResponseEntity<ResponseDTO<Object>> resetPassword(String email, String newPassword) {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(email).orElseThrow(UserNotFound::new));
        User user = optionalUser.get();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
        return responseUtil.successResponse(null, "Successfully updated the password");
    }
}

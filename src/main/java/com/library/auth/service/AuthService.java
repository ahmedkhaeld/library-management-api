package com.library.auth.service;

import com.library.auth.dto.SignInRequest;
import com.library.auth.dto.SignUpRequest;
import com.library.security.JwtTokenProvider;
import com.library.user.Role;
import com.library.user.User;
import com.library.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class AuthService {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserService userService, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public Map<String, String> SignUp(SignUpRequest signupRequest) {
        User user = userService.createUser(
                signupRequest.getUsername(),
                passwordEncoder.encode(signupRequest.getPassword()),
                Role.valueOf(signupRequest.getRole())
        );

        String token = jwtTokenProvider.generateToken(user);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return response;
    }

    public Map<String, String> SignIn(SignInRequest signinRequest) {
        User user;
        try {
            user = userService.getUserByUsername(signinRequest.getUsername());
        } catch (EntityNotFoundException e) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        if (passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
            String token = jwtTokenProvider.generateToken(user);

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return response;
        } else {
            throw new IllegalArgumentException("Invalid username or password");
        }
    }
}

package com.olivmaher.urlshortener.service;

import com.olivmaher.urlshortener.dto.AuthResponse;
import com.olivmaher.urlshortener.dto.LoginRequest;
import com.olivmaher.urlshortener.dto.RegisterRequest;
import com.olivmaher.urlshortener.entity.User;
import com.olivmaher.urlshortener.exception.DuplicateResourceException;
import com.olivmaher.urlshortener.exception.ResourceNotFoundException;
import com.olivmaher.urlshortener.repository.UserRepository;
import com.olivmaher.urlshortener.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    public AuthService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(RegisterRequest req){
        if(userRepository.findByEmail(req.getEmail()).isPresent()){
            throw new DuplicateResourceException("User Already Exists");
        }
        User user = new User(req.getEmail(), bCryptPasswordEncoder.encode(req.getPassword()));
        userRepository.save(user);
        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest req){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );
        User user = userRepository.findByEmail(req.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        String token = jwtUtil.generateToken(user);

        return new AuthResponse(token);
    }
}

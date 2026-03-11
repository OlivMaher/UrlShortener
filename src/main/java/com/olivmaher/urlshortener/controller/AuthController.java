package com.olivmaher.urlshortener.controller;


import com.olivmaher.urlshortener.dto.AuthResponse;
import com.olivmaher.urlshortener.dto.LoginRequest;
import com.olivmaher.urlshortener.dto.RegisterRequest;
import com.olivmaher.urlshortener.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req){
        AuthResponse res = authService.register(req);
        return ResponseEntity.status(201).body(res);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req){
        AuthResponse res = authService.login(req);
        return ResponseEntity.ok(res);
    }
}

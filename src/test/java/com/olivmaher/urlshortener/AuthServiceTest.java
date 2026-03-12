package com.olivmaher.urlshortener;

import com.olivmaher.urlshortener.dto.AuthResponse;
import com.olivmaher.urlshortener.dto.RegisterRequest;
import com.olivmaher.urlshortener.entity.User;
import com.olivmaher.urlshortener.exception.DuplicateResourceException;
import com.olivmaher.urlshortener.repository.UserRepository;
import com.olivmaher.urlshortener.service.AuthService;
import com.olivmaher.urlshortener.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private org.springframework.security.authentication.AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_shouldThrow_whenEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@email.com");
        request.setPassword("password123");

        when(userRepository.findByEmail("test@email.com"))
                .thenReturn(Optional.of(new User()));

        assertThrows(DuplicateResourceException.class, () -> {
            authService.register(request);
        });
    }

    @Test
    void register_shouldSaveUserAndReturnToken_whenEmailIsNew() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@email.com");
        request.setPassword("password123");

        when(userRepository.findByEmail("test@email.com"))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123"))
                .thenReturn("hashedPassword");
        when(jwtUtil.generateToken(any(User.class)))
                .thenReturn("mockToken");

        AuthResponse response = authService.register(request);

        verify(userRepository).save(any(User.class));
        assertEquals("mockToken", response.getToken());
    }
}
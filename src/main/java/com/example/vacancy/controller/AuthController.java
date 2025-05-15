package com.example.vacancy.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.vacancy.config.JwtUtils;
import com.example.vacancy.dto.AuthRequest;
import com.example.vacancy.dto.AuthResponse;
import com.example.vacancy.dto.MessageResponse;
import com.example.vacancy.dto.RegisterRequest;
import com.example.vacancy.model.User;
import com.example.vacancy.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PasswordEncoder encoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (userRepo.existsByUsername(req.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        try {
            User user = new User();
            user.setUsername(req.getUsername());
            user.setPassword(encoder.encode(req.getPassword()));
            user.setRole(req.getRole());

            User savedUser = userRepo.save(user);

            logger.info("User registered successfully: {}", savedUser.getUsername());

            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        } catch (Exception e) {
            logger.error("Registration failed: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        try {
            // Print debug info
            System.out.println("Login attempt for user: " + req.getUsername());

            // This will throw an exception if authentication fails
            authManager.authenticate(new UsernamePasswordAuthenticationToken(
                    req.getUsername(), req.getPassword()));

            System.out.println("Authentication successful");

            // Try/catch this part to see if user exists
            UserDetails userDetails = null;
            try {
                userDetails = userRepo.findByUsername(req.getUsername())
                        .map(u -> {
                            System.out.println("User found, role: " + u.getRole().name());
                            return new org.springframework.security.core.userdetails.User(
                                    u.getUsername(),
                                    u.getPassword(),
                                    List.of(new SimpleGrantedAuthority("ROLE_" + u.getRole().name())));
                        })
                        .orElseThrow();
            } catch (Exception e) {
                System.out.println("Error finding or mapping user: " + e.getMessage());
                throw e;
            }

            String token = jwtUtils.generateToken(userDetails);
            System.out.println("Token generated successfully");

            return ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
            throw e;
        }
    }
}

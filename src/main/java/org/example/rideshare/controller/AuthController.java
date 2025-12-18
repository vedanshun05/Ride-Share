package org.example.rideshare.controller;

import jakarta.validation.Valid;
import org.example.rideshare.dto.AuthRequest;
import org.example.rideshare.dto.RegisterRequest;
import org.example.rideshare.model.User;
import org.example.rideshare.service.UserService;
import org.example.rideshare.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "User registration and login")
public class AuthController {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager manager;
    private final UserService service;
    private final JwtUtil jwt;

    public AuthController(AuthenticationManager m, UserService s, JwtUtil j) {
        this.manager = m;
        this.service = s;
        this.jwt = j;
    }

    @PostMapping("/register")
    public User register(@RequestBody @Valid RegisterRequest req) {
        logger.info("Register request for user: {}", req.getUsername());
        return service.register(req.getUsername(), req.getPassword(), req.getRole());
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody AuthRequest req) {
        logger.info("Login request for user: {}", req.getUsername());
        try {
            Authentication auth = manager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
            logger.info("Authentication successful for user: {}", req.getUsername());
            String token = jwt.generateToken(req.getUsername(),
                    service.loadUserByUsername(req.getUsername()).getAuthorities().iterator().next().getAuthority());
            return Map.of("token", token);
        } catch (Exception e) {
            logger.error("Authentication failed for user: {}", req.getUsername(), e);
            throw e;
        }
    }
}

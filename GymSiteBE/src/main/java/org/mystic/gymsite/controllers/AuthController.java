package org.mystic.gymsite.controllers;

import lombok.RequiredArgsConstructor;

import org.mystic.gymsite.dtos.LoginRequest;
import org.mystic.gymsite.dtos.RegisterRequest;
import org.mystic.gymsite.dtos.AuthResponse;
import org.mystic.gymsite.services.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}

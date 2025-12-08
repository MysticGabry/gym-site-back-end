package org.mystic.gymsite.controllers;

import lombok.RequiredArgsConstructor;
import org.mystic.gymsite.dtos.LoginItem;
import org.mystic.gymsite.dtos.RegisterItem;
import org.mystic.gymsite.dtos.AuthItem;
import org.mystic.gymsite.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthItem> register(@RequestBody RegisterItem request) {
        AuthItem response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginItem request) {
        AuthItem response = authService.login(request);
        if (response == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Credenziali errate");
        }
        return ResponseEntity.ok(response);
    }
}

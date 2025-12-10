package org.mystic.gymsite.controllers;

import lombok.RequiredArgsConstructor;
import org.mystic.gymsite.entities.User;
import org.mystic.gymsite.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(Authentication auth) {
        User user = userService.getByUsername(auth.getName());
        return ResponseEntity.ok(Map.of(
                "username", user.getUsername(),
                "email", user.getEmail(),
                "role", user.getRole()
        ));
    }

    @PutMapping("/email")
    public ResponseEntity<?> updateEmail(
            Authentication auth,
            @RequestBody Map<String, String> request) {
        String newEmail = request.get("email");

        if (newEmail == null || newEmail.isBlank()) {
            return ResponseEntity.badRequest().body("Email non valida");
        }

        try {
            User updated = userService.updateEmail(auth.getName(), newEmail);

            return ResponseEntity.ok(Map.of(
                    "message", "Email aggiornata con successo",
                    "email", updated.getEmail()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "Errore aggiornamento email",
                    "error", e.getMessage()
            ));
        }
    }

    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(
            Authentication auth,
            @RequestBody Map<String, String> request) {
        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");

        if (oldPassword == null || newPassword == null ||
                oldPassword.isBlank() || newPassword.isBlank()) {
            return ResponseEntity.badRequest().body("Password non valide");
        }
        try {
            userService.updatePassword(auth.getName(), oldPassword, newPassword);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "Errore nel cambio password",
                    "error", e.getMessage()
            ));
        }

        return ResponseEntity.ok(Map.of(
                "message", "Password aggiornata con successo"
        ));
    }
}

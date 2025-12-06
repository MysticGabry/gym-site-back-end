package org.mystic.gymsite.services;

import lombok.RequiredArgsConstructor;
import org.mystic.gymsite.dtos.AuthResponse;
import org.mystic.gymsite.dtos.LoginRequest;
import org.mystic.gymsite.dtos.RegisterRequest;
import org.mystic.gymsite.entities.Role;
import org.mystic.gymsite.entities.User;
import org.mystic.gymsite.repositories.UserRepository;
import org.mystic.gymsite.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority; // Import Manca - Aggiunto
import org.springframework.security.core.context.SecurityContextHolder; // Import Manca - Aggiunto
import org.springframework.security.core.userdetails.UserDetails; // Import Manca - Aggiunto
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already in use");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return new AuthResponse(token, "ROLE_USER");
    }

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Email non trovata"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Password errata");
        }

        String token = jwtService.generateToken(user);

        return new AuthResponse(token, user.getRole().toString());
    }

}
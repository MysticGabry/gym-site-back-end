package org.mystic.gymsite.services;

import lombok.RequiredArgsConstructor;
import org.mystic.gymsite.dtos.AuthDTO;
import org.mystic.gymsite.dtos.LoginDTO;
import org.mystic.gymsite.dtos.RegisterDTO;
import org.mystic.gymsite.utils.Role;
import org.mystic.gymsite.entities.User;
import org.mystic.gymsite.repositories.UserRepository;
import org.mystic.gymsite.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthDTO register(RegisterDTO request) {

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return new AuthDTO(token, user.getUsername(), user.getRole().name());
    }

    public AuthDTO login(LoginDTO request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElse(null);
        if (user == null) {
            user = userRepository.findByEmail(request.getUsername())
                    .orElse(null);
        }
        if (user == null) {
            return null;
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return null;
        }
        if (user.getRole() == null) {
            if ("admin".equalsIgnoreCase(user.getUsername())) {
                user.setRole(Role.ADMIN);
            } else {
                user.setRole(Role.USER);
            }
            userRepository.save(user);
        }
        String token = jwtService.generateToken(user);
        return new AuthDTO(
                token,
                user.getUsername(),
                user.getRole().name()
        );
    }
}
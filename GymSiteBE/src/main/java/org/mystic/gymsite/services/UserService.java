package org.mystic.gymsite.services;

import lombok.RequiredArgsConstructor;
import org.mystic.gymsite.entities.User;
import org.mystic.gymsite.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
    }

    public User updateEmail(String username, String newEmail) {
        User user = getByUsername(username);
        user.setEmail(newEmail);
        return userRepository.save(user);
    }

    public void updatePassword(String username, String oldPassword, String newPassword) {
        User user = getByUsername(username);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Vecchia password errata");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}

package org.mystic.gymsite.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class AuthResponse {
    private String token;
    @Getter
    @Setter
    private String role;

    public AuthResponse(String token, String role) {
        this.token = token;
        this.role = role;
    }


}
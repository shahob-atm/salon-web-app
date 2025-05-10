package com.sh32bit.service.impl;

import com.sh32bit.enums.Role;
import com.sh32bit.model.User;
import com.sh32bit.repository.UserRepository;
import com.sh32bit.request.LoginRequest;
import com.sh32bit.request.RegisterRequest;
import com.sh32bit.response.AuthResponse;
import com.sh32bit.response.TokenResponse;
import com.sh32bit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final KeycloakUserService keycloakUserService;

    @Override
    public AuthResponse register(RegisterRequest req) throws Exception {
        keycloakUserService.createUser(req);

        User user = User.builder()
                .email(req.email())
                .username(req.username())
                .firstName(req.firstName())
                .lastName(req.lastName())
                .phoneNumber(req.phoneNumber())
                .role(Role.CUSTOMER)
                .build();

        userRepository.save(user);

        TokenResponse tokenResponse = keycloakUserService.getAdminAccessToken(
                req.username(),
                req.password(),
                "password",
                null
        );

        return AuthResponse.builder()
                .accessToken(tokenResponse.getAccessToken())
                .refreshToken(tokenResponse.getRefreshToken())
                .title("Welcome to " + user.getFirstName() + " " + user.getLastName())
                .message("Registered Successfully")
                .role(user.getRole())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest req) throws Exception {
        TokenResponse tokenResponse = keycloakUserService.getAdminAccessToken(
                req.username(),
                req.password(),
                "password",
                null
        );

        return AuthResponse.builder()
                .accessToken(tokenResponse.getAccessToken())
                .refreshToken(tokenResponse.getRefreshToken())
                .title("Welcome to " + req.email())
                .message("Login Successfully")
                .role(null)
                .build();
    }

    @Override
    public AuthResponse refreshToken(String token) throws Exception {
        TokenResponse tokenResponse = keycloakUserService.getAdminAccessToken(
                null,
                null,
                "refresh_token",
                token
        );

        return AuthResponse.builder()
                .accessToken(tokenResponse.getAccessToken())
                .refreshToken(tokenResponse.getRefreshToken())
                .title("Get Access Token with Refresh Token")
                .message("Refresh Successfully")
                .build();
    }
}

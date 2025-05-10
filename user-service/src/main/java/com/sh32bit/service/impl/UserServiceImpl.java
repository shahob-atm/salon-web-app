package com.sh32bit.service.impl;

import com.sh32bit.keycloak.KeycloakUserInfo;
import com.sh32bit.model.User;
import com.sh32bit.repository.UserRepository;
import com.sh32bit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final KeycloakUserService keycloakUserService;

    @Override
    public User getUserByJwtToken(String jwt) throws Exception {
        KeycloakUserInfo userInfo = keycloakUserService.fetchUserProfileByJwt(jwt);

        return userRepository.findByEmail(userInfo.email());
    }

    @Override
    public User getUserById(Long id) throws Exception {
        return userRepository.findById(id)
                .orElseThrow(() -> new Exception("User not found"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}

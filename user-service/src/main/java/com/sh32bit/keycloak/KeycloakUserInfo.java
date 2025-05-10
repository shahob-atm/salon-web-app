package com.sh32bit.keycloak;

public record KeycloakUserInfo(
        String sub,
        String email,
        String preferred_username
) {
}

package com.sh32bit.keycloak;

public record KeycloakUser(
        String id,
        String email,
        String firstName,
        String lastName,
        boolean enabled
) {
}

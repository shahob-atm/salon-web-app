package com.sh32bit.keycloak;

import java.util.List;

public record UserRequest(
        String username,
        boolean enabled,
        String firstName,
        String lastName,
        String email,
        List<Credential> credentials,
        List<String> realmRoles
) {
}

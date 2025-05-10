package com.sh32bit.keycloak;

import java.util.Map;

public record KeycloakRole(
        String id,
        String name,
        String description,
        boolean composite,
        boolean clientRole,
        String containerId,
        Map<String, Object> attributes
) {
}

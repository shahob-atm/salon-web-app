package com.sh32bit.keycloak;

public record Credential(
        String type,
        String value,
        boolean temporary
) {
}

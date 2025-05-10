package com.sh32bit.request;

public record RegisterRequest(
        String firstName,
        String lastName,
        String email,
        String username,
        String password,
        String phoneNumber
) {
}

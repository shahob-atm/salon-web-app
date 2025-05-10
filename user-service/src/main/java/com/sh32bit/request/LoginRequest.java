package com.sh32bit.request;

public record LoginRequest(
        String email,
        String password,
        String username
) {
}

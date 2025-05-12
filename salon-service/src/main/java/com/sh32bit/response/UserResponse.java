package com.sh32bit.response;

import com.sh32bit.enums.Role;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String username,
        Role role
) {
}

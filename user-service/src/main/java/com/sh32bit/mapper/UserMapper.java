package com.sh32bit.mapper;

import com.sh32bit.model.User;
import com.sh32bit.response.UserResponse;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getUsername(),
                user.getRole()
        );
    }
}

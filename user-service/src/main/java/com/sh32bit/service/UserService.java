package com.sh32bit.service;

import com.sh32bit.model.User;

import java.util.List;

public interface UserService {
    User getUserByJwtToken(String jwt) throws Exception;

    User getUserById(Long id) throws Exception;

    List<User> getAllUsers();
}

package com.sh32bit.controller;

import com.sh32bit.mapper.UserMapper;
import com.sh32bit.model.User;
import com.sh32bit.response.UserResponse;
import com.sh32bit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getUserByJwtToken(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.getUserByJwtToken(jwt);
        UserResponse res = userMapper.mapToResponse(user);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long id) throws Exception {
        User user = userService.getUserById(id);
        UserResponse res = userMapper.mapToResponse(user);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() throws Exception {
        List<User> users = userService.getAllUsers();
        List<UserResponse> res = users.stream().map(userMapper::mapToResponse).toList();

        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}

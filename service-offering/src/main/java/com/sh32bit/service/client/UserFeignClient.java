package com.sh32bit.service.client;

import com.sh32bit.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("USER-SERVICE")
public interface UserFeignClient {
    @GetMapping("/api/users/profile")
    ResponseEntity<UserResponse> getUserByJwtToken(
            @RequestHeader("Authorization") String jwt
    ) throws Exception;
}

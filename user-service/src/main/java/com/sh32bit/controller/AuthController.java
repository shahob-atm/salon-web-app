package com.sh32bit.controller;

import com.sh32bit.request.LoginRequest;
import com.sh32bit.request.RegisterRequest;
import com.sh32bit.response.ApiResponseBody;
import com.sh32bit.response.AuthResponse;
import com.sh32bit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseBody<AuthResponse>> registerHandler(
            @RequestBody RegisterRequest req
    ) throws Exception {
        AuthResponse res = authService.register(req);
        ApiResponseBody<AuthResponse> responseBody = ApiResponseBody.ok(res.getMessage(), res);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseBody<AuthResponse>> loginHandler(
            @RequestBody LoginRequest req
    ) throws Exception {
        AuthResponse res = authService.login(req);
        ApiResponseBody<AuthResponse> responseBody = ApiResponseBody.ok(res.getMessage(), res);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/refresh-token/{token}")
    public ResponseEntity<ApiResponseBody<AuthResponse>> refreshTokenHandler(
            @PathVariable("token") String token
    ) throws Exception {
        AuthResponse res = authService.refreshToken(token);
        ApiResponseBody<AuthResponse> responseBody = ApiResponseBody.ok(res.getMessage(), res);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}

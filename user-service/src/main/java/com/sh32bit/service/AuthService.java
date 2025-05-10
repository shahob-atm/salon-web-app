package com.sh32bit.service;

import com.sh32bit.request.LoginRequest;
import com.sh32bit.request.RegisterRequest;
import com.sh32bit.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest req) throws Exception;

    AuthResponse login(LoginRequest req) throws Exception;

    AuthResponse refreshToken(String token) throws Exception;
}

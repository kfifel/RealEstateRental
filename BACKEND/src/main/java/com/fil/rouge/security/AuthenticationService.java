package com.fil.rouge.security;

import com.fil.rouge.domain.AppUser;
import com.fil.rouge.security.auth.JwtAuthenticationResponse;
import com.fil.rouge.utils.ValidationException;
import com.fil.rouge.web.dto.request.SignInRequest;
import com.fil.rouge.web.dto.request.SignUpRequest;

public interface AuthenticationService {

    JwtAuthenticationResponse signup(SignUpRequest request) throws ValidationException;

    JwtAuthenticationResponse signin(SignInRequest request);

    JwtAuthenticationResponse refreshToken(String refreshToken) throws ValidationException;

    AppUser me();
}

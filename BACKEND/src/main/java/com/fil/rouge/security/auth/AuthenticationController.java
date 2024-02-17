package com.fil.rouge.security.auth;

import com.fil.rouge.domain.AppUser;
import com.fil.rouge.web.exception.UnauthorizedException;
import com.fil.rouge.security.AuthenticationService;
import com.fil.rouge.utils.ValidationException;
import com.fil.rouge.web.dto.request.SignInRequest;
import com.fil.rouge.web.dto.request.SignUpRequest;
import com.fil.rouge.web.dto.response.UserResponseDto;
import com.fil.rouge.web.mapper.UserDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody @Valid SignInRequest credential) {
        JwtAuthenticationResponse result = authenticationService.signin(credential);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/register")
    public ResponseEntity<JwtAuthenticationResponse> signup(@RequestBody @Valid SignUpRequest register) throws ValidationException {
        JwtAuthenticationResponse result = authenticationService.signup(register);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> me() {
        AppUser result = authenticationService.me();
        return ResponseEntity.ok(UserDtoMapper.toDto(result));
    }

    @GetMapping("/token/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refreshToken(HttpServletRequest request) throws ValidationException {
        String authorization = request.getHeader("Authorization");
        if(authorization == null || !authorization.startsWith("Bearer ")) {
            throw new UnauthorizedException("Refresh token is missing");
        }
        String token = authorization.substring(7);
        JwtAuthenticationResponse result = authenticationService.refreshToken(token);
        return ResponseEntity.ok(result);
    }
}

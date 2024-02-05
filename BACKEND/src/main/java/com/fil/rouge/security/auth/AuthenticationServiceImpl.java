package com.fil.rouge.security.auth;

import com.fil.rouge.domain.Role;
import com.fil.rouge.domain.AppUser;
import com.fil.rouge.web.exception.UnauthorizedException;
import com.fil.rouge.repository.UserRepository;
import com.fil.rouge.security.AuthenticationService;
import com.fil.rouge.security.jwt.JwtService;
import com.fil.rouge.security.jwt.TokenType;
import com.fil.rouge.service.RoleService;
import com.fil.rouge.service.UserService;
import com.fil.rouge.utils.CustomError;
import com.fil.rouge.utils.ValidationException;
import com.fil.rouge.web.dto.request.SignInRequest;
import com.fil.rouge.web.dto.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.fil.rouge.security.AuthoritiesConstants.ROLE_TENANT;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) throws ValidationException {
        Role roleUser;
        Optional<Role> byName = roleService.findByName(ROLE_TENANT);
        if(byName.isEmpty())
            roleUser = roleService.save(Role.builder().name(ROLE_TENANT).build());
        else
            roleUser = byName.get();
        AppUser user = AppUser.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .roles(List.of(roleUser))
        .build();
        userService.save(user);
        String accessToken = jwtService.generateToken(user, TokenType.ACCESS_TOKEN);
        String refreshToken = jwtService.generateToken(user, TokenType.REFRESH_TOKEN);
        return JwtAuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public JwtAuthenticationResponse signin(SignInRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var accessToken = jwtService.generateToken(user, TokenType.ACCESS_TOKEN);
        var refreshToken = jwtService.generateToken(user, TokenType.REFRESH_TOKEN);
        return  JwtAuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public JwtAuthenticationResponse refreshToken(String  refreshToken) throws ValidationException {
        if(jwtService.isTokenValid(refreshToken, TokenType.REFRESH_TOKEN)) {
            String username = jwtService.extractUserName(refreshToken);
            var user = userRepository.findByEmail(username).orElseThrow(() -> new ValidationException(new CustomError("email", "User not found")));
            var accessToken = jwtService.generateToken(user, TokenType.ACCESS_TOKEN);
            return JwtAuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
        throw new UnauthorizedException("Refresh token is invalid");
    }

    @Override
    public AppUser me() {
        return userService.getCurrentUser();
    }
}

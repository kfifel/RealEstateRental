package com.fil.rouge.security.auth;

import com.fil.rouge.domain.AppUser;
import com.fil.rouge.security.PasswordResetTokenService;
import com.fil.rouge.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/password-reset")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetTokenService tokenService;
    private final UserService userService;

    @PostMapping("/request")
    public ResponseEntity<Void> requestPasswordReset(@RequestParam String email) {
        AppUser user = userService.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        tokenService.createToken(user);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/validate")
    public ResponseEntity<Void> validateToken(@RequestParam String token) {
        if (tokenService.validateToken(token)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        if (!tokenService.validateToken(token))
            throw new UsernameNotFoundException("Token invalide");

        tokenService.resetPassword(token, newPassword);
        return ResponseEntity.ok().build();
    }
}
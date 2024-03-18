package com.fil.rouge.security;

import com.fil.rouge.domain.AppUser;

public interface PasswordResetTokenService {

    void createToken(AppUser user);
    boolean validateToken(String token);
    void resetPassword(String token, String newPassword);
}

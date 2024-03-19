package com.fil.rouge.security.auth;

import com.fil.rouge.domain.AppUser;
import com.fil.rouge.domain.PasswordResetToken;
import com.fil.rouge.repository.PasswordResetTokenRepository;
import com.fil.rouge.repository.UserRepository;
import com.fil.rouge.security.PasswordResetTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Override
    public void createToken(AppUser user) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .user(user)
                .token(token)
                .expiryDate(LocalDateTime.now().plusMinutes(10))
                .build();
        PasswordResetToken passwordResetToken = tokenRepository.save(resetToken);

        sendPasswordResetEmail(user.getEmail(), passwordResetToken.getToken());
    }

    private void sendPasswordResetEmail(String recipientEmail, String token) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(recipientEmail);
            helper.setSubject("Password Reset Off Your Account KRIHNA");
            // HTML content for the email body
            String htmlContent = "<html><body>"
                    + "<h2 style=\"color: #03A8B5;\">Reset Your Password</h2>"
                    + "<p>To reset your password, click the following link:</p>"
                    + "<p><a href=\"http://localhost:4200/account/reset-password?token=" + token + "\" style=\""
                    + "background-color: #03A8B5; color: #ffffff; text-decoration: none; padding: 10px 20px; border-radius: 5px;\">Reset Password</a></p>"
                    + "</body></html>";

            helper.setText(htmlContent, true); // Set to true for HTML content
            mailSender.send(message);
        } catch (MessagingException e) {
            // Handle exception
            e.printStackTrace();
            throw new RuntimeException("Error has been occurred while sending the reset password contact support!");
        }
    }

    @Override
    public boolean validateToken(String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token).orElse(null);
        return resetToken != null && !resetToken.isExpired();
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new UsernameNotFoundException("Token invalide"));
        AppUser user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}

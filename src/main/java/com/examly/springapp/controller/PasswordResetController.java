package com.examly.springapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import com.examly.springapp.service.PasswordResetTokenService;

@RestController
@RequestMapping("/api/password-reset")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetTokenService passwordResetTokenService;

    // Endpoint to create a password reset token for a user by userId
    @PostMapping("/request")
    public ResponseEntity<String> requestPasswordReset(@RequestParam Long userId) {
        passwordResetTokenService.createResetToken(userId);
        return ResponseEntity.ok("Password reset token created and sent to user's email.");
    }

    // NEW: Endpoint to create a password reset token by email
    @PostMapping("/request-by-email")
    public ResponseEntity<String> requestPasswordResetByEmail(@RequestParam String email) {
        passwordResetTokenService.createResetTokenByEmail(email);
        return ResponseEntity.ok("Password reset token created and sent to user's email.");
    }

    // Endpoint to reset password using token
    @PostMapping("/confirm")
    public ResponseEntity<String> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {

        passwordResetTokenService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Password reset successful.");
    }
}

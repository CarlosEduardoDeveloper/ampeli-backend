package com.api.ampeli.controller;

import com.api.ampeli.model.dto.AuthResponse;
import com.api.ampeli.model.dto.LoginRequest;
import com.api.ampeli.model.dto.RegisterRequest;
import com.api.ampeli.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);

        if (response.getUserId() != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);

        if (response.getUserId() != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping("/status/{userId}")
    public ResponseEntity<AuthResponse> getUserStatus(@PathVariable Long userId) {
        AuthResponse response = authService.checkUserStatus(userId);

        if (response.getUserId() != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/check-email/{email}")
    public ResponseEntity<Boolean> checkEmailAvailability(@PathVariable String email) {
        boolean isAvailable = authService.isEmailAvailable(email);
        return ResponseEntity.ok(isAvailable);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        boolean success = authService.changePassword(
            request.getUserId(),
            request.getCurrentPassword(),
            request.getNewPassword()
        );

        if (success) {
            return ResponseEntity.ok().body("Password changed successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to change password");
        }
    }

    // Inner class for change password request
    public static class ChangePasswordRequest {
        private Long userId;
        private String currentPassword;
        private String newPassword;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getCurrentPassword() { return currentPassword; }
        public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }
}

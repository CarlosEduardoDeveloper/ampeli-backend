package com.api.ampeli.services;

import com.api.ampeli.model.User;
import com.api.ampeli.model.Member;
import com.api.ampeli.model.dto.AuthResponse;
import com.api.ampeli.model.dto.LoginRequest;
import com.api.ampeli.model.dto.RegisterRequest;
import com.api.ampeli.repository.UserRepository;
import com.api.ampeli.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthResponse register(RegisterRequest request) {
        // Validate password confirmation
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return new AuthResponse(null, null, null, null, false, null, "Passwords do not match");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse(null, null, null, null, false, null, "Email already registered");
        }

        // Validate password strength
        String passwordValidation = validatePassword(request.getPassword());
        if (passwordValidation != null) {
            return new AuthResponse(null, null, null, null, false, null, passwordValidation);
        }

        try {
            // Create new user
            User user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPhone(request.getPhone());
            user.setPassword(passwordEncoder.encode(request.getPassword()));

            User savedUser = userRepository.save(user);

            return new AuthResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getPhone(),
                false, // New user hasn't completed member form yet
                null,
                "User registered successfully"
            );

        } catch (Exception e) {
            return new AuthResponse(null, null, null, null, false, null, "Registration failed: " + e.getMessage());
        }
    }

    public AuthResponse login(LoginRequest request) {
        try {
            // Find user by email
            Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
            if (userOpt.isEmpty()) {
                return new AuthResponse(null, null, null, null, false, null, "Invalid email or password");
            }

            User user = userOpt.get();

            // Verify password
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return new AuthResponse(null, null, null, null, false, null, "Invalid email or password");
            }

            // Check if user has completed member form
            Optional<Member> memberOpt = memberRepository.findByUserId(user.getId());
            boolean hasCompletedForm = memberOpt.isPresent();
            Long memberId = hasCompletedForm ? memberOpt.get().getId() : null;

            return new AuthResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                hasCompletedForm,
                memberId,
                "Login successful"
            );

        } catch (Exception e) {
            return new AuthResponse(null, null, null, null, false, null, "Login failed: " + e.getMessage());
        }
    }

    public AuthResponse checkUserStatus(Long userId) {
        try {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                return new AuthResponse(null, null, null, null, false, null, "User not found");
            }

            User user = userOpt.get();

            // Check if user has completed member form
            Optional<Member> memberOpt = memberRepository.findByUserId(userId);
            boolean hasCompletedForm = memberOpt.isPresent();
            Long memberId = hasCompletedForm ? memberOpt.get().getId() : null;

            return new AuthResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                hasCompletedForm,
                memberId,
                "User status retrieved successfully"
            );

        } catch (Exception e) {
            return new AuthResponse(null, null, null, null, false, null, "Failed to retrieve user status: " + e.getMessage());
        }
    }

    public boolean changePassword(Long userId, String currentPassword, String newPassword) {
        try {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                return false;
            }

            User user = userOpt.get();

            // Verify current password
            if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                return false;
            }

            // Validate new password strength
            if (validatePassword(newPassword) != null) {
                return false;
            }

            // Update password
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    private String validatePassword(String password) {
        if (password.length() < 6) {
            return "Password must be at least 6 characters long";
        }

        if (password.length() > 100) {
            return "Password must not exceed 100 characters";
        }

        // Check for at least one letter and one number
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasNumber = password.matches(".*[0-9].*");

        if (!hasLetter || !hasNumber) {
            return "Password must contain at least one letter and one number";
        }

        return null; // Password is valid
    }

    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }
}

package com.api.ampeli.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private Long userId;
    private String name;
    private String email;
    private String phone;
    private boolean hasCompletedMemberForm;
    private Long memberId; // null if form not completed
    private String message;

    public AuthResponse(Long userId, String name, String email, String phone, boolean hasCompletedMemberForm, Long memberId) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.hasCompletedMemberForm = hasCompletedMemberForm;
        this.memberId = memberId;
    }
}

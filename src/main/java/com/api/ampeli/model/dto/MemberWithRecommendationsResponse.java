package com.api.ampeli.model.dto;

import com.api.ampeli.model.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberWithRecommendationsResponse {
    private Member member;
    private ConnectionResponse recommendations;
}

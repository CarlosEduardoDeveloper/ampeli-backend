package com.api.ampeli.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationItem {
    private String name;
    private String description;
    private Double score;
}

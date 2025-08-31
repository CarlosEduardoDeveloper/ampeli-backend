package com.api.ampeli.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionResponse {
    private List<RecommendationItem> groups;
    private List<RecommendationItem> ministries;
    private List<RecommendationItem> cells;
}

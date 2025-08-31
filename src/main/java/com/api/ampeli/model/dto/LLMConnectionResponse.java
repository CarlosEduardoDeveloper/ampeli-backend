package com.api.ampeli.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LLMConnectionResponse {

    @JsonProperty("groups")
    private List<LLMRecommendationItem> groups;

    @JsonProperty("ministries")
    private List<LLMRecommendationItem> ministries;

    @JsonProperty("cells")
    private List<LLMRecommendationItem> cells;
}

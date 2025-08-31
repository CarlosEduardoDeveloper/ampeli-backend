package com.api.ampeli.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LLMConnectionRequest {

    @JsonProperty("member")
    private LLMMemberDto member;

    @JsonProperty("groups")
    private List<LLMGroupDto> groups;

    @JsonProperty("ministries")
    private List<LLMMinistryDto> ministries;

    @JsonProperty("cells")
    private List<LLMCellDto> cells;
}

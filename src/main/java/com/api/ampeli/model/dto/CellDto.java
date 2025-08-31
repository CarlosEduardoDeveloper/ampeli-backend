package com.api.ampeli.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CellDto {
    private String name;
    private String description;
    private String leaderPhone;
}

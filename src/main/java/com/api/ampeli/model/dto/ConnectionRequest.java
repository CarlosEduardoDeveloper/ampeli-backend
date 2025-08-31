package com.api.ampeli.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionRequest {
    private MemberDto member;
    private List<GroupDto> groups;
    private List<MinistryDto> ministries;
    private List<CellDto> cells;
}

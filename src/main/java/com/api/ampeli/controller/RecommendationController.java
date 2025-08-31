package com.api.ampeli.controller;

import com.api.ampeli.model.dto.*;
import com.api.ampeli.model.*;
import com.api.ampeli.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin(origins = "*")
public class RecommendationController {

    @Autowired
    private LLMService llmService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private MinistryService ministryService;

    @Autowired
    private CellService cellService;

    @PostMapping("/member/{memberId}")
    public ResponseEntity<?> getRecommendationsForMember(@PathVariable Long memberId) {
        try {
            // Get member data
            Member member = memberService.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + memberId));

            // Get all available groups, ministries, and cells
            List<Group> groups = groupService.findAll();
            List<Ministry> ministries = ministryService.findAll();
            List<Cell> cells = cellService.findAll();

            // Convert to DTOs
            MemberDto memberDto = convertToMemberDto(member);
            List<GroupDto> groupDtos = groups.stream().map(this::convertToGroupDto).collect(Collectors.toList());
            List<MinistryDto> ministryDtos = ministries.stream().map(this::convertToMinistryDto).collect(Collectors.toList());
            List<CellDto> cellDtos = cells.stream().map(this::convertToCellDto).collect(Collectors.toList());

            // Create request for LLM
            ConnectionRequest request = new ConnectionRequest(memberDto, groupDtos, ministryDtos, cellDtos);

            // Get recommendations from LLM
            ConnectionResponse response = llmService.getRecommendations(request);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/custom")
    public ResponseEntity<?> getCustomRecommendations(@RequestBody ConnectionRequest request) {
        try {
            ConnectionResponse response = llmService.getRecommendations(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/health")
    public ResponseEntity<?> checkLLMHealth() {
        boolean isAvailable = llmService.isLLMServiceAvailable();
        if (isAvailable) {
            return ResponseEntity.ok().body("LLM service is available");
        } else {
            return ResponseEntity.serviceUnavailable().body("LLM service is not available");
        }
    }

    // Conversion methods
    private MemberDto convertToMemberDto(Member member) {
        MemberDto dto = new MemberDto();
        dto.setUserId(member.getUser() != null ? member.getUser().getId().intValue() : null);
        dto.setNomeCompleto(member.getFullName());
        dto.setDataNascimento(member.getBirthDate());
        dto.setGenero(member.getGender() != null ? member.getGender().name().toLowerCase() : null);
        dto.setEstadoCivil(member.getMaritalStatus() != null ? member.getMaritalStatus().name().toLowerCase() : null);
        dto.setEmail(member.getEmail());
        dto.setTelefone(member.getPhone());
        dto.setTempoIgreja(member.getChurchAttendanceTime());
        dto.setOutrasIgrejas(member.getPreviousChurches());
        dto.setComoConheceu(member.getHowFoundChurch());
        dto.setParticipacaoAnterior(member.getPreviousParticipation());
        dto.setAreasInteresse(member.getInterestAreas() != null ?
            List.of(member.getInterestAreas().split(",")) : null);
        dto.setHabilidadesDons(member.getSkillsGifts());
        dto.setVoluntariarArea(member.getVolunteerArea());
        dto.setDiasHorariosDisponiveis(member.getAvailableDaysTimes());
        dto.setPreferenciaEventos(member.getEventPreference() != null ?
            member.getEventPreference().name().toLowerCase() : null);
        dto.setInteresseEm(member.getInterestsIn() != null ?
            List.of(member.getInterestsIn().split(",")) : null);
        dto.setBuscaNaIgreja(member.getChurchSearch());
        dto.setDispostoNovosGrupos(member.getOpenToNewGroups());
        dto.setPreferenciaGrupos(member.getGroupPreference());
        dto.setEstagioFe(member.getFaithStage() != null ?
            member.getFaithStage().name().toLowerCase() : null);
        dto.setAcompanhamentoPastoral(member.getPastoralSupportInterest());
        dto.setDificuldadesFe(member.getFaithDifficulties());
        return dto;
    }

    private GroupDto convertToGroupDto(Group group) {
        return new GroupDto(group.getName(), group.getDescription(), group.getLeaderPhone());
    }

    private MinistryDto convertToMinistryDto(Ministry ministry) {
        return new MinistryDto(ministry.getName(), ministry.getDescription(), ministry.getLeaderPhone());
    }

    private CellDto convertToCellDto(Cell cell) {
        return new CellDto(cell.getName(), cell.getDescription(), cell.getLeaderPhone());
    }
}

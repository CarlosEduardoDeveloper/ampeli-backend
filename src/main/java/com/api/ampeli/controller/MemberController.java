package com.api.ampeli.controller;

import com.api.ampeli.model.Member;
import com.api.ampeli.model.Group;
import com.api.ampeli.model.Ministry;
import com.api.ampeli.model.Cell;
import com.api.ampeli.model.dto.*;
import com.api.ampeli.services.MemberService;
import com.api.ampeli.services.LLMService;
import com.api.ampeli.services.GroupService;
import com.api.ampeli.services.MinistryService;
import com.api.ampeli.services.CellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/members")
@CrossOrigin(origins = "*")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private LLMService llmService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private MinistryService ministryService;

    @Autowired
    private CellService cellService;

    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.findAll();
        return ResponseEntity.ok(members);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        Optional<Member> member = memberService.findById(id);
        return member.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Member> getMemberByUserId(@PathVariable Long userId) {
        Optional<Member> member = memberService.findByUserId(userId);
        return member.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Member> getMemberByEmail(@PathVariable String email) {
        Optional<Member> member = memberService.findByEmail(email);
        return member.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/faith-stage/{faithStage}")
    public ResponseEntity<List<Member>> getMembersByFaithStage(@PathVariable String faithStage) {
        try {
            Member.FaithStage stage = Member.FaithStage.valueOf(faithStage.toUpperCase());
            List<Member> members = memberService.findByFaithStage(stage);
            return ResponseEntity.ok(members);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/interest/{interest}")
    public ResponseEntity<List<Member>> getMembersByInterest(@PathVariable String interest) {
        List<Member> members = memberService.findByInterestAreasContaining(interest);
        return ResponseEntity.ok(members);
    }

    @GetMapping("/volunteer-area/{area}")
    public ResponseEntity<List<Member>> getMembersByVolunteerArea(@PathVariable String area) {
        List<Member> members = memberService.findByVolunteerAreaContaining(area);
        return ResponseEntity.ok(members);
    }

    @PostMapping
    public ResponseEntity<?> createMember(@Valid @RequestBody Member member) {
        try {
            Member savedMember = memberService.save(member);

            // Get recommendations from LLM service
            try {
                ConnectionResponse recommendations = getRecommendationsForMember(savedMember);

                // Create response with member and recommendations
                MemberWithRecommendationsResponse response = new MemberWithRecommendationsResponse();
                response.setMember(savedMember);
                response.setRecommendations(recommendations);

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } catch (Exception llmException) {
                // If LLM service fails, still return the saved member
                System.err.println("LLM service failed: " + llmException.getMessage());
                return ResponseEntity.status(HttpStatus.CREATED).body(savedMember);
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private ConnectionResponse getRecommendationsForMember(Member member) {
        // Convert Member to MemberDto
        MemberDto memberDto = convertToMemberDto(member);

        // Get all available groups, ministries, and cells
        List<GroupDto> groups = groupService.findAll().stream()
                .map(this::convertToGroupDto)
                .collect(Collectors.toList());

        List<MinistryDto> ministries = ministryService.findAll().stream()
                .map(this::convertToMinistryDto)
                .collect(Collectors.toList());

        List<CellDto> cells = cellService.findAll().stream()
                .map(this::convertToCellDto)
                .collect(Collectors.toList());

        // Create connection request
        ConnectionRequest request = new ConnectionRequest();
        request.setMember(memberDto);
        request.setGroups(groups);
        request.setMinistries(ministries);
        request.setCells(cells);

        // Get recommendations from LLM service
        return llmService.getRecommendations(request);
    }

    private MemberDto convertToMemberDto(Member member) {
        MemberDto dto = new MemberDto();
        dto.setUserId(member.getUser() != null ? member.getUser().getId().intValue() : null);
        dto.setNomeCompleto(member.getFullName());
        dto.setDataNascimento(member.getBirthDate());
        dto.setGenero(member.getGender() != null ? member.getGender().toString().toLowerCase() : null);
        dto.setEstadoCivil(member.getMaritalStatus() != null ? member.getMaritalStatus().toString().toLowerCase() : null);
        dto.setEmail(member.getEmail());
        dto.setTelefone(member.getPhone());
        dto.setTempoIgreja(member.getChurchAttendanceTime());
        dto.setOutrasIgrejas(member.getPreviousChurches());
        dto.setComoConheceu(member.getHowFoundChurch());
        dto.setParticipacaoAnterior(member.getPreviousParticipation());

        // Convert comma-separated strings to lists
        if (member.getInterestAreas() != null) {
            dto.setAreasInteresse(List.of(member.getInterestAreas().split(",")));
        }

        dto.setHabilidadesDons(member.getSkillsGifts());
        dto.setVoluntariarArea(member.getVolunteerArea());
        dto.setDiasHorariosDisponiveis(member.getAvailableDaysTimes());
        dto.setPreferenciaEventos(member.getEventPreference() != null ? member.getEventPreference().toString().toLowerCase() : null);

        if (member.getInterestsIn() != null) {
            dto.setInteresseEm(List.of(member.getInterestsIn().split(",")));
        }

        dto.setBuscaNaIgreja(member.getChurchSearch());
        dto.setDispostoNovosGrupos(member.getOpenToNewGroups());
        dto.setPreferenciaGrupos(member.getGroupPreference());
        dto.setEstagioFe(member.getFaithStage() != null ? member.getFaithStage().toString().toLowerCase() : null);
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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMember(@PathVariable Long id, @Valid @RequestBody Member memberDetails) {
        try {
            Member updatedMember = memberService.update(id, memberDetails);
            return ResponseEntity.ok(updatedMember);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable Long id) {
        try {
            memberService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

package com.api.ampeli.services;

import com.api.ampeli.model.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LLMService {

    @Value("${llm.service.url:https://createhack-ampeli-8v64pyqxh-lauropereiras-projects.vercel.app}")
    private String llmServiceUrl;

    private final RestTemplate restTemplate;

    public LLMService() {
        this.restTemplate = new RestTemplate();
    }

    public ConnectionResponse getRecommendations(ConnectionRequest request) {
        try {
            // Convert to LLM format
            LLMConnectionRequest llmRequest = convertToLLMRequest(request);
            
            String url = llmServiceUrl + "/recommend-connections";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<LLMConnectionRequest> entity = new HttpEntity<>(llmRequest, headers);

            ResponseEntity<LLMConnectionResponse> response = restTemplate.postForEntity(
                url, entity, LLMConnectionResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return convertToConnectionResponse(response.getBody());
            } else {
                throw new RuntimeException("LLM service returned error: " + response.getStatusCode());
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to communicate with LLM service: " + e.getMessage(), e);
        }
    }

    public boolean isLLMServiceAvailable() {
        try {
            String url = llmServiceUrl + "/";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    private LLMConnectionRequest convertToLLMRequest(ConnectionRequest request) {
        LLMConnectionRequest llmRequest = new LLMConnectionRequest();
        
        // Convert member
        llmRequest.setMember(convertToLLMMember(request.getMember()));
        
        // Convert groups
        llmRequest.setGroups(request.getGroups().stream()
            .map(this::convertToLLMGroup)
            .toList());
        
        // Convert ministries
        llmRequest.setMinistries(request.getMinistries().stream()
            .map(this::convertToLLMMinistry)
            .toList());
        
        // Convert cells
        llmRequest.setCells(request.getCells().stream()
            .map(this::convertToLLMCell)
            .toList());
        
        return llmRequest;
    }

    private LLMMemberDto convertToLLMMember(MemberDto member) {
        LLMMemberDto llmMember = new LLMMemberDto();
        
        llmMember.setNomeCompleto(member.getNomeCompleto());
        llmMember.setDataNascimento(member.getDataNascimento() != null ? 
            member.getDataNascimento().toString() : null);
        llmMember.setGenero(member.getGenero());
        llmMember.setEstadoCivil(member.getEstadoCivil());
        llmMember.setTelefone(member.getTelefone());
        llmMember.setEmail(member.getEmail());
        llmMember.setEndereco(""); // Endereço não está no MemberDto atual
        llmMember.setProfissao(""); // Profissão não está no MemberDto atual
        llmMember.setInteresses(member.getAreasInteresse());
        llmMember.setDisponibilidade(member.getDiasHorariosDisponiveis());
        llmMember.setEstagioFe(member.getEstagioFe());
        llmMember.setExperienciaLideranca(member.getParticipacaoAnterior());
        llmMember.setPreferenciasEventos(member.getPreferenciaEventos());
        llmMember.setObservacoes(member.getBuscaNaIgreja());
        llmMember.setUserId(member.getUserId());
        
        return llmMember;
    }

    private LLMGroupDto convertToLLMGroup(GroupDto group) {
        return new LLMGroupDto(group.getName(), group.getDescription(), group.getLeaderPhone());
    }

    private LLMMinistryDto convertToLLMMinistry(MinistryDto ministry) {
        return new LLMMinistryDto(ministry.getName(), ministry.getDescription(), ministry.getLeaderPhone());
    }

    private LLMCellDto convertToLLMCell(CellDto cell) {
        return new LLMCellDto(cell.getName(), cell.getDescription(), cell.getLeaderPhone());
    }

    private ConnectionResponse convertToConnectionResponse(LLMConnectionResponse llmResponse) {
        ConnectionResponse response = new ConnectionResponse();
        
        // Convert groups
        if (llmResponse.getGroups() != null) {
            response.setGroups(llmResponse.getGroups().stream()
                .map(item -> new RecommendationItem(item.getName(), item.getDescription(), item.getScore()))
                .toList());
        }
        
        // Convert ministries
        if (llmResponse.getMinistries() != null) {
            response.setMinistries(llmResponse.getMinistries().stream()
                .map(item -> new RecommendationItem(item.getName(), item.getDescription(), item.getScore()))
                .toList());
        }
        
        // Convert cells
        if (llmResponse.getCells() != null) {
            response.setCells(llmResponse.getCells().stream()
                .map(item -> new RecommendationItem(item.getName(), item.getDescription(), item.getScore()))
                .toList());
        }
        
        return response;
    }
}

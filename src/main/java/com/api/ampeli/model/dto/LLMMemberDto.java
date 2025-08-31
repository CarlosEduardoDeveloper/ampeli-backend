package com.api.ampeli.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LLMMemberDto {

    @JsonProperty("nome_completo")
    private String nomeCompleto;

    @JsonProperty("data_nascimento")
    private String dataNascimento;

    @JsonProperty("genero")
    private String genero;

    @JsonProperty("estado_civil")
    private String estadoCivil;

    @JsonProperty("telefone")
    private String telefone;

    @JsonProperty("email")
    private String email;

    @JsonProperty("endereco")
    private String endereco;

    @JsonProperty("profissao")
    private String profissao;

    @JsonProperty("interesses")
    private List<String> interesses;

    @JsonProperty("disponibilidade")
    private String disponibilidade;

    @JsonProperty("estagio_fe")
    private String estagioFe;

    @JsonProperty("experiencia_lideranca")
    private String experienciaLideranca;

    @JsonProperty("preferencias_eventos")
    private String preferenciasEventos;

    @JsonProperty("observacoes")
    private String observacoes;

    @JsonProperty("user_id")
    private Integer userId;
}

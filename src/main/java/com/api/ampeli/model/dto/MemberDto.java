package com.api.ampeli.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private Integer userId;
    private String nomeCompleto;
    private LocalDate dataNascimento;
    private String genero; // "masculino", "feminino", "outro"
    private String estadoCivil; // "solteiro", "casado", "divorciado", "viuvo"
    private String email;
    private String telefone;
    private String tempoIgreja;
    private String outrasIgrejas;
    private String comoConheceu;
    private String participacaoAnterior;
    private List<String> areasInteresse;
    private String habilidadesDons;
    private String voluntariarArea;
    private String diasHorariosDisponiveis;
    private String preferenciaEventos; // "presencial", "digital", "ambos"
    private List<String> interesseEm;
    private String buscaNaIgreja;
    private Boolean dispostoNovosGrupos;
    private String preferenciaGrupos;
    private String estagioFe; // "iniciante", "caminhando", "atuante"
    private Boolean acompanhamentoPastoral;
    private String dificuldadesFe;
}

package com.api.ampeli.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "members")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Full name is required")
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String fullName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status", length = 20)
    private MaritalStatus maritalStatus;

    @Size(max = 150)
    @Column(length = 150)
    private String email;

    @Size(max = 20)
    @Column(length = 20)
    private String phone;

    @Column(name = "church_attendance_time")
    private String churchAttendanceTime; // mudado para String para compatibilidade

    @Size(max = 500)
    @Column(name = "previous_churches", length = 500)
    private String previousChurches;

    @Size(max = 300)
    @Column(name = "how_found_church", length = 300)
    private String howFoundChurch;

    @Size(max = 500)
    @Column(name = "previous_participation", length = 500)
    private String previousParticipation; // participacao_anterior

    @Size(max = 500)
    @Column(name = "interest_areas", length = 500)
    private String interestAreas; // areas_interesse como String separada por vírgulas

    @Size(max = 500)
    @Column(name = "skills_gifts", length = 500)
    private String skillsGifts; // habilidades_dons

    @Size(max = 300)
    @Column(name = "volunteer_area", length = 300)
    private String volunteerArea;

    @Size(max = 200)
    @Column(name = "available_days_times", length = 200)
    private String availableDaysTimes; // dias_horarios_disponiveis

    @Enumerated(EnumType.STRING)
    @Column(name = "event_preference", length = 20)
    private EventPreference eventPreference;

    @Size(max = 500)
    @Column(name = "interests_in", length = 500)
    private String interestsIn; // interesse_em como String separada por vírgulas

    @Size(max = 500)
    @Column(name = "church_search", length = 500)
    private String churchSearch; // busca_na_igreja

    @Column(name = "open_to_new_groups")
    private Boolean openToNewGroups;

    @Size(max = 300)
    @Column(name = "group_preference", length = 300)
    private String groupPreference; // preferencia_grupos

    @Enumerated(EnumType.STRING)
    @Column(name = "faith_stage", length = 30)
    private FaithStage faithStage;

    @Column(name = "pastoral_support_interest")
    private Boolean pastoralSupportInterest;

    @Size(max = 500)
    @Column(name = "faith_difficulties", length = 500)
    private String faithDifficulties; // dificuldades_fe

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(mappedBy = "members", fetch = FetchType.LAZY)
    private Set<Group> groups = new HashSet<>();

    @ManyToMany(mappedBy = "members", fetch = FetchType.LAZY)
    private Set<Ministry> ministries = new HashSet<>();

    @ManyToMany(mappedBy = "members", fetch = FetchType.LAZY)
    private Set<Cell> cells = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    public enum Gender {
        MASCULINO, FEMININO, OUTRO
    }

    public enum MaritalStatus {
        SOLTEIRO, CASADO, DIVORCIADO, VIUVO
    }

    public enum AgeGroupPreference {
        CHILDREN, TEENS, YOUNG_ADULTS, ADULTS, SENIORS, NO_PREFERENCE
    }

    public enum FaithStage {
        INICIANTE, CAMINHANDO, ATUANTE
    }

    public enum EventPreference {
        PRESENCIAL, DIGITAL, AMBOS
    }
}

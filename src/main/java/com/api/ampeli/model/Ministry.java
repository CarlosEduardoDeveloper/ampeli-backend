package com.api.ampeli.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ministries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ministry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String name;

    @Size(max = 500)
    @Column(length = 500)
    private String description;

    @Size(max = 20)
    @Column(name = "leader_phone", length = 20)
    private String leaderPhone;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "ministry_members",
        joinColumns = @JoinColumn(name = "ministry_id"),
        inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private Set<Member> members = new HashSet<>();

    public Ministry(String name, String description, String leaderPhone) {
        this.name = name;
        this.description = description;
        this.leaderPhone = leaderPhone;
    }
}

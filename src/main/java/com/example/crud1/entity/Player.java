package com.example.crud1.entity;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter@Setter
@Builder
@Entity
@IdClass(PlayerPrimaryKey.class)
@Hidden
@EqualsAndHashCode
public class Player {
    @Column(updatable = false, nullable = false, unique = true )
    private UUID id;
    @Id
    @Column(nullable = false)
    private String name;
    @Id
    @Column(nullable = false)
    private String surname;
    private LocalDate birthdate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;
}

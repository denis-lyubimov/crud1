package com.example.crud1.entity;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Hidden
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;
    @Column(unique = true)
    private String name;
    private BigDecimal bank;
    @OneToMany(mappedBy = "team", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Player> players;

    public boolean equalsWithoutId(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Team team = (Team) obj;
        return this.name.equals(team.name) && this.getBank().compareTo(team.getBank()) == 0;
    }
}

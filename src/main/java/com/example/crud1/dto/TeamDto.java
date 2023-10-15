package com.example.crud1.dto;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
@Getter@Setter
@ToString
@EqualsAndHashCode
@Hidden
@Schema(hidden = true)
public class TeamDto implements Serializable {
    private UUID id;
    @Pattern(regexp = "[a-zA-Z0-9]+", message = "Team name must contain letters and digits only")
    private String name;
    @NotNull
    private BigDecimal bank;
    private List<PlayerDto> players;

    @AssertTrue(message = "no need poor teams")
    private boolean isTeamPoor(){
        if (this.bank == null){
            return true;
        }
        return (this.bank.compareTo(BigDecimal.valueOf(5.0)) >= 0);
    }
}

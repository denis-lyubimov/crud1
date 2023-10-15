package com.example.crud1.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@Getter@Setter
@ToString
@Hidden
@EqualsAndHashCode
@Schema(hidden = true)
public class PlayerDto implements Serializable {
    private UUID id;
    @NotBlank(message = "name cannot be empty or null")
    private String name;
    @NotBlank(message = "surname cannot be empty or null")
    private String surname;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;
    private String team;

    @AssertTrue(message = "player's age should be >= 18")
    private boolean isBirthdateTooBig(){
        if (this.birthdate == null){
            return true;
        }
        return this.birthdate.isBefore(LocalDate.now().minusYears(17));
    }

    @AssertTrue(message = "player's age should be <= 50")
    private boolean isBirthdateTooSmall(){
        if (this.birthdate == null){
            return true;
        }
        return this.birthdate.isAfter(LocalDate.now().minusYears(51));
    }
}

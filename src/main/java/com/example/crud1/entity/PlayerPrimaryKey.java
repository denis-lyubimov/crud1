package com.example.crud1.entity;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Hidden
public class PlayerPrimaryKey implements Serializable {
    private String name;
    private String surname;
}

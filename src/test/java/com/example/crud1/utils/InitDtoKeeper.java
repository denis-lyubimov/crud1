package com.example.crud1.utils;

import com.example.crud1.dto.PlayerDto;
import com.example.crud1.dto.TeamDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class InitDtoKeeper {
    public static List<TeamDto> getInitTeamDtoListForTeamTest() {
        return new ArrayList<>() {{
            add(
                    TeamDto.builder()
                            .id(UUID.fromString("bee4f99c-24f6-4831-bafc-d338ab96cc35"))
                            .name("team1")
                            .bank(BigDecimal.valueOf(32).setScale(2, RoundingMode.HALF_UP))
                            .players(new ArrayList<PlayerDto>() {
                                {
                                    add(
                                            PlayerDto.builder()
                                                    .id(UUID.fromString("84dc3967-ffc0-4f3c-a4b4-0e872a253ac0"))
                                                    .name("name1")
                                                    .surname("surname1")
                                                    .birthdate(LocalDate.parse("2000-01-01"))
                                                    .team("team1")
                                                    .build()
                                    );
                                    add(
                                            PlayerDto.builder()
                                                    .id(UUID.fromString("ceb0c0a9-0264-40d0-87f1-817fc8805f1c"))
                                                    .name("name2")
                                                    .surname("surname2")
                                                    .birthdate(LocalDate.parse("2000-01-01"))
                                                    .team("team1")
                                                    .build()
                                    );
                                }
                            })
                            .build()
            );
            add(
                    TeamDto.builder()
                            .id(UUID.fromString("28d970db-3486-4dff-bc95-e006974cff65"))
                            .name("team2")
                            .bank(BigDecimal.valueOf(22).setScale(2, RoundingMode.HALF_UP))
                            .players(new ArrayList<PlayerDto>() {
                                {
                                    add(
                                            PlayerDto.builder()
                                                    .id(UUID.fromString("7e28303e-f662-4dc5-835c-3ecf7694ce7c"))
                                                    .name("name3")
                                                    .surname("surname3")
                                                    .birthdate(LocalDate.parse("2000-01-01"))
                                                    .team("team2")
                                                    .build()
                                    );
                                    add(
                                            PlayerDto.builder()
                                                    .id(UUID.fromString("c586fb8e-b8eb-4c86-8c31-766668c226ff"))
                                                    .name("name4")
                                                    .surname("surname4")
                                                    .birthdate(LocalDate.parse("2000-01-01"))
                                                    .team("team2")
                                                    .build()
                                    );
                                }
                            })
                            .build()
            );
            add(
                    TeamDto.builder()
                            .id(UUID.fromString("959f777c-a3a3-420b-8fe0-1500eb1643df"))
                            .name("team3")
                            .bank(BigDecimal.valueOf(560).setScale(2, RoundingMode.HALF_UP))
                            .players(Collections.emptyList())
                            .build()
            );
            add(
                    TeamDto.builder()
                            .id(UUID.fromString("f17639c1-5d0c-427a-a30e-1ce664debb64"))
                            .name("team4")
                            .bank(BigDecimal.valueOf(123).setScale(2, RoundingMode.HALF_UP))
                            .players(Collections.emptyList())
                            .build()
            );
        }};
    }

    public static List<PlayerDto> getInitPlayerDtoListForPlayerTest() {
        return new ArrayList<>() {
            {
                add(
                        PlayerDto.builder()
                                .id(UUID.fromString("84dc3967-ffc0-4f3c-a4b4-0e872a253ac0"))
                                .name("name1")
                                .surname("surname1")
                                .birthdate(LocalDate.parse("2000-01-01"))
                                .team(null)
                                .build()
                );
                add(
                        PlayerDto.builder()
                                .id(UUID.fromString("ceb0c0a9-0264-40d0-87f1-817fc8805f1c"))
                                .name("name2")
                                .surname("surname2")
                                .birthdate(LocalDate.parse("2000-01-01"))
                                .team(null)
                                .build()
                );
                add(
                        PlayerDto.builder()
                                .id(UUID.fromString("7e28303e-f662-4dc5-835c-3ecf7694ce7c"))
                                .name("name3")
                                .surname("surname3")
                                .birthdate(LocalDate.parse("2000-01-01"))
                                .team(null)
                                .build()
                );
                add(
                        PlayerDto.builder()
                                .id(UUID.fromString("c586fb8e-b8eb-4c86-8c31-766668c226ff"))
                                .name("name4")
                                .surname("surname4")
                                .birthdate(LocalDate.parse("2000-01-01"))
                                .team(null)
                                .build()
                );
            }
        };
    }
}

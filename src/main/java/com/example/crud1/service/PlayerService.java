package com.example.crud1.service;

import com.example.crud1.dto.PlayerDto;
import com.example.crud1.dto.PlayerDtoForPatch;
import com.example.crud1.entity.Player;

import java.util.List;
import java.util.UUID;

public interface PlayerService {

    UUID create(Player Player);

    void removeById(UUID id);

    String removeByName(String name);

    void removeAll();

    void updateWithJson(String name, String surname, PlayerDtoForPatch playerDtoForPatch);

    void updateTeam(String name, String surname, String team);

    PlayerDto findById(UUID id);

    PlayerDto findByNameAndSurname(String name, String surname);

    List<PlayerDto> findByName(String name);

    List<PlayerDto> findAll();
}

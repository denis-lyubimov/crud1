package com.example.crud1.service;

import com.example.crud1.dto.TeamDto;
import com.example.crud1.entity.Team;

import java.util.List;
import java.util.UUID;

public interface TeamService {
    UUID create(Team team);

    TeamDto findById(UUID id);

    List<TeamDto> findAll();

    void updateById(UUID id, Team team);

    void deleteById(UUID id);

    void deleteAll();
}

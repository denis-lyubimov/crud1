package com.example.crud1.mapper.impl;

import com.example.crud1.dto.TeamDto;
import com.example.crud1.entity.Player;
import com.example.crud1.entity.Team;
import com.example.crud1.mapper.EntityDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TeamDtoMapper implements EntityDtoMapper<Team, TeamDto> {

    private final PlayerDtoMapper playerMapper;

    @Autowired
    public TeamDtoMapper(PlayerDtoMapper playerMapper) {
        this.playerMapper = playerMapper;
    }

    @Override
    public Team dtoToEntity(TeamDto teamDto) {
        Team team = Team.builder()
                .name(teamDto.getName())
                .bank(teamDto.getBank())
                .players(new ArrayList<>())
                .build();

        if (teamDto.getPlayers() != null) {
            team.setPlayers(teamDto.getPlayers().stream()
                    .map(playerDto -> {
                        Player player = playerMapper.dtoToEntity(playerDto);
                        player.setTeam(team);
                        return player;
                    })
                    .collect(Collectors.toList()));
        }

        return team;
    }

    @Override
    public TeamDto entityToDto(Team team) {
        return TeamDto.builder()
                .id(team.getId())
                .name(team.getName())
                .bank(team.getBank())
                .players(playerMapper.entityListToDtoList(team.getPlayers()))
                .build();
    }

    @Override
    public List<TeamDto> entityListToDtoList(List<Team> teams) {
        return teams.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Team> dtoListToEntityList(List<TeamDto> teamDtos) {
        return teamDtos.stream()
                .map(this::dtoToEntity)
                .collect(Collectors.toList());
    }
}

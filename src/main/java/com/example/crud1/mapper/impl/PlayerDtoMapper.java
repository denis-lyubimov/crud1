package com.example.crud1.mapper.impl;

import com.example.crud1.dto.PlayerDto;
import com.example.crud1.entity.Player;
import com.example.crud1.entity.Team;
import com.example.crud1.exception.NotFoundException;
import com.example.crud1.mapper.EntityDtoMapper;
import com.example.crud1.mapper.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PlayerDtoMapper implements EntityDtoMapper<Player, PlayerDto> {
    private final TeamRepository teamRepository;

    @Autowired
    public PlayerDtoMapper(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public Player dtoToEntity(PlayerDto playerDto) {
        Player player = Player.builder()
                .birthdate(playerDto.getBirthdate())
                .name(playerDto.getName())
                .surname(playerDto.getSurname())
                .build();

        if (playerDto.getTeam() != null) {
            Optional<Team> optionalTeam = teamRepository.findByName(playerDto.getTeam());
            optionalTeam.ifPresentOrElse(player::setTeam, () -> {
                throw new NotFoundException("Team %s not found".formatted(playerDto.getTeam()));
            });
        }

        return player;
    }

    @Override
    public PlayerDto entityToDto(Player player) {
        return PlayerDto.builder()
                .id(player.getId())
                .birthdate(player.getBirthdate())
                .name(player.getName())
                .surname(player.getSurname())
                .team(player.getTeam() == null ? null : player.getTeam().getName())
                .build();
    }

    @Override
    public List<PlayerDto> entityListToDtoList(List<Player> players) {
        return players.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Player> dtoListToEntityList(List<PlayerDto> playersDto) {
        return playersDto.stream()
                .map(this::dtoToEntity)
                .collect(Collectors.toList());
    }

}

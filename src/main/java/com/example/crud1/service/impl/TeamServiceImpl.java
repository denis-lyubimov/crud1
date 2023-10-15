package com.example.crud1.service.impl;

import com.example.crud1.dto.TeamDto;
import com.example.crud1.entity.Team;
import com.example.crud1.exception.NotFoundException;
import com.example.crud1.exception.NothingToChangeExcpetion;
import com.example.crud1.mapper.impl.TeamDtoMapper;
import com.example.crud1.mapper.repository.PlayerRepository;
import com.example.crud1.mapper.repository.TeamRepository;
import com.example.crud1.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final TeamDtoMapper teamMapper;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, PlayerRepository playerRepository, TeamDtoMapper teamMapper) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
        this.playerRepository = playerRepository;
    }

    @Override
    @Transactional
    public UUID create(Team team) {
        team.getPlayers().forEach(player -> {
            player.setId(UUID.randomUUID());
        });
        Team createdTeam = teamRepository.save(team);
        return createdTeam.getId();
    }


    @Override
    @Transactional(readOnly = true)
    public TeamDto findById(UUID id) {
        return teamRepository.findById(id)
                .map(teamMapper::entityToDto)
                .orElseThrow(() -> new NotFoundException("Team %s not found".formatted(id.toString())));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamDto> findAll() {
        return teamMapper.entityListToDtoList(teamRepository.findAll());
    }

    @Override
    @Transactional
    public void updateById(UUID id, Team team) {
        Team foundTeam = teamRepository.findById(id).orElseThrow(() -> new NotFoundException("Team %s not found".formatted(id.toString())));
        if (team.getPlayers().size() != 0){
            throw new IllegalArgumentException("Can not update or add players by put teams method, players should be empty");
        }
        if (team.equalsWithoutId(foundTeam)){
            throw new NothingToChangeExcpetion("Сhangeable fields are equals, nothing to update");
        }
        team.setId(id);
        teamRepository.save(team);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        teamRepository.findById(id).ifPresentOrElse(team -> {
                    playerRepository.findByTeam(team).forEach(optionalPlayer ->
                            optionalPlayer.ifPresent(player -> player.setTeam(null)));
                    teamRepository.deleteById(id);
                },
                () -> {
                    throw new NotFoundException("Team %s not found".formatted(id.toString()));
                }
        );
    }

    @Override
    @Transactional
    public void deleteAll() {
        teamRepository.findAll().forEach(team -> {
            playerRepository.findByTeam(team).forEach(optionalPlayer ->
                    optionalPlayer.ifPresent(player -> player.setTeam(null)));
        });
        teamRepository.deleteAll();
    }
}

package com.example.crud1.service.impl;

import com.example.crud1.dto.PlayerDto;
import com.example.crud1.dto.PlayerDtoForPatch;
import com.example.crud1.entity.Player;
import com.example.crud1.exception.NotFoundException;
import com.example.crud1.exception.NothingToChangeExcpetion;
import com.example.crud1.mapper.impl.PlayerDtoMapper;
import com.example.crud1.mapper.repository.PlayerRepository;
import com.example.crud1.mapper.repository.TeamRepository;
import com.example.crud1.service.PlayerService;
import com.example.crud1.utils.UtilsHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final PlayerDtoMapper playerMapper;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, PlayerDtoMapper playerMapper, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
        this.teamRepository = teamRepository;
    }

    // create
    @Override
    @Transactional
    public UUID create(Player player) {
        playerRepository.findByNameAndSurname(player.getName(), player.getSurname()).ifPresent(existingPlayer -> {
            throw new DuplicateKeyException("Player %s %s already exists".formatted(player.getName(), player.getSurname()));
        });
        player.setId(UUID.randomUUID());
        Player createdPlayer = playerRepository.save(player);
        return createdPlayer.getId();
    }

    // delete
    @Override
    @Transactional
    public void removeById(UUID id) {
        playerRepository.findById(id).orElseThrow(() -> new NotFoundException("Player is not found by id %s".formatted(id.toString())));
        playerRepository.deleteById(id);
    }

    @Transactional
    @Override
    public String removeByName(String name) {
        List<Player> removedPlayers = playerRepository.deleteByName(name);
        if (removedPlayers.size() == 0) {
            throw new NotFoundException("Player not found");
        }
        StringBuilder namesBulder = new StringBuilder();
        removedPlayers.forEach(player -> namesBulder.append(" ").append(player.getId()));
        return namesBulder.toString();
    }

    @Override
    public void removeAll() {
        playerRepository.deleteAll();
    }

    @Transactional
    @Override
    public void updateWithJson(String name, String surname, PlayerDtoForPatch playerDto) {
        Player player = findEntityByNameAndSurname(name, surname);
        Player playerForPathing = Player.builder()
                .id(player.getId())
                .name(name)
                .surname(surname)
                .birthdate(player.getBirthdate())
                .team(player.getTeam())
                .build();
        playerForPathing = changePlayerForUpdate(playerDto, playerForPathing);
        if (player.equals(playerForPathing)) {
            throw new NothingToChangeExcpetion("No new parameters for player %s %s".formatted(name, surname));
        }
        playerRepository.save(playerForPathing);
    }

    @Transactional
    @Override
    public void updateTeam(String name, String surname, String team) {
        Player player = findEntityByNameAndSurname(name, surname);
        Player playerForPathing = Player.builder()
                .id(player.getId())
                .name(name)
                .surname(surname)
                .birthdate(player.getBirthdate())
                .team(player.getTeam())
                .build();
        playerForPathing = changePlayerTeamForUpdate(team, playerForPathing);
        if (playerForPathing.equals(player)) {
            throw new NothingToChangeExcpetion("Player %s %s already is already on a team %s".formatted(name, surname, team));
        }
        playerRepository.save(playerForPathing);
    }

    // read
    @Override
    @Transactional(readOnly = true)
    public PlayerDto findById(UUID id) {
        return playerRepository.findById(id).map(playerMapper::entityToDto).orElseThrow(() -> new NotFoundException("Player is not found by id %s".formatted(id.toString())));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayerDto> findByName(String name) {
        List<PlayerDto> playerDtos = new ArrayList<>(Collections.emptyList());
        List<Optional<Player>> players = playerRepository.findByName(name);
        players.forEach(player -> player.ifPresentOrElse(presentPlayer -> playerDtos.add(playerMapper.entityToDto(presentPlayer)), () -> {
            throw new NotFoundException("Player %s not found".formatted(name));
        }));
        return playerDtos;
    }

    @Transactional(readOnly = true)
    @Override
    public PlayerDto findByNameAndSurname(String name, String surname) {
        return playerRepository.findByNameAndSurname(name, surname).map(playerMapper::entityToDto).orElseThrow(() -> new NotFoundException("Player %s %s not found".formatted(name, surname)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayerDto> findAll() {
        return playerRepository.findAll().stream().map(playerMapper::entityToDto).collect(Collectors.toList());
    }

    @Transactional
    Player findEntityByNameAndSurname(String name, String surname) {
        return playerRepository.findByNameAndSurname(name, surname).orElseThrow(() -> new NotFoundException("Player %s %s not found".formatted(name, surname)));
    }

    private Player changePlayerForUpdate(PlayerDtoForPatch playerDtoForPatch, Player player) {
        if (playerDtoForPatch.getBirthdate() != null && playerDtoForPatch.getBirthdate().compareTo(player.getBirthdate()) != 0) {
            player.setBirthdate(playerDtoForPatch.getBirthdate());
        }
        if (!UtilsHelper.playerDtoForPatchTeamIsEmpty(playerDtoForPatch)) {
            changePlayerTeamForUpdate(playerDtoForPatch.getTeam(), player);
        }
        return player;
    }

    private Player changePlayerTeamForUpdate(String teamName, Player player) {
        teamRepository.findByName(teamName).ifPresentOrElse(team -> {
            if (player.getTeam() == null || !player.getTeam().getName().equals(teamName)) {
                System.out.println("setting team");
                player.setTeam(team);
            }
        }, () -> {
            throw new NotFoundException("Team %s not found".formatted(teamName));
        });
        return player;
    }
}


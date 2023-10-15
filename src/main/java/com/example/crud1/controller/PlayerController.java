package com.example.crud1.controller;

import com.example.crud1.dto.PlayerDto;
import com.example.crud1.dto.PlayerDtoForPatch;
import com.example.crud1.dto.RestResponse;
import com.example.crud1.exception.NothingToChangeExcpetion;
import com.example.crud1.mapper.impl.PlayerDtoMapper;
import com.example.crud1.service.PlayerService;
import com.example.crud1.utils.UtilsHelper;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Hidden
@Validated
@RestController
@RequestMapping("/players")
@Tag(name = "контроллер для плеера", description = "rest crud для плеера")
public class PlayerController {
    private final PlayerService playerService;
    private final PlayerDtoMapper playerMapper;

    @Autowired
    public PlayerController(PlayerService playerService, PlayerDtoMapper playerMapper) {
        this.playerService = playerService;
        this.playerMapper = playerMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "создает пелеера")
    public RestResponse create(@RequestBody List<@Valid PlayerDto> playerDtos) {
        List<Map<String, String>> responsList = new ArrayList<>();
        for (PlayerDto playerDto : playerDtos) {
            UUID id = playerService.create(playerMapper.dtoToEntity(playerDto));
            Map<String, String> playerMap = new HashMap<>();
            playerMap.put("name", playerDto.getName());
            playerMap.put("surname", playerDto.getSurname());
            playerMap.put("id", id.toString());
            responsList.add(playerMap);
        }
        return new RestResponse(responsList);
    }

    @PatchMapping("/{name}/{surname}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "обновление всего плеера при поиске по имени и фамилии")
    public RestResponse updateWithJson(@Parameter(description = "имя плеера") @PathVariable("name") @NotBlank(message = "name can not be empty") String name,
                                       @Parameter(description = "фамилия плеера") @PathVariable("surname") @NotBlank(message = "surname can not be empty") String surname,
                                       @Parameter(description = "параметры для обновления плеера") @RequestBody @Valid PlayerDtoForPatch playerDto) {
        if (UtilsHelper.playerDtoForPatchVariablesAreEmpty(playerDto)) {
            throw new NothingToChangeExcpetion("Empty body, nothing to change");
        }
        playerService.updateWithJson(name, surname, playerDto);
        return new RestResponse("player %s %s was updated".formatted(name, surname));
    }

    @PatchMapping("/{name}/{surname}/team")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "обновление всего плеера при поиске по имени и фамилии")
    public RestResponse updateTeam(@Parameter(description = "имя плеера") @PathVariable("name") @NotBlank(message = "name can not be empty") String name,
                                   @Parameter(description = "фамилия плеера") @PathVariable("surname") @NotBlank(message = "surname can not be empty") String surname,
                                   @Parameter(description = "название команды") @RequestParam(name = "team") @Pattern(regexp = "[a-zA-Z0-9]+", message = "Team must contain letters and digits only") String team) {
        playerService.updateTeam(name, surname, team);
        return new RestResponse("player %s %s was updated".formatted(name, surname));
    }

    @GetMapping("/{id}")
    @Operation(summary = "возвращает плеера по айди")
    public PlayerDto findPlayerBydId(@Parameter(description = "UUID плеера") @PathVariable("id") @NotNull UUID id) {
        return playerService.findById(id);
    }

    @GetMapping("/{name}/{surname}")
    @Operation(summary = "возвращает плеера по имени и фамилии")
    public PlayerDto findPlayerByNameAndSurname(
            @Parameter(description = "имя плеера") @PathVariable("name") @NotBlank(message = "name can not be empty") String name,
            @Parameter(description = "фамилия плеера") @PathVariable("surname") @NotBlank(message = "surname can not be empty") String surname
    ) {
        return playerService.findByNameAndSurname(name, surname);
    }

    @GetMapping
    @Operation(summary = "возвращает всех плееров")
    public List<PlayerDto> findPlayer() {
        return playerService.findAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "удаляет плеера по id")
    public RestResponse deleteById(@Parameter(description = "UUID плеера") @PathVariable("id") @NotNull UUID id) {
        playerService.removeById(id);
        return new RestResponse("Player %s is deleted".formatted(id));
    }


    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "удаляет плеера по имени")
    public RestResponse deleteByName
            (@Parameter(description = "имя плеера") @RequestParam(name = "name") @NotBlank String name) {
        return new RestResponse(playerService.removeByName(name));
    }
}

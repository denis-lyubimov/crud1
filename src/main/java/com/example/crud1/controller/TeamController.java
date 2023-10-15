package com.example.crud1.controller;

import com.example.crud1.dto.RestResponse;
import com.example.crud1.dto.TeamDto;
import com.example.crud1.mapper.impl.TeamDtoMapper;
import com.example.crud1.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Validated
@RestController
@RequestMapping("/teams")
@Tag(name = "контроллер для тимы", description = "rest crud для тимы")
public class TeamController {

    private final TeamService teamService;
    private final TeamDtoMapper teamMapper;

    @Autowired
    public TeamController(TeamService teamService, TeamDtoMapper teamMapper) {
        this.teamService = teamService;
        this.teamMapper = teamMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "создает тиму")
    public RestResponse createTeam(@RequestBody List<@Valid TeamDto> teamDtos) {
        List<Map<String, String>> resultList = new ArrayList<>();
        for (TeamDto teamDto : teamDtos) {
            UUID id = teamService.create(teamMapper.dtoToEntity(teamDto));
            Map<String, String> teamMap = new HashMap<>();
            teamMap.put("name", teamDto.getName());
            teamMap.put("id", id.toString());
            resultList.add(teamMap);
        }
        return new RestResponse(resultList);
    }

    @GetMapping("/{id}")
    @Operation(summary = "возвращает тиму по айди")
    public TeamDto getTeamById(@Parameter(description = "UUID тимы") @PathVariable @NotNull UUID id) {
        return teamService.findById(id);
    }

    @GetMapping
    @Operation(summary = "возвращает все тимы")
    public List<TeamDto> getAllTeams() {
        return teamService.findAll();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "обновляет всю тиму при передаче id тимы, не меняет плееров")
    public RestResponse updateTeamById(@Parameter(description = "UUID тимы") @PathVariable UUID id,
                                       @RequestBody @Valid TeamDto teamDto) {
        teamService.updateById(id, teamMapper.dtoToEntity(teamDto));
        return new RestResponse("Team " + id + " was updated");
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "удаляет тиму по id")
    public RestResponse deleteTeamById(@Parameter(description = "UUID тимы") @PathVariable @NotNull(message = "team id can not be empty") UUID id) {
        teamService.deleteById(id);
        return new RestResponse("Team " + id + " was deleted");
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "удаляет все тимы")
    public RestResponse deleteAllTeams() {
        teamService.deleteAll();
        return new RestResponse("All teams were deleted");
    }
}

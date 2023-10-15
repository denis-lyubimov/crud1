package com.example.crud1.inetgrationtest;

import com.example.crud1.Crud1Application;
import com.example.crud1.dto.PlayerDto;
import com.example.crud1.dto.TeamDto;
import com.example.crud1.service.PlayerService;
import com.example.crud1.service.TeamService;
import com.example.crud1.utils.InitDtoKeeper;
import com.example.crud1.utils.JsonMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("teamServiceTests")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = {Crud1Application.class})
@AutoConfigureMockMvc
public class TeamControllerIT {
    @Autowired
    private MockMvc mvc;
    @Autowired
    PlayerService playerService;
    @Autowired
    TeamService teamService;

    @Test
    @DisplayName("should return all teams")
    void getAllTeamsTest() throws Exception {
        List<TeamDto> excpetedTeamDtos = InitDtoKeeper.getInitTeamDtoListForTeamTest();

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200))
                .andReturn();

        JSONArray actual = new JSONArray(mvcResult.getResponse().getContentAsString());
        JSONArray expected = new JSONArray();
        for (TeamDto teamDto : excpetedTeamDtos) {
            expected.put(JsonMapper.teamDtoToJson(teamDto));
        }

        Assertions.assertEquals(expected.toString(), actual.toString());
    }

    @Test
    @DisplayName("should return team by id")
    void getTeamByIdTest() throws Exception {
        TeamDto excpetedTeamDto = InitDtoKeeper.getInitTeamDtoListForTeamTest().get(0);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/teams/" + excpetedTeamDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200))
                .andReturn();

        JSONObject actual = new JSONObject(mvcResult.getResponse().getContentAsString());
        JSONObject expected = JsonMapper.teamDtoToJson(excpetedTeamDto);

        Assertions.assertEquals(expected.toString(), actual.toString());
    }

    @Test
    @DisplayName("should create team with player and delete team but not player")
    void createAndDeleteTeamTest() throws Exception {
        TeamDto excpetedTeamDto = TeamDto.builder()
                .name("team")
                .bank(BigDecimal.valueOf(11.11))
                .players(List.of(
                        PlayerDto.builder()
                                .name("name")
                                .surname("surname")
                                .birthdate(LocalDate.parse("1999-01-01"))
                                .build()
                ))
                .build();
        PlayerDto expectedPlayerDto = excpetedTeamDto.getPlayers().get(0);

        JSONObject teamWithPlayerJson = JsonMapper.teamDtoToJson(excpetedTeamDto);
        JSONArray requestBody = new JSONArray();
        requestBody.put(teamWithPlayerJson);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString())
                )
                .andExpect(status().is(201))
                .andReturn();

        String teamId = new JSONObject(mvcResult.getResponse().getContentAsString()).getJSONArray("result").getJSONObject(0).getString("id");
        TeamDto actualTeamDto = teamService.findById(UUID.fromString(teamId));
        PlayerDto actualPlayerDto = actualTeamDto.getPlayers().get(0);
        excpetedTeamDto.setId(UUID.fromString(teamId));
        expectedPlayerDto.setId(actualPlayerDto.getId());
        expectedPlayerDto.setTeam("team");

        //correct team and player were created
        Assertions.assertEquals(excpetedTeamDto, actualTeamDto);

        mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/teams/" + teamId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200))
                .andReturn();
        JSONObject expectedDeleteMessage = new JSONObject();
        expectedDeleteMessage.put("result", "Team %s was deleted".formatted(teamId));
        String actualDeleteMessage = mvcResult.getResponse().getContentAsString();
        // correct message after deleting team
        Assertions.assertEquals(expectedDeleteMessage.toString(), actualDeleteMessage);

        //deleted team does not exist
        mvc.perform(MockMvcRequestBuilders.get("/teams/"+ teamId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(404));

        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/players/"+ actualPlayerDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200))
                .andReturn();

        JSONObject actualPlayer = new JSONObject(mvcResult.getResponse().getContentAsString());

        //playerExists
        Assertions.assertEquals(expectedPlayerDto.getId().toString(), actualPlayer.getString("id"));
    }

}

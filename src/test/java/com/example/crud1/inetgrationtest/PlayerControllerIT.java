package com.example.crud1.inetgrationtest;

import com.example.crud1.Crud1Application;
import com.example.crud1.dto.PlayerDto;
import com.example.crud1.dto.PlayerDtoForPatch;
import com.example.crud1.dto.TeamDto;
import com.example.crud1.utils.InitDtoKeeper;
import com.example.crud1.utils.JsonMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;


@ActiveProfiles("palyerServiceTests")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {Crud1Application.class})
public class PlayerControllerIT {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("should return all players")
    void getAllPlayersTest() {
        List<PlayerDto> expectedBodyList = InitDtoKeeper.getInitPlayerDtoListForPlayerTest();

        webTestClient.get()
                .uri("/players")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PlayerDto.class).isEqualTo(expectedBodyList);
    }

    @Test
    @DisplayName("should return one player by id")
    void getPlayerByIdTest() {
        PlayerDto expectedBody = InitDtoKeeper.getInitPlayerDtoListForPlayerTest().get(0);

        webTestClient.get()
                .uri("/players/" + expectedBody.getId())
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PlayerDto.class).isEqualTo(expectedBody);
    }

    @Test
    @DisplayName("should return one player by name and surname")
    void getPlayerByNameAndSurnameTest() {
        PlayerDto expectedBody = InitDtoKeeper.getInitPlayerDtoListForPlayerTest().get(0);

        webTestClient.get()
                .uri("/players/name1/surname1")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PlayerDto.class).isEqualTo(expectedBody);
    }

    @Test
    @DisplayName("should check CRUD")
    void crudOnePlayerTest() throws JSONException {
        List<PlayerDto> newPlayerList = List.of(PlayerDto.builder()
                .name("createOnePlayerTestName")
                .surname("createOnePlayerTestSurame")
                .birthdate(LocalDate.parse("2000-01-01"))
                .team(null)
                .build()
        );

        //create player
        String message = newPlayerList.get(0).getName() + " " + newPlayerList.get(0).getId();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", new JSONArray().put(message));
        EntityExchangeResult<String> stringEntityExchangeResult = webTestClient.post()
                .uri("/players")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(newPlayerList)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(String.class)
                .returnResult();
        String playerId = new JSONObject(stringEntityExchangeResult.getResponseBody()).getJSONArray("result").getJSONObject(0).getString("id");
        newPlayerList.get(0).setId(UUID.fromString(playerId));
        PlayerDto expectedCreatedPlayer = newPlayerList.get(0);

        //check created player
        EntityExchangeResult<PlayerDto> playerDtoResultAfterPost = webTestClient.get()
                .uri("/players/" + playerId)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PlayerDto.class)
                .returnResult();
        PlayerDto actualCreatedPlayer = playerDtoResultAfterPost.getResponseBody();

        Assertions.assertEquals(expectedCreatedPlayer, actualCreatedPlayer);

        //check team1 and team2 do not have players
        TeamDto expectedTeam1Dto = TeamDto.builder()
                .id(UUID.fromString("bee4f99c-24f6-4831-bafc-d338ab96cc35"))
                .name("team1")
                .bank(new BigDecimal(32).setScale(2, RoundingMode.HALF_UP))
                .players(Collections.emptyList())
                .build();
        TeamDto expectedTeam2Dto = TeamDto.builder()
                .id(UUID.fromString("28d970db-3486-4dff-bc95-e006974cff65"))
                .name("team2")
                .bank(new BigDecimal(22).setScale(2, RoundingMode.HALF_UP))
                .players(Collections.emptyList())
                .build();

        EntityExchangeResult<TeamDto> team1DtoEntityExchangeResult = webTestClient.get()
                .uri("/teams/" + expectedTeam1Dto.getId())
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TeamDto.class)
                .returnResult();
        TeamDto actualTeam1Dto = team1DtoEntityExchangeResult.getResponseBody();

        Assertions.assertEquals(expectedTeam1Dto, actualTeam1Dto);

        EntityExchangeResult<TeamDto> team2DtoEntityExchangeResult = webTestClient.get()
                .uri("/teams/" + expectedTeam2Dto.getId())
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TeamDto.class)
                .returnResult();
        TeamDto actualTeam2Dto = team2DtoEntityExchangeResult.getResponseBody();

        Assertions.assertEquals(expectedTeam2Dto, actualTeam2Dto);

        //patch player with birthdate and team
        JSONObject patchResponse = new JSONObject();
        assert actualCreatedPlayer != null;
        patchResponse.put("result", "player %s %s was updated".formatted(actualCreatedPlayer.getName(), actualCreatedPlayer.getSurname()));
        PlayerDtoForPatch playerDtoForPatch = PlayerDtoForPatch.builder()
                .birthdate(LocalDate.parse("1990-01-01"))
                .team("team1")
                .build();
        webTestClient.patch()
                .uri("/players/" + actualCreatedPlayer.getName() + "/" + actualCreatedPlayer.getSurname())
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(playerDtoForPatch)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo(patchResponse.toString());
        PlayerDto expectedPathcedPlayer = expectedCreatedPlayer;
        expectedPathcedPlayer.setTeam(playerDtoForPatch.getTeam());
        expectedPathcedPlayer.setBirthdate(playerDtoForPatch.getBirthdate());

        //check patched player and team1
        EntityExchangeResult<PlayerDto> playerDtoResultAfterPatch = webTestClient.get()
                .uri("/players/" + playerId)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PlayerDto.class)
                .returnResult();
        PlayerDto actualPathcedPlayer = playerDtoResultAfterPatch.getResponseBody();

        Assertions.assertEquals(expectedPathcedPlayer, actualPathcedPlayer);

        team1DtoEntityExchangeResult = webTestClient.get()
                .uri("/teams/" + expectedTeam1Dto.getId())
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TeamDto.class)
                .returnResult();
        actualTeam1Dto = team1DtoEntityExchangeResult.getResponseBody();
        expectedTeam1Dto.setPlayers(List.of(expectedPathcedPlayer));

        Assertions.assertEquals(expectedTeam1Dto, actualTeam1Dto);

        //patch player with team
        webTestClient.patch()
                .uri("/players/" + actualCreatedPlayer.getName() + "/" + actualCreatedPlayer.getSurname() + "/team?team=team2")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(playerDtoForPatch)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo(patchResponse.toString());
        expectedPathcedPlayer.setTeam("team2");

        //check patched player and team1 + team2
        EntityExchangeResult<PlayerDto> playerDtoResultAfter2ndPatch = webTestClient.get()
                .uri("/players/" + playerId)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PlayerDto.class)
                .returnResult();
        actualPathcedPlayer = playerDtoResultAfter2ndPatch.getResponseBody();

        Assertions.assertEquals(expectedPathcedPlayer, actualPathcedPlayer);

        team1DtoEntityExchangeResult = webTestClient.get()
                .uri("/teams/" + expectedTeam1Dto.getId())
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TeamDto.class)
                .returnResult();
        actualTeam1Dto = team1DtoEntityExchangeResult.getResponseBody();
        expectedTeam1Dto.setPlayers(Collections.emptyList());

        Assertions.assertEquals(expectedTeam1Dto, actualTeam1Dto);

        team2DtoEntityExchangeResult = webTestClient.get()
                .uri("/teams/" + expectedTeam2Dto.getId())
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TeamDto.class)
                .returnResult();
        actualTeam2Dto = team2DtoEntityExchangeResult.getResponseBody();
        expectedTeam2Dto.setPlayers(List.of(expectedPathcedPlayer));

        Assertions.assertEquals(expectedTeam2Dto, actualTeam2Dto);

        //delete player
        JSONObject deleteResponse = new JSONObject();
        deleteResponse.put("result", "deleted player %s".formatted(actualCreatedPlayer.getName()));
        webTestClient.delete()
                .uri("/players/" + playerId)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo(deleteResponse.toString());

        //check player is deleted
        webTestClient.get()
                .uri("/players/" + playerId)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isNotFound();

        //check team2 does not have a player
        team2DtoEntityExchangeResult = webTestClient.get()
                .uri("/teams/" + expectedTeam2Dto.getId())
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TeamDto.class)
                .returnResult();
        actualTeam2Dto = team2DtoEntityExchangeResult.getResponseBody();
        expectedTeam2Dto.setPlayers(Collections.emptyList());

        Assertions.assertEquals(expectedTeam2Dto, actualTeam2Dto);
    }

    @Test
    @DisplayName("should return 404")
    void get404OnNotExistingPlayerTest() throws JSONException {
        UUID nonExistingPlayerId = UUID.randomUUID();
        String nonExistingPlayerName = "nonExistingPlayerName";
        String nonExistingPlayerSurname = "nonExistingPlayerSurame";

        //404 on getById
        EntityExchangeResult<String> getByIdEntityResult = webTestClient.get()
                .uri("/players/" + nonExistingPlayerId)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .returnResult();
        JSONObject jsonObject = new JSONObject(getByIdEntityResult.getResponseBody());
        Integer expectedStatus = 404;
        String expectedMessage = "Player is not found by id %s".formatted(nonExistingPlayerId.toString());
        String expectedDescription = "Not Found";

        Assertions.assertEquals(jsonObject.getInt("status"), expectedStatus);
        Assertions.assertEquals(jsonObject.getString("message"), expectedMessage);
        Assertions.assertEquals(jsonObject.getString("description"), expectedDescription);

        //404 on get by name/surname
        EntityExchangeResult<String> getByNameSurnameEntityResult = webTestClient.get()
                .uri("/players/" + nonExistingPlayerName + "/" + nonExistingPlayerSurname)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .returnResult();
        jsonObject = new JSONObject(getByNameSurnameEntityResult.getResponseBody());
        expectedMessage = "Player %s %s not found".formatted(nonExistingPlayerName, nonExistingPlayerSurname);

        Assertions.assertEquals(expectedStatus, jsonObject.getInt("status"));
        Assertions.assertEquals(expectedMessage, jsonObject.getString("message"));
        Assertions.assertEquals(expectedDescription, jsonObject.getString("description"));
    }

    @Test
    @DisplayName("should return 400 on wrong paramers while creating player")
    void get400OnCreatingPlayerTest() throws JSONException {
        List<PlayerDto> newPlayerList = List.of(PlayerDto.builder()
                .name("")
                .surname("")
                .birthdate(LocalDate.parse("3000-01-01"))
                .team(null)
                .build()
        );

        //400 on create
        EntityExchangeResult<String> entityExchangeResult = webTestClient.post()
                .uri("/players")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(newPlayerList)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .returnResult();
        JSONObject actualJsonObject = new JSONObject(entityExchangeResult.getResponseBody());

        String expectedAgeMessge = "player's age should be >= 18";
        String expectedNameMessage = "name cannot be empty or null";
        String expectedSurnameMessage = "surname cannot be empty or null";
        JSONArray expectedJsonArray = new JSONArray();
        expectedJsonArray.put(expectedAgeMessge);
        expectedJsonArray.put(expectedNameMessage);
        expectedJsonArray.put(expectedSurnameMessage);
        JSONObject expectedJsonObject = new JSONObject();
        expectedJsonObject.put("status", 400);
        expectedJsonObject.put("message", expectedJsonArray);
        expectedJsonObject.put("description", "Bad Request");

        HashSet<String> actualSet = JsonMapper.jsonArrayToHashSet(actualJsonObject.getJSONArray("message"));
        HashSet<String> expectedSet = JsonMapper.jsonArrayToHashSet(expectedJsonObject.getJSONArray("message"));

        Assertions.assertEquals(expectedJsonObject.getInt("status"), actualJsonObject.getInt("status"));
        Assertions.assertEquals(expectedSet, actualSet);
        Assertions.assertEquals(expectedJsonObject.getString("description"), actualJsonObject.getString("description"));
    }

    @Test
    @DisplayName("should return NothingToChangeExcpetion on empty patch parameters")
    void getNothingToChangeExcpetionPatchPlayerTest() throws JSONException {
        PlayerDtoForPatch playerDtoForPatch = PlayerDtoForPatch.builder()
                .birthdate(LocalDate.parse("2000-01-01"))
                .team("")
                .build();

        //patch player with no_changes-body
        EntityExchangeResult<String> entityExchangeResult = webTestClient.patch()
                .uri("/players/name1/surname1")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(playerDtoForPatch)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult();
        JSONObject actualJsonObject = new JSONObject(entityExchangeResult.getResponseBody());
        JSONObject expectedJsonObject = new JSONObject();
        expectedJsonObject.put("status", 200);
        expectedJsonObject.put("message", "No new parameters for player name1 surname1");
        expectedJsonObject.put("description", "OK");

        Assertions.assertEquals(expectedJsonObject.getInt("status"), actualJsonObject.getInt("status"));
        Assertions.assertEquals(expectedJsonObject.getString("message"), actualJsonObject.getString("message"));
        Assertions.assertEquals(expectedJsonObject.getString("description"), actualJsonObject.getString("description"));

        //patch player with empty body
        playerDtoForPatch.setBirthdate(null);
        entityExchangeResult = webTestClient.patch()
                .uri("/players/name1/surname1")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(playerDtoForPatch)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult();
        actualJsonObject = new JSONObject(entityExchangeResult.getResponseBody());
        expectedJsonObject.put("message", "Empty body, nothing to change");

        Assertions.assertEquals(expectedJsonObject.getInt("status"), actualJsonObject.getInt("status"));
        Assertions.assertEquals(expectedJsonObject.getString("message"), actualJsonObject.getString("message"));
        Assertions.assertEquals(expectedJsonObject.getString("description"), actualJsonObject.getString("description"));
    }
}

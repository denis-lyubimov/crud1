package com.example.crud1.utils;

import com.example.crud1.dto.PlayerDto;
import com.example.crud1.dto.TeamDto;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.List;

public class JsonMapper {
    public static JSONObject teamDtoToJson(TeamDto teamDto) throws JSONException {
        JSONObject teamDtoJson = new JSONObject();
        if (teamDto.getId() != null) {
            teamDtoJson.put("id", teamDto.getId());
        }
        teamDtoJson.put("name", teamDto.getName());
        teamDtoJson.put("bank", teamDto.getBank());
        JSONArray jsonArrayOfPlayers = new JSONArray();
        List<PlayerDto> playerDtos = teamDto.getPlayers();
        if (playerDtos != null && playerDtos.size() > 0) {
            for (PlayerDto playerDto : playerDtos) {
                jsonArrayOfPlayers.put(playerDtoToJson(playerDto));
            }
        }
        teamDtoJson.put("players", jsonArrayOfPlayers);
        return teamDtoJson;
    }

    public static JSONObject playerDtoToJson(PlayerDto playerDto) throws JSONException {
        JSONObject playerDtoJson = new JSONObject();
        if (playerDto.getId() != null) {
            playerDtoJson.put("id", playerDto.getId());
        }
        playerDtoJson.put("name", playerDto.getName());
        playerDtoJson.put("surname", playerDto.getSurname());
        playerDtoJson.put("birthdate", playerDto.getBirthdate());
        if (playerDto.getTeam() != null) {
            playerDtoJson.put("team", playerDto.getTeam());
        }
        return playerDtoJson;
    }

    public static HashSet<String> jsonArrayToHashSet(JSONArray jsonArray) throws JSONException {
        HashSet<String> hashSet = new HashSet<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            hashSet.add(jsonArray.getString(i));
        }
        return hashSet;
    }
}

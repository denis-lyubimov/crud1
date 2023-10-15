package com.example.crud1.utils;

import com.example.crud1.dto.PlayerDtoForPatch;

public class UtilsHelper {
    public UtilsHelper() {
    }

    public static boolean playerDtoForPatchVariablesAreEmpty(PlayerDtoForPatch playerDtoForPatch) {
        if (playerDtoForPatch.getTeam() == null && playerDtoForPatch.getBirthdate() == null) {
            return true;
        }
        if (playerDtoForPatch.getTeam().isBlank() && playerDtoForPatch.getBirthdate() == null) {
            return true;
        }
        return false;
    }

    public static boolean playerDtoForPatchTeamIsEmpty(PlayerDtoForPatch playerDtoForPatch) {
        if (playerDtoForPatch.getTeam() == null) {
            return true;
        }
        if (playerDtoForPatch.getTeam().isBlank()) {
            return true;
        }
        return false;
    }


}

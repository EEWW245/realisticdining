package com.example.realisticdining;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServerEatingState {

    private static final Map<UUID, Boolean> playerEatingState = new HashMap<>();

    public static void setEating(UUID playerId, boolean isEating) {
        playerEatingState.put(playerId, isEating);
    }

    public static boolean isEating(UUID playerId) {
        return playerEatingState.getOrDefault(playerId, false);
    }

    public static void reset(UUID playerId) {
        playerEatingState.remove(playerId);
    }
}

package com.franz.sud.engine.components.narrative;

import com.franz.sud.engine.components.geography.Room;

import java.util.HashMap;
import java.util.Map;

public class GameNarrative {
    private Map<Room, Narrative> gameNarrative = new HashMap<>();

    public GameNarrative() {

    }

    private GameNarrative(Room room) {
        gameNarrative.put(room, new Narrative(new String[]{}));
    }

    public Narrative getNarrative(Room room) {
        return gameNarrative.getOrDefault(room, new Narrative(new String[] {}));
    }

    public void addNarrative(Room rm, Narrative nar) {
        gameNarrative.put(rm, nar);
    }
}

package dialogue;

import components.geography.Room;

import java.util.HashMap;
import java.util.Map;

public class GameNarrative {
    private Map<Room, Narrative> gameNarrative = new HashMap<>();

    public GameNarrative() {

    }

    private GameNarrative(Room room) {
        gameNarrative.put(room, new Narrative());
    }

    public Narrative getNarrative(Room room) {
        if (gameNarrative.containsKey(room)) return gameNarrative.get(room);
        return new GameNarrative(room).getNarrative(room);
    }

    public void addNarrative(Room rm, Narrative nar) {
        gameNarrative.put(rm, nar);
    }
}

package dialogue;

import components.geography.Room;

import java.util.HashMap;
import java.util.Map;

public class GameNarrative {
    private Map<Room, Narrative> gameNarrative = new HashMap<>();
    private boolean isNarrated = false;

    public Narrative getNarrative(Room room) {
        return gameNarrative.get(room);
    }

    public void addNarrative(Room rm, Narrative nar) {
        gameNarrative.put(rm, nar);
    }

}

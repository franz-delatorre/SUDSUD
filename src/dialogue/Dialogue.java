package dialogue;

import java.util.ArrayList;

public class Dialogue {
    private ArrayList<String[]> actDialogue = new ArrayList<>();

    public Dialogue() {
    }

    public void addDialogue(String[] dialogue) {
        actDialogue.add(dialogue);
    }

    public String[] getDialogue(int index) {
        return actDialogue.get(index);
    }
}

package dialogue;

import java.util.ArrayList;

public class Narrative {
    private static final int NARRATIVE_SIZE = 2;

    private ArrayList<String[]> narrative = new ArrayList<>(NARRATIVE_SIZE);
    private boolean isNarrated = false;

    public Narrative() {
        narrative.add(new String[] {});
        narrative.add(new String[] {});
    }

    public void addNarrative(String[] s) {
        narrative.add(s);
    }

    public String[] getNarrative(int index) {
        if (index > NARRATIVE_SIZE - 1) {
            return new Narrative().getNarrative(index);
        }
        return narrative.get(index);
    }

    public boolean isNarrated() {
        return isNarrated;
    }

    public void setNarrated(boolean narrated) {
        isNarrated = narrated;
    }
}

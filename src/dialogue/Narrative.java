package dialogue;

import java.util.ArrayList;

public class Narrative {
    private ArrayList<String[]> narrative = new ArrayList<>(2);
    private boolean isNarrated = false;

    public Narrative() {
        narrative.add(new String[] {});
    }

    public void addNarrative(String[] s) {
        narrative.add(s);
    }

    public String[] getNarrative(int index) {
        if (index > narrative.size() - 1) {
            return new Narrative().getNarrative(0);
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

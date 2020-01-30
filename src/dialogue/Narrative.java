package dialogue;

import java.util.ArrayList;

public class Narrative {
    private ArrayList<String[]> narrative = new ArrayList<>(2);
    private boolean isNarrated = false;

    public void addNarrative(String[] s) {
        narrative.add(s);
    }

    public String[] getStrings(int index) {
        if (narrative.size() == 1) {
            return narrative.get(0);
        }

        if (index > 1 || narrative.get(index) == null) {
            return null;
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

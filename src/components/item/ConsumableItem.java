package components.item;

public class ConsumableItem extends Item {
    private boolean isUsed;

    public ConsumableItem() {
        this.isUsed = false;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }
}

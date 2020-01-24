package components.unit;

public class UnskilledUnit extends Unit {
    public static class Builder extends Unit.Builder<Builder> {
        public Builder(){}

        @Override
        public UnskilledUnit build() {
            return new UnskilledUnit(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    protected UnskilledUnit(Builder builder) {
        super(builder);
    }
}

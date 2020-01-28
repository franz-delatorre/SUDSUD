package misc;

public enum StatType {
    EVASION {
        @Override
        public String toString() {
            return "Evasion";
        }
    },
    CRITICAL_CHANCE {
        @Override
        public String toString() {
            return "Crit Chance";
        }
    },
    LIFESTEAL {
        @Override
        public String toString() {
            return "Lifesteal";
        }
    };
}

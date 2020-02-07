package com.franz.sud.engine.misc;

public enum Direction {
    NORTH{
        @Override
        public Direction getOpposite() {
            return SOUTH;
        }
    },
    SOUTH{
        @Override
        public Direction getOpposite() {
            return NORTH;
        }
    },
    EAST{
        @Override
        public Direction getOpposite() {
            return WEST;
        }
    },
    WEST{
        @Override
        public Direction getOpposite() {
            return EAST;
        }
    };

    public Direction getOpposite(){
        return null;
    }
}

package agh.ics.oop;

import java.util.Random;

public enum MapDirection {
    NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST;
    private static final int SIZE = 8;
    public String toString() {
        return switch (this) {
            case NORTH_EAST -> "Północny Wschód";
            case EAST -> "Wschód";
            case WEST -> "Zachód";
            case NORTH -> "Północ";
            case SOUTH_EAST -> "Południowy Wschód";
            case SOUTH -> "Południe";
            case SOUTH_WEST -> "Południowy zachód";
            case NORTH_WEST -> "Północny zachód";
        };
    }

    public MapDirection next(){
        return switch (this) {
            case NORTH -> NORTH_EAST;
            case NORTH_EAST -> EAST;
            case EAST -> SOUTH_EAST;
            case SOUTH_EAST -> SOUTH;
            case SOUTH -> SOUTH_WEST;
            case SOUTH_WEST -> WEST;
            case WEST -> NORTH_WEST;
            case NORTH_WEST -> NORTH;
        };
    }

    public MapDirection previous(){
        return switch (this) {
            case NORTH -> NORTH_WEST;
            case NORTH_WEST -> WEST;
            case WEST -> SOUTH_WEST;
            case SOUTH_WEST -> SOUTH;
            case SOUTH -> SOUTH_EAST;
            case SOUTH_EAST -> EAST;
            case EAST -> NORTH_EAST;
            case NORTH_EAST -> NORTH;
        };
    }

    public static MapDirection asignRandomDirection(){ //https://kodejava.org/how-do-i-pick-a-random-value-from-an-enum
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }

    public Vector2d toUnitVector(){
        return switch (this) {
            case NORTH -> new Vector2d(0,1);
            case NORTH_EAST -> new Vector2d(1,1);
            case EAST ->  new Vector2d(1,0);
            case SOUTH_EAST -> new Vector2d(1,-1);
            case SOUTH -> new Vector2d(0,-1);
            case SOUTH_WEST -> new Vector2d(-1,-1);
            case WEST ->  new Vector2d(-1,0);
            case NORTH_WEST -> new Vector2d(-1,1);
        };
    }
}

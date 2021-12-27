package agh.ics.oop;

import java.util.Random;

enum MapDirection {
    NORTH,
    SOUTH,
    WEST,
    EAST,
    NORTHEAST,
    SOUTHEAST,
    SOUTHWEST,
    NORTHWEST;

    public MapDirection next(){
        return switch (this) {
            case NORTH -> NORTHEAST;
            case EAST -> SOUTHEAST;
            case SOUTH -> SOUTHWEST;
            case WEST -> NORTHWEST;
            case NORTHEAST -> EAST;
            case SOUTHEAST -> SOUTH;
            case SOUTHWEST -> WEST;
            case NORTHWEST -> NORTH;
        };
    }

    public Vector2d toUnitVector(){
        return switch (this) {
            case NORTH -> new Vector2d(0, 1);
            case EAST -> new Vector2d(1, 0);
            case SOUTH -> new Vector2d(0, -1);
            case WEST -> new Vector2d(-1, 0);
            case NORTHEAST -> new Vector2d(1, 1);
            case SOUTHEAST -> new Vector2d(1, -1);
            case SOUTHWEST -> new Vector2d(-1, -1);
            case NORTHWEST -> new Vector2d(-1, 1);
        };
    }

    public static MapDirection getRandomOrientation(){
        return values()[new Random().nextInt(values().length)];
    }
}

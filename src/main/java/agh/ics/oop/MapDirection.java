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

    public MapDirection previous(){
        return switch (this) {
            case NORTH -> NORTHWEST;
            case EAST -> NORTHEAST;
            case SOUTH -> SOUTHEAST;
            case WEST -> SOUTHWEST;
            case NORTHEAST -> NORTH;
            case SOUTHEAST -> EAST;
            case SOUTHWEST -> SOUTH;
            case NORTHWEST -> WEST;
        };
    }
//
//    public MapDirection next_90(){
//        return switch (this) {
//            case NORTH -> EAST;
//            case EAST -> SOUTH;
//            case SOUTH -> WEST;
//            case WEST -> NORTH;
//            case NORTHEAST -> SOUTHEAST;
//            case SOUTHEAST -> SOUTHWEST;
//            case SOUTHWEST -> NORTHWEST;
//            case NORTHWEST -> NORTHEAST;
//        };
//    }
//
//    public MapDirection previous_90(){
//        return switch (this) {
//            case NORTH -> WEST;
//            case EAST -> NORTH;
//            case SOUTH -> EAST;
//            case WEST -> SOUTH;
//            case NORTHEAST -> NORTHWEST;
//            case SOUTHEAST -> NORTHEAST;
//            case SOUTHWEST -> SOUTHEAST;
//            case NORTHWEST -> SOUTHWEST;
//        };
//    }

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

    public static MapDirection randomOrientation(){
        return values()[new Random().nextInt(values().length)];
    }
}

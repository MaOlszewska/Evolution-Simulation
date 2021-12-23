package agh.ics.oop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MapDirectionTest {
    @Test
    public void nextTest(){
        assertEquals(MapDirection.SOUTHEAST.next(), MapDirection.SOUTH);
        assertEquals(MapDirection.NORTH.next(), MapDirection.NORTHEAST);
    }

    @Test
    public void toUniVectorTest(){
        assertEquals(MapDirection.SOUTHEAST.toUnitVector(), new Vector2d(1, -1));
        assertEquals(MapDirection.NORTHWEST.toUnitVector(), new Vector2d(-1, 1));
    }

    @Test
    public void randomOrientationTest(){
        MapDirection direction = MapDirection.randomOrientation();
        assertTrue(direction.toUnitVector().x >= -1 && direction.toUnitVector().x <=1);
        assertTrue(direction.toUnitVector().y >= -1 && direction.toUnitVector().y <=1);
    }
}

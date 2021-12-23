package agh.ics.oop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Vector2dTest {
    Vector2d Vector =  new Vector2d(1,1);
    Vector2d Vector1 =  new Vector2d(0,0);
    Vector2d Vector2 =  new Vector2d(-5,2);
    Vector2d Vector3 =  new Vector2d(1,1);

    @Test
    public void hashCodeTest(){
        assertEquals(Vector.hashCode(), Vector3.hashCode());
    }

    @Test
    public void precedesTest(){
        assertTrue(Vector1.precedes(Vector));
        assertFalse(Vector2.precedes(Vector3));
    }

    @Test
    public void followsTest(){
        assertTrue(Vector.follows(Vector1));
        assertFalse(Vector2.follows(Vector3));
    }

    @Test
    public void addTest(){
        assertEquals(Vector.add(Vector2), new Vector2d(-4,3));
        assertEquals(Vector3.add(Vector1), Vector3);
    }
}

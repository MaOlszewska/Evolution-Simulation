package agh.ics.oop;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class MapTest {

    @Test
    public void placeTest(){
        AbstractWorldMap map = new RightMap(5, 5, 0.3f,10,false);
        Animal animal = new Animal(new Vector2d(1,1),10,map,new AnimalGenes());
        map.place(animal);
        assertTrue(map.isOccupied(animal.getPosition()));
        assertTrue(map.objectAt(animal.getPosition()) instanceof Animal);
    }

    @Test
    public void isOccupiedTest(){
        AbstractWorldMap map = new RightMap(5, 5, 0.3f,10,false);
        Animal animal = new Animal(new Vector2d(1,1),10,map,new AnimalGenes());
        map.place(animal);
        assertTrue(map.isOccupied(animal.getPosition()));
        assertFalse(map.isOccupied(new Vector2d(2,2)));
        Grass grass = new Grass(new Vector2d(3,3));
        assertFalse(map.isOccupied(grass.getPosition()));
    }

    @Test
    public void objectAtTest(){
        AbstractWorldMap map = new RightMap(5, 5, 0.3f,10,false);
        Animal animal = new Animal(new Vector2d(1,1),10,map,new AnimalGenes());
        map.place(animal);
        assertTrue(map.objectAt(new Vector2d(1,1)) instanceof Animal);
        assertFalse(map.objectAt(new Vector2d(3,3)) instanceof Grass);
    }

    @Test
    public void jungleLowerLeftTest(){
        AbstractWorldMap map = new RightMap(10, 10, 0.5f,10,false);
        assertEquals(map.jungleLowerLeft(10,10,0.5f), new Vector2d(2,2));

        AbstractWorldMap map1 = new RightMap(5, 30, 0.4f,10,false);
        assertEquals(map1.jungleLowerLeft(5,30,0.6f),new Vector2d(1,13));
    }

    @Test
    public void jungleUpperRight(){
        AbstractWorldMap map = new RightMap(10, 10, 0.5f,10,false);
        assertEquals(map.jungleUpperRight(10,10,0.5f), new Vector2d(7,7));

        AbstractWorldMap map1 = new RightMap(5, 30, 0.4f,10,false);
        assertEquals(map1.jungleUpperRight(5,30,0.6f),new Vector2d(4,16));
    }

}

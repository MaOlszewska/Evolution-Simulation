package agh.ics.oop;

import org.junit.jupiter.api.Test;
import java.util.LinkedList;
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

    @Test
    public void addPriorityAnimalTest(){
        AbstractWorldMap map = new RightMap(10, 10, 0.5f,10,false);
        Animal animal1 = new Animal(new Vector2d(1,1), 15,map,new AnimalGenes());
        Animal animal2 = new Animal(new Vector2d(1,1), 10,map,new AnimalGenes());
        Animal animal3 = new Animal(new Vector2d(1,1), 20,map,new AnimalGenes());
        map.addPriorityAnimal(animal1, animal1.getPosition());
        map.addPriorityAnimal(animal2, animal2.getPosition());
        map.addPriorityAnimal(animal3, animal3.getPosition());

        assertEquals(map.animals.get(new Vector2d(1,1)).poll(),animal3);
        assertEquals(map.animals.get(new Vector2d(1,1)).poll(),animal1);
    }

    @Test
    public void getAnimalsTest(){
        AbstractWorldMap map = new RightMap(10, 10, 0.5f,10,false);
        Animal animal1 = new Animal(new Vector2d(1,1), 15,map,new AnimalGenes());
        Animal animal2 = new Animal(new Vector2d(1,1), 10,map,new AnimalGenes());
        Animal animal3 = new Animal(new Vector2d(1,1), 20,map,new AnimalGenes());
        map.addPriorityAnimal(animal1, animal1.getPosition());
        map.addPriorityAnimal(animal2, animal2.getPosition());
        map.addPriorityAnimal(animal3, animal3.getPosition());

        assertEquals(map.getAnimals().get(0), animal3);
    }

    @Test
    public void isEmptyJungleTest(){
        AbstractWorldMap map = new RightMap(3, 3, 0.5f,10,false);
        map.plantTuftInJungle();
        map.plantTuftInJungle();
        map.plantTuftInJungle();
        assertEquals(map.isEmptyPlaceInJungle(), true);
        map.plantTuftInJungle();
        assertEquals(map.isEmptyPlaceInJungle(), false);
    }


    @Test
    public void hungryAnimalsInPositionTest(){
        AbstractWorldMap map = new RightMap(10, 10, 0.5f,10,false);
        Animal animal1 = new Animal(new Vector2d(1,1), 20,map,new AnimalGenes());
        Animal animal2 = new Animal(new Vector2d(1,1), 10,map,new AnimalGenes());
        Animal animal3 = new Animal(new Vector2d(1,1), 20,map,new AnimalGenes());
        map.addPriorityAnimal(animal1, animal1.getPosition());
        map.addPriorityAnimal(animal2, animal2.getPosition());
        map.addPriorityAnimal(animal3, animal3.getPosition());
        LinkedList<Animal> testList = new LinkedList<>();
        testList.add(animal1);
        testList.add(animal3);
        assertEquals(map.hungryAnimalsAtPosition(new Vector2d(1,1)), testList );
    }

    @Test
    public void findPairTest(){
        AbstractWorldMap map = new RightMap(10, 10, 0.5f,10,false);
        Animal animal1 = new Animal(new Vector2d(1,1), 40,map,new AnimalGenes());
        Animal animal2 = new Animal(new Vector2d(1,1), 50,map,new AnimalGenes());
        Animal animal3 = new Animal(new Vector2d(1,1), 2,map,new AnimalGenes());
        map.addPriorityAnimal(animal1, animal1.getPosition());
        map.addPriorityAnimal(animal2, animal2.getPosition());
        map.addPriorityAnimal(animal3, animal3.getPosition());
        LinkedList<LinkedList<Animal>> testList = new LinkedList<>();
        LinkedList<Animal> testListAnimal = new LinkedList<>();
        testListAnimal.add(animal2);
        testListAnimal.add(animal1);
        testList.add(testListAnimal);
        assertEquals(map.findPair(5f), testList);
    }

    @Test
    public void removeAnimalFrommPositionTest(){
        AbstractWorldMap map = new RightMap(10, 10, 0.5f,10,false);
        Animal animal1 = new Animal(new Vector2d(1,1), 40,map,new AnimalGenes());
        Animal animal2 = new Animal(new Vector2d(1,1), 50,map,new AnimalGenes());
        Animal animal3 = new Animal(new Vector2d(1,1), 2,map,new AnimalGenes());
        map.addPriorityAnimal(animal1, animal1.getPosition());
        map.addPriorityAnimal(animal2, animal2.getPosition());
        map.addPriorityAnimal(animal3, animal3.getPosition());

        assertEquals(map.animals.get(new Vector2d(1,1)).peek(), animal2);
        map.removeAnimalFromPosition(animal2,animal2.getPosition());
        assertEquals(map.animals.get(new Vector2d(1,1)).peek(), animal1);
    }
}

package agh.ics.oop;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class StatisticTest {

    @Test
    public void findDominantGeonotype(){
        AbstractWorldMap map = new RightMap(5, 5, 0.3f,10,false);
        AnimalGenes genes = new AnimalGenes(new int[]{0,0,0,0,0,0,0,0,0,1,1,1,1,1,2,2,2,2,2,3,3,3,6,6,6,6,7,7,7,7,7,7});
        Animal animal = new Animal(new Vector2d(1,1),10, map,genes);

        AnimalGenes genes1 = new AnimalGenes(new int[]{0,0,0,0,0,1,1,1,1,1,1,1,1,1,2,2,2,2,6,6,6,6,6,6,6,6,6,6,6,6,6,7});
        Animal animal1 = new Animal(new Vector2d(1,1),10, map,genes1);

        AnimalGenes genes2 = new AnimalGenes(new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,3,5,5,6,6,6,6,7,7,7,7,7,7});
        Animal animal2 = new Animal(new Vector2d(1,1),10, map,genes2);

        ArrayList<Animal> animals = new ArrayList<>();
        animals.add(animal);
        animals.add(animal1);
        animals.add(animal2);
        Statistics stats = new Statistics(10,10);
        stats.findDominantGenotype(animals);
        assertEquals(stats.getDominantGenotype(), 0);
    }

    @Test
    public void counterAvgChildrenOfAliveAnimalsTest(){
        AbstractWorldMap map = new RightMap(5, 5, 0.3f,10,false);
        Animal animal = new Animal(new Vector2d(1,1),10, map,new AnimalGenes());
        Animal animal1 = new Animal(new Vector2d(1,1),10, map,new AnimalGenes());
        Animal animal2 = new Animal(new Vector2d(1,1),10, map,new AnimalGenes());

        ArrayList<Animal> animals = new ArrayList<>();
        animals.add(animal);
        animals.add(animal1);
        animals.add(animal2);
        animal.addChild();
        animal.addChild();
        animal2.addChild();
        animal2.addChild();
        animal2.addChild();
        animal1.addChild();
        Statistics stats = new Statistics(10,10);
        stats.calculateAvgChildrenOfAliveAnimals(animals);
        assertEquals(stats.getAvgChildren(),2);
    }

    @Test
    public void counterOfAvgEnergy(){
        Statistics stats = new Statistics(10,3);
        stats.calculateOfAvgEnergy(30);
        assertEquals(stats.getAvgEnergy(),10);
    }
}

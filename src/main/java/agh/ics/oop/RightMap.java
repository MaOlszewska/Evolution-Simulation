package agh.ics.oop;

import java.lang.reflect.Array;
import java.util.*;

public class RightMap extends AbstractWorldMap{  // mapa z murem

    public RightMap(int width, int height, float jungleRatio, int caloriesGrass) {
        super(width,height,jungleRatio,caloriesGrass);
    }

    @Override
    public Vector2d selectPosition(Vector2d oldPosition, MapDirection orientation) {
        return oldPosition.add(orientation.toUnitVector());
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.precedes(upperRight) && position.follows(lowerLeft);
    }

    @Override
    public void removeDeadAnimal(Animal animal) {
        allAnimals.remove(animal);
        removeAnimalFromPosition(animal, animal.getPosition());
        animal.removeObserver(this);
    }

}


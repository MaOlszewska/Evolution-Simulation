package agh.ics.oop;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class RightMap extends AbstractWorldMap implements IWorldMap{  // mapa z murem
    private Vector2d lowerLeft;
    private Vector2d upperRight;
    private Vector2d lowerLeftJungle;
    private Vector2d upperRightJungle;
    private int caloriesGrass;
    protected LinkedHashMap<Vector2d, PriorityQueue<Animal>> animals;
    protected LinkedHashMap<Vector2d, Grass> grass;

    public RightMap(int height, int width, int jungleSize, int caloriesGrass) {
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width, height);
        this.lowerLeftJungle = jungleLowerLeft(height, width, jungleSize);
        this.upperRightJungle = jungleUpperRight(height, width, jungleSize);
        this.caloriesGrass = caloriesGrass;
        this.animals = new LinkedHashMap<>();
        this.grass = new LinkedHashMap<>();
    }


    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.precedes(upperRight) && position.follows(lowerLeft);
    }

    @Override
    public boolean place(Animal animal) {
        return false;
    }


    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {

    }
}

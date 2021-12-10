package agh.ics.oop;
import java.util.ArrayList;
import java.util.Random;

public class Animal implements IMapElement, IPositionChangeObserver {
    private Vector2d position;
    private MapDirection orientation;
    private IWorldMap map;
    private ArrayList<IPositionChangeObserver> observers;
    private int energy;
    private AnimalGenes genes;


    public Animal(Vector2d initialPosition, int initialEnergy, IWorldMap map, AnimalGenes genes){
        this.orientation = MapDirection.randomOrientation();
        this.position = initialPosition;
        this.observers = new ArrayList<>();
        this.energy = initialEnergy;
        this.map = map;
        this.genes = genes;
    }

    public int getEnergy(){
        return this.energy;
    }

    public void substractEnergy(int i ){
        this.energy -= i;
    }

    public void addEnergy(int i){
        this.energy += i;
    }

    public MapDirection getOrientation(){
        return this.orientation;
    }


    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    public void addObserver(IPositionChangeObserver observer){
        this.observers.add(observer);
    }

    public void removeObserver (IPositionChangeObserver observer){
        this.observers.remove(observer);
    }

    public void move(int movement){
        Vector2d newPosition;
        switch (movement) {
            case 0 :
                newPosition = this.position.add(this.orientation.toUnitVector());
                if(this.map.canMoveTo(newPosition)){
                    Vector2d oldPosition = this.position;
                    this.position = newPosition;
                    positionChanged(oldPosition,newPosition);
                }

            case 1 :
                this.orientation = this.orientation.next();
                break;
            case 2:
                this.orientation = this.orientation.next().next();
                break;
            case 3 :
                this.orientation = this.orientation.next().next().next();
                break;
            case 4:
                newPosition = this.position.subtract(this.orientation.toUnitVector());
                if(this.map.canMoveTo(newPosition)){
                    Vector2d oldPosition = this.position;
                    this.position = newPosition;
                    positionChanged(oldPosition,newPosition);
                }

                break;
            case 5:
                this.orientation = this.orientation.next().next().next().next();
                break;
            case 6:
                this.orientation = this.orientation.next().next().next().next().next();
                break;
            case 7:
                this.orientation = this.orientation.next().next().next().next().next().next();
                break;
            default: ;

        }
    }

    @Override
    public String getPath(IMapElement object) {
        Animal animal = (Animal) object;
        MapDirection orientation = animal.orientation;
        switch (orientation) {
            case NORTH -> {return "src/main/resources/North.png";}
            case EAST -> {return "src/main/resources/East.png";}
            case SOUTH -> {return "src/main/resources/South.png";}
            case WEST -> {return "src/main/resources/WEST.png";}
            default -> {return "src/main/resources/WEST.png";}
        }
    }

    public int selectMovement(){
        return genes.selectMovemnet();
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        observers.forEach(observer -> observer.positionChanged(oldPosition,newPosition));
    }
}
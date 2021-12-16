package agh.ics.oop;

import java.lang.reflect.Array;
import java.util.*;

public class RightMap extends AbstractWorldMap implements IPositionChangeObserver, IEnergyChangeObserver{  // mapa z murem
    private Vector2d lowerLeft;
    private Vector2d upperRight;
    private Vector2d lowerLeftJungle;
    private Vector2d upperRightJungle;
    private int caloriesGrass;
//    private final LinkedHashMap<Vector2d, PriorityQueue<Animal>> animals;
//    private final LinkedHashMap<Vector2d, Grass> grass;


    public RightMap(int width, int height, float jungleRatio, int caloriesGrass) {
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width -1 , height- 1);
        this.lowerLeftJungle = jungleLowerLeft(width, height, jungleRatio);
        this.upperRightJungle = jungleUpperRight(width, height, jungleRatio);
        this.caloriesGrass = caloriesGrass;
//        this.animals = new LinkedHashMap<>();
//        this.grass = new LinkedHashMap<>();
    }

//    public boolean isOccupied(Vector2d position) {
//        return !(objectAt(position) == null) ;
//    }
    public Vector2d getLowerLeft(){return lowerLeft;}
    public Vector2d getLowerLeftJungle(){return lowerLeftJungle;}
    public Vector2d getUpperRight(){return upperRight;}
    public Vector2d getUpperRightJungle(){return upperRightJungle;}
    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.precedes(upperRight) && position.follows(lowerLeft);
    }

//    @Override
//    public Object objectAt(Vector2d position) {
//        if(grass.get(position) == null){
//            PriorityQueue <Animal> animalsOnPos = animals.get(position);
//            if(animalsOnPos == null || animalsOnPos.size() == 0) return null;
//            else return animalsOnPos.peek();
//        }
//        else {
//            return grass.get(position);
//        }
//    }

    @Override
    public LinkedList<Animal> hungryAnimalsInPosition(Vector2d position) {
        PriorityQueue<Animal> animalsOnPos = animals.get(position);
        if (animalsOnPos != null && !animalsOnPos.isEmpty()){
            LinkedList<Animal> toFeed = new LinkedList<>();
            Animal first = animalsOnPos.poll();
            toFeed.add(first);

            while (animalsOnPos.peek() != null && animalsOnPos.peek().getEnergy() == first.getEnergy()){
                toFeed.add(animalsOnPos.poll());
            }
            animalsOnPos.addAll(toFeed);
            return toFeed;
        }
        else return null;
    }

    @Override
    public void removeDeadAnimal(Animal animal) {
        allAnimals.remove(animal);
        removeAnimalFromPosition(animal, animal.getPosition());
        animal.removeObserver(this);
    }

    @Override
    public void removeEatenGrass(Grass tuft) {
        grass.remove(tuft.getPosition(),tuft);
    }

    @Override
    public void positionChanged(Animal animal,Vector2d oldPosition, Vector2d newPosition) {
        removeAnimalFromPosition(animal, oldPosition);
        addAnimalAtPosition(animal,newPosition);
    }

    @Override
    public void removeAnimalFromPosition(Animal animal, Vector2d oldPosition) {
        animals.get(oldPosition).remove(animal);
    }

    public void addAnimalAtPosition(Animal animal, Vector2d position){
        PriorityQueue<Animal> animalsOnPos = animals.get(position);
        if (animalsOnPos == null) {
            addPriorityAnimal(animal, animal.getPosition());}
        else {animalsOnPos.add(animal);}
    }


    public boolean isEmptyJungle() {
        for(int i = lowerLeftJungle.x; i <= upperRightJungle.x; i++ ) {
            for (int j = lowerLeftJungle.y; j <= upperRightJungle.y; j++) {
                if (!isOccupied(new Vector2d(i,j))) {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean isEmptySteppe() {
        for(int i = 0; i <= upperRight.x;i++ ) {
            for (int j = 0; j <= upperRight.y; j++) {
                Vector2d pos = new Vector2d(i,j);
                if((!pos.follows(lowerLeftJungle) && !pos.precedes(upperRightJungle))) {
                    if (!isOccupied(pos)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public Grass plantTuftInJungle() {
        Random random = new Random();
        Vector2d pos;
        do {
            int posX = lowerLeftJungle.x + random.nextInt(upperRightJungle.x - lowerLeftJungle.x + 1);
            int posY = lowerLeftJungle.y + random.nextInt(upperRightJungle.y - lowerLeftJungle.y + 1);
            pos = new Vector2d(posX,posY);
        }while(isOccupied(pos));
        Grass grasses = new Grass(pos);
        grass.put(pos,grasses);
        return grasses;
    }

    public Grass plantTuftInSteppe() {
        Random random = new Random();
        Vector2d pos;
        do {
            int posX =  random.nextInt(upperRight.x + 1);
            int posY =  random.nextInt(upperRight.y + 1);
            pos = new Vector2d(posX, posY);
        }while(isOccupied(pos) || (pos.follows(lowerLeftJungle) && pos.precedes(upperRightJungle)));
        Grass grasses = new Grass(pos);
        grass.put(pos,grasses);
        return grasses;
    }

    @Override
    public void energyChanged(Animal animal) {
        removeAnimalFromPosition(animal,animal.getPosition());
        addAnimalAtPosition(animal, animal.getPosition());
    }
}


package agh.ics.oop;

import java.lang.reflect.Array;
import java.util.*;

public class RightMap extends AbstractWorldMap implements IWorldMap, IPositionChangeObserver{  // mapa z murem
    private Vector2d lowerLeft;
    private Vector2d upperRight;
    private Vector2d lowerLeftJungle;
    private Vector2d upperRightJungle;
    private int caloriesGrass;
    private final LinkedHashMap<Vector2d, PriorityQueue<Animal>> animals;
    private final LinkedHashMap<Vector2d, Grass> grass;


    public RightMap(int height, int width, float jungleRatio, int caloriesGrass) {
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width -1 , height- 1);
        this.lowerLeftJungle = jungleLowerLeft(height, width, jungleRatio);
        this.upperRightJungle = jungleUpperRight(height, width, jungleRatio);
        this.caloriesGrass = caloriesGrass;
        this.animals = new LinkedHashMap<>();
        this.grass = new LinkedHashMap<>();
    }

    public boolean isOccupied(Vector2d position) {
        return !(objectAt(position) == null) ;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.precedes(upperRight) && position.follows(lowerLeft);
    }

    @Override
    public Object objectAt(Vector2d position) {
        if(grass.get(position) == null){
            PriorityQueue <Animal> animalsOnPos = animals.get(position);
            if(animalsOnPos == null || animalsOnPos.size() == 0) return null;
            else return animalsOnPos.peek();
        }
        else {
            return grass.get(position);
        }
    }

    @Override
    public void place(Animal animal) {
        animal.addObserver(this);
        Vector2d position = animal.getPosition();
        addAnimalAtPosition(animal,position);
    }

    @Override
    public void addPriotityAnimal(Animal animal, Vector2d position) {
        PriorityQueue<Animal> animalsOnPosition = animals.get(position);
        if(animalsOnPosition == null){
            PriorityQueue<Animal> animalsPriority = new PriorityQueue<>((animal1, animal2) -> animal2.getEnergy() - animal1.getEnergy());
            animalsPriority.add(animal);
            animals.put(position,animalsPriority);
        }
        else{
            animalsOnPosition.add(animal);
        }
    }

    @Override
    public LinkedList<Animal> hungryAnimalsInPosition(Vector2d position) {
        PriorityQueue<Animal> animalsOnPos = animals.get(position);
        if (animalsOnPos != null && animalsOnPos.size() >= 1){

            LinkedList<Animal> toFeed = new LinkedList<>();
            Animal first = animalsOnPos.poll();
            toFeed.add(first);

            // feed strongest animal on position - if there is more than 1, share a plant
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
        removeAnimalFromPosition(animal, animal.getPosition());
        animal.removeObserver(this);
    }

    @Override
    public void removeEatenGrass(Grass tuft) {
        grass.remove(tuft);
    }

    @Override
    public void positionChanged(Animal animal,Vector2d oldPosition, Vector2d newPosition) {
        removeAnimalFromPosition(animal, oldPosition);
        addAnimalAtPosition(animal,newPosition);
    }

    @Override
    public void removeAnimalFromPosition(Animal animal, Vector2d oldPosition) {
        PriorityQueue<Animal> animalsOnOldPos = animals.get(oldPosition);
        animalsOnOldPos.remove(animal);
    }

    public void addAnimalAtPosition(Animal animal, Vector2d position){
        PriorityQueue<Animal> animalsOnPos = animals.get(position);

        if (animalsOnPos == null) {
            PriorityQueue<Animal> newList = new PriorityQueue<>((a1,a2) -> a2.getEnergy() - a1.getEnergy());
            newList.add(animal);
            animals.put(position,newList);}
        else {animalsOnPos.add(animal);}
    }

    @Override
    public boolean isEmptyJungle() {
        for(int i = lowerLeftJungle.x; i <= upperRightJungle.x;i++ ) {
            for (int j = lowerLeftJungle.y; j <= upperRightJungle.y; j++) {
                Object object = objectAt(new Vector2d(i, j));
                if (object == null) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isEmptySteppe() {
        for(int i = lowerLeft.x; i <= upperRight.x;i++ ) {
            for (int j = lowerLeft.y; j <= upperRight.y; j++) {
                Vector2d pos = new Vector2d(i,j);
                if((!pos.follows(lowerLeftJungle) && !pos.precedes(upperRightJungle))) {
                    Object object = objectAt(pos);
                    if (object == null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Vector2d plantTuftInJungle() {
        Random random = new Random();
        Vector2d pos;
        do {
            int posX = lowerLeftJungle.x + random.nextInt(upperRightJungle.x - lowerLeftJungle.x + 1);
            int posY = lowerLeftJungle.y + random.nextInt(upperRightJungle.y - lowerLeftJungle.y + 1);
            pos = new Vector2d(posX,posY);
        }while(isOccupied(pos));
        grass.put(pos,new Grass(pos));
        return pos;
    }

    @Override
    public Vector2d plantTuftInSteppe() {
        Random random = new Random();
        Vector2d pos;
        do {
            int posX =  random.nextInt(upperRight.x + 1);
            int posY =  random.nextInt(upperRight.y + 1);
            pos = new Vector2d(posX, posY);
        }while(isOccupied(pos) || (pos.follows(lowerLeftJungle) && pos.precedes(upperRightJungle)));
        grass.put(pos,new Grass(pos));
        return pos;
    }
}


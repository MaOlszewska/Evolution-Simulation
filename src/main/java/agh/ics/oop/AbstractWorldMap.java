package agh.ics.oop;

import java.util.*;

public abstract class AbstractWorldMap implements IWorldMap,IPositionChangeObserver {

    LinkedList<Animal> allAnimals = new LinkedList<>();
    HashMap<Vector2d, PriorityQueue<Animal>> animals = new LinkedHashMap<>();
    HashMap<Vector2d, Grass> grass = new LinkedHashMap<>();

    @Override
    public void place(Animal animal) {
        allAnimals.add(animal);
        animal.addObserver(this);
        addPriorityAnimal(animal, animal.getPosition());
    }

    public void addPriorityAnimal(Animal animal, Vector2d position) {
        PriorityQueue<Animal> animalsOnPosition = animals.get(position);
        if (animalsOnPosition == null || animals.size() == 0) {
            PriorityQueue<Animal> animalsPriority = new PriorityQueue<>((animal1, animal2) -> animal2.getEnergy() - animal1.getEnergy());
            animalsPriority.add(animal);
            animals.put(position, animalsPriority);
        } else {
            animalsOnPosition.add(animal);
        }
    }

    public abstract boolean canMoveTo(Vector2d position);

    @Override
    public Object objectAt(Vector2d position) {
        if (grass.get(position) == null) {
            PriorityQueue<Animal> animalsOnPos = animals.get(position);
            if (animalsOnPos == null || animalsOnPos.size() == 0) return null;
            else return animalsOnPos.peek();
        } else {
            return grass.get(position);
        }
    }


    public Vector2d jungleLowerLeft( int width, int height,float jungleRatio) {
        int jungleSizeX = (int) (width * jungleRatio);
        //int jungleSizeY = (int) (height * jungleRatio);
        int jungleX = (width - jungleSizeX) / 2;
        int jungleY = (height - jungleSizeX) / 2;
        return new Vector2d(jungleX + 1, jungleY + 1);
    }

    public Vector2d jungleUpperRight( int width, int height, float jungleRatio) {
        int jungleSizeX = (int) (width * jungleRatio);
        //int jungleSizeY = (int) (height * jungleRatio);
        int jungleX = (width - jungleSizeX) / 2;
        int jungleY = (height - jungleSizeX) / 2;
        return new Vector2d(jungleX + jungleSizeX, jungleY + jungleSizeX);
    }

    public boolean isOccupied(Vector2d position) {
        if ((animals.get(position) == null
                || (animals.get(position).isEmpty())) && grass.get(position) == null) return false;
        return true;
    }

    public abstract Vector2d getLowerLeft();
    public abstract Vector2d getLowerLeftJungle();
    public abstract Vector2d getUpperRight();
    public abstract Vector2d getUpperRightJungle();
    public abstract void removeEatenGrass(Grass tuft);

    public abstract LinkedList<Animal> hungryAnimalsInPosition(Vector2d position);

    public abstract boolean isEmptyJungle();

    public abstract Grass plantTuftInJungle();

    public abstract boolean isEmptySteppe();

    public abstract Grass plantTuftInSteppe();

    public abstract void removeDeadAnimal(Animal animal);

    public abstract void removeAnimalFromPosition(Animal animal, Vector2d oldPosition);

    public LinkedList<LinkedList<Animal>> findPair(float energyToMove) {
        LinkedList<LinkedList<Animal>> allPairToReproduce = new LinkedList<>();
        for (Vector2d position : animals.keySet()) {
            PriorityQueue<Animal> animalsOnPos = animals.get(position);
            if (animalsOnPos.size() >= 2) {
                Object[] animalsInArray = animalsOnPos.toArray();
                Animal firstParent = (Animal) animalsInArray[0];
                Animal secondParent = (Animal) animalsInArray[1];
                if (firstParent.getEnergy() >= energyToMove && secondParent.getEnergy() >= energyToMove) {
                    LinkedList<Animal> pairParentAnimal = new LinkedList<>();
                    pairParentAnimal.add(firstParent);
                    pairParentAnimal.add(secondParent);
                    allPairToReproduce.add(pairParentAnimal);
                }
            }
        }
        return allPairToReproduce;

    }
}



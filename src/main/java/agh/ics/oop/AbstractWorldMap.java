package agh.ics.oop;

import java.util.*;

public abstract class AbstractWorldMap implements IWorldMap,IPositionChangeObserver {
    protected LinkedHashMap<Vector2d, PriorityQueue<Animal> > animals = new LinkedHashMap<>();
    protected LinkedHashMap<Vector2d, Grass> grass = new LinkedHashMap<>();


    public abstract boolean canMoveTo(Vector2d position);


    @Override
    public Object objectAt(Vector2d position) {
        return animals.get(position);  // jesli nie ma żadneog zwierzecia zwraca null
    }

    public Vector2d jungleLowerLeft(int height, int width, float jungleRatio){
        int jungleSize = (int) (width * jungleRatio);
        int jungleX = (width - jungleSize) / 2 ;
        int jungleY = (height - jungleSize) / 2 ;
        return new Vector2d(jungleX + 1,jungleY + 1 );
    }

    public Vector2d jungleUpperRight( int height, int width, float jungleRatio){
        int jungleSize = (int) (width * jungleRatio);
        int jungleX = (width - jungleSize) / 2 ;
        int jungleY = (height - jungleSize) / 2 ;
        return new Vector2d(jungleX + jungleSize,jungleY + jungleSize );
    }

    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }


    public void removeDeadAnimal(Animal animal){   // DO POPRAWY
        animals.remove(animals.get(animal.getPosition()));
        animal.removeObserver(this);
    }

    public void removeEatenGrass(Grass tuft){
        grass.remove(tuft.getPosition());
    }

    public LinkedList<Animal> hungryAnimalsInPosition(Vector2d position) {
        PriorityQueue<Animal> allHungryAnimals = animals.get(position);
        LinkedList<Animal> animalsToFeed = new LinkedList<>();
        if (allHungryAnimals == null && allHungryAnimals.size() == 0) {
            return null;
        } else {
            Animal animalToCheck = allHungryAnimals.poll(); // zwierze z najwieksza iloscią energii
            animalsToFeed.add(animalToCheck);
            Animal nextAnimalToCheck = allHungryAnimals.poll();
            while (nextAnimalToCheck.getEnergy() == animalToCheck.getEnergy() && allHungryAnimals.size() >= 1) {
                animalsToFeed.add(nextAnimalToCheck);
                nextAnimalToCheck = allHungryAnimals.poll();
            }

        }
        return animalsToFeed;
    }

    public abstract boolean isEmptyPlaceInJungle();
    public abstract void plantTuftInJungle();
    public abstract boolean isEmptyPlaceInSteppe();
    public abstract void plantTuftInSteppe();

    public LinkedList<LinkedList<Animal>> findPairOfAnimal(int startEnergy){
        LinkedList<LinkedList<Animal>> healtyAnimalsPairs = new LinkedList<>();

        for(Vector2d position : animals.keySet()){
            PriorityQueue<Animal> allAnimalsOnPosition = animals.get(position);
            LinkedList<Animal> parentAnimal = new LinkedList<>();
            if(allAnimalsOnPosition.size() >= 2){
                Animal firstAnimal = allAnimalsOnPosition.poll();
                Animal secondAnimal = allAnimalsOnPosition.poll();
                // połowa energi początkowej zwierzęcia a nie danego osobnika
                if(firstAnimal.getEnergy() > (startEnergy / 2) && secondAnimal.getEnergy() > (startEnergy / 2)){
                    parentAnimal.add(firstAnimal);
                    parentAnimal.add(secondAnimal);
                }
                healtyAnimalsPairs.add(parentAnimal);
            }
        }
        return healtyAnimalsPairs;
    }


}



package agh.ics.oop;

import java.util.*;

public abstract class AbstractWorldMap implements IWorldMap,IPositionChangeObserver {

    public abstract boolean canMoveTo(Vector2d position);

    public abstract Object objectAt(Vector2d position);

    public abstract void addPriotityAnimal(Animal animal, Vector2d position);

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

    public abstract boolean isOccupied(Vector2d position);


    public abstract void removeEatenGrass(Grass tuft);

    public abstract LinkedList<Animal> hungryAnimalsInPosition(Vector2d position);

    public abstract boolean isEmptyJungle();
    public abstract Vector2d plantTuftInJungle();
    public abstract boolean isEmptySteppe();
    public abstract Vector2d plantTuftInSteppe();

//    public LinkedList<LinkedList<Animal>> findPairOfAnimal(int startEnergy){
//
//        LinkedList<LinkedList<Animal>> healtyAnimalsPairs = new LinkedList<>();
//
//        for(Vector2d position : animals.keySet()){
//            PriorityQueue<Animal> allAnimalsOnPosition = animals.get(position);
//            LinkedList<Animal> parentAnimal = new LinkedList<>();
//            if(allAnimalsOnPosition.size() >= 2){
//                Animal firstAnimal = allAnimalsOnPosition.poll();
//                Animal secondAnimal = allAnimalsOnPosition.poll();
//                // połowa energi początkowej zwierzęcia a nie danego osobnika
//                if(firstAnimal.getEnergy() > (startEnergy / 2) && secondAnimal.getEnergy() > (startEnergy / 2)){
//                    parentAnimal.add(firstAnimal);
//                    parentAnimal.add(secondAnimal);
//                }
//                healtyAnimalsPairs.add(parentAnimal);
//            }
//        }
//        return healtyAnimalsPairs;
//    }


    public abstract void removeDeadAnimal(Animal animal);
    public abstract void removeAnimalFromPosition(Animal animal, Vector2d oldPosition);
}



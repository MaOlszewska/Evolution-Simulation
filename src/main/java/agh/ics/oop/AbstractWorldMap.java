package agh.ics.oop;

import java.util.*;

public abstract class AbstractWorldMap implements IWorldMap,IPositionChangeObserver {
    protected LinkedHashMap<Vector2d, PriorityQueue<Animal> > animals = new LinkedHashMap<>();
    protected LinkedHashMap<Vector2d, Grass> grass = new LinkedHashMap<>();
    //protected MapVisualizer mapVisualizer = new MapVisualizer(this);
    //protected ArrayList<IMapElement> mapElements = new ArrayList<>();
    //protected MapBoundary boundary = new MapBoundary(this);


    public abstract boolean canMoveTo(Vector2d position);


    @Override
    public Object objectAt(Vector2d position) {
        return animals.get(position);  // jesli nie ma żadneog zwierzecia zwraca null
    }
//    @Override
//    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
//        Animal animal = animals.get(oldPosition);
//        animals.remove(oldPosition);
//        animals.put(newPosition, animal);
//    }

    public Vector2d jungleLowerLeft(int height, int width, int jungleSize){
        int jungleX = (width - jungleSize) / 2 ;
        int jungleY = (height - jungleSize) / 2 ;
        return new Vector2d(jungleX + 1,jungleY + 1 );
    }

    public Vector2d jungleUpperRight( int height, int width, int jungleSize){
        int jungleX = (width - jungleSize) / 2 ;
        int jungleY = (height - jungleSize) / 2 ;
        return new Vector2d(jungleX + jungleSize,jungleY + jungleSize );
    }

    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    public void placeAnimal(Animal animal) {
        animal.addObserver(this);
        addPriotityAnimal(animal, animal.getPosition());
    }

    public void addPriotityAnimal(Animal animal, Vector2d position){
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

//    public void placeGrass(Grass tuft){
//        Vector2d position = tuft.getPosition();
//        if(!isOccupied(position) ){
//            grass.put(position, tuft);
//        }
//    }
//    public String toString() {
//        return mapVisualizer.draw(getLeftCorner(), getRightCorner());
//    }
//    public ArrayList<IMapElement> getMapElements() {
//        return mapElements;
//    }
//   // public MapBoundary getMapBoundary() {
//        return boundary;
//    }

    public void removeDeadAnimal(Animal animal){   // DO POPRAWY
        animals.remove(animals.get(animal.getPosition()));
        animal.removeObserver(this);
    }

    public void removeEatenGrass(Grass tuft){
        grass.remove(tuft.getPosition());
    }
    public LinkedList<Animal> hungryAnimalsInPosition(Vector2d position){
        PriorityQueue<Animal> allHungryAnimals = animals.get(position);
        LinkedList<Animal> animalsToFeed = new LinkedList<>();
        if(allHungryAnimals == null && allHungryAnimals.size() == 0) {
            return null;
        }
        else{
            Animal animalToCheck = allHungryAnimals.poll(); // zwierze z najwieksza iloscią energii
            animalsToFeed.add(animalToCheck);
            Animal nextAnimalToCheck = allHungryAnimals.poll();
            while(nextAnimalToCheck.getEnergy() == animalToCheck.getEnergy() && allHungryAnimals.size() >= 1){
                animalsToFeed.add(nextAnimalToCheck);
                nextAnimalToCheck = allHungryAnimals.poll();
            }

        }
        return animalsToFeed;
    }
}



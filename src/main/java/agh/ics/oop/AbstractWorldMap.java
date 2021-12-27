package agh.ics.oop;

import java.util.*;

public abstract class AbstractWorldMap implements IWorldMap,IPositionChangeObserver {

    LinkedList<Animal> allAnimals = new LinkedList<>();
    HashMap<Vector2d, PriorityQueue<Animal>> animals = new LinkedHashMap<>();
    HashMap<Vector2d, Grass> grass = new LinkedHashMap<>();
    protected Vector2d lowerLeft;
    protected Vector2d upperRight;
    protected Vector2d lowerLeftJungle;
    protected Vector2d upperRightJungle;
    protected int caloriesGrass;
    protected int height;
    protected int width;
    protected boolean magic;

    public AbstractWorldMap(int width, int height, float jungleRatio, int caloriesGrass, boolean isMagic ) {
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width - 1 , height - 1);
        this.lowerLeftJungle = jungleLowerLeft(width, height, jungleRatio);
        this.upperRightJungle = jungleUpperRight(width, height, jungleRatio);
        this.caloriesGrass = caloriesGrass;
        this.height = height;
        this.width = width;
        this.magic = isMagic;
    }

    public abstract boolean canMoveTo(Vector2d position);

    public abstract Vector2d selectPosition(Vector2d oldPosition, MapDirection orientation);

    public int getHeight(){return this.height;}

    public int getWidth(){return this.width;}

    public Vector2d getUpperRightJungle(){return upperRightJungle;}

    public Vector2d getLowerLeftJungle(){return lowerLeftJungle;}

    public Vector2d getUpperRight(){return upperRight;}

    public ArrayList getAnimals() {
        ArrayList allAnimals = new ArrayList();
        for (Vector2d position : animals.keySet()) {
            PriorityQueue<Animal> animalsOnPos = animals.get(position);
            if(animalsOnPos.size() != 0){
                allAnimals.add(animalsOnPos.peek());
            }
        }
        return allAnimals;
    }

    public void positionChanged(Animal animal,Vector2d oldPosition, Vector2d newPosition) {
        removeAnimalFromPosition(animal, oldPosition);
        addAnimalAtPosition(animal,newPosition);
    }

    public void removeDeadAnimal(Animal animal) {
        allAnimals.remove(animal);
        removeAnimalFromPosition(animal, animal.getPosition());
        animal.removeObserver(this);
    }

    public void addAnimalAtPosition(Animal animal, Vector2d position){
        PriorityQueue<Animal> animalsOnPos = animals.get(position);
        if (animalsOnPos == null) {
            addPriorityAnimal(animal, animal.getPosition());}
        else {animalsOnPos.add(animal);}
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


    @Override
    public void place(Animal animal) {
        allAnimals.add(animal);
        animal.addObserver(this);
        addPriorityAnimal(animal, animal.getPosition());
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return (animals.get(position) != null
                && (!animals.get(position).isEmpty())) || grass.get(position) != null;
    }

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
        int jungleSizeX;
        if(width <= height)  jungleSizeX = (int) (width * jungleRatio);
        else jungleSizeX = (int) (height * jungleRatio);
        int jungleX = (width - jungleSizeX) / 2;
        int jungleY = (height - jungleSizeX) / 2;
        return new Vector2d(jungleX , jungleY);
    }

    public Vector2d jungleUpperRight( int width, int height, float jungleRatio) {
        int jungleSizeX;
        if(width <= height)  jungleSizeX = (int) (width * jungleRatio);
        else jungleSizeX = (int) (height * jungleRatio);
        int jungleX = (width - jungleSizeX) / 2;
        int jungleY = (height - jungleSizeX) / 2;
        return new Vector2d(jungleX + jungleSizeX, jungleY + jungleSizeX);
    }

    public void removeAnimalFromPosition(Animal animal, Vector2d oldPosition) {animals.get(oldPosition).remove(animal);}

    public void removeEatenGrass(Grass tuft) {grass.remove(tuft.getPosition(),tuft);}

    public boolean isEmptyPlaceInJungle() {
        for(int i = lowerLeftJungle.x; i <= upperRightJungle.x; i++ ) {
            for (int j = lowerLeftJungle.y; j <= upperRightJungle.y; j++) {
                if (!isOccupied(new Vector2d(i,j))) {
                    return true;
                }
            }
        }
        return false;
    }

    public Grass plantTuftInJungle() {
        Random random = new Random();
        Vector2d position;
        do {
            int positionX = lowerLeftJungle.x + random.nextInt(upperRightJungle.x - lowerLeftJungle.x + 1);
            int positionY = lowerLeftJungle.y + random.nextInt(upperRightJungle.y - lowerLeftJungle.y + 1);
            position = new Vector2d(positionX, positionY);
        }while(isOccupied(position));
        Grass grasses = new Grass(position);
        grass.put(position,grasses);
        return grasses;
    }

    public boolean isEmptyPlaceInSteppe() {
        for(int i = 0; i <= upperRight.x; i++ ) {
            for (int j = 0; j <= upperRight.y; j++) {
                Vector2d position = new Vector2d(i, j);
                if((!position.follows(lowerLeftJungle) && !position.precedes(upperRightJungle))) {
                    if (!isOccupied(position)) {
                        return true;
                    }
                }
            }
        }
        return false;
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

    public LinkedList<Animal> hungryAnimalsAtPosition(Vector2d position) {
        PriorityQueue<Animal> animalsAtPosition = animals.get(position);
        if (animalsAtPosition != null && !animalsAtPosition.isEmpty()){
            LinkedList<Animal> animalsToFeed = new LinkedList<>();
            Animal first = animalsAtPosition.poll();
            animalsToFeed.add(first);
            while (animalsAtPosition.peek() != null && animalsAtPosition.peek().getEnergy() == first.getEnergy()){
                animalsToFeed.add(animalsAtPosition.poll());
            }
            animalsAtPosition.addAll(animalsToFeed);
            return animalsToFeed;
        }
        else return null;
    }
}



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

    public int getHeight(){return this.height;}
    public int getWidth(){return this.width;}
    public abstract Vector2d selectPosition(Vector2d oldPosition, MapDirection orientation);

    public void positionChanged(Animal animal,Vector2d oldPosition, Vector2d newPosition) {
        removeAnimalFromPosition(animal, oldPosition);
        addAnimalAtPosition(animal,newPosition);
    }

    public void addAnimalAtPosition(Animal animal, Vector2d position){
        PriorityQueue<Animal> animalsOnPos = animals.get(position);
        if (animalsOnPos == null) {
            addPriorityAnimal(animal, animal.getPosition());}
        else {animalsOnPos.add(animal);}
    }

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
        int jungleX = (width - jungleSizeX) / 2;
        int jungleY = (height - jungleSizeX) / 2;
        return new Vector2d(jungleX , jungleY);
    }

    public Vector2d jungleUpperRight( int width, int height, float jungleRatio) {
        int jungleSizeX = (int) (width * jungleRatio);
        int jungleX = (width - jungleSizeX) / 2;
        int jungleY = (height - jungleSizeX) / 2;
        return new Vector2d(jungleX + jungleSizeX, jungleY + jungleSizeX);
    }

    public boolean isOccupied(Vector2d position) {
        if ((animals.get(position) == null
                || (animals.get(position).isEmpty())) && grass.get(position) == null) return false;
        return true;
    }

    public Vector2d getLowerLeft(){return lowerLeft;}
    public Vector2d getLowerLeftJungle(){return lowerLeftJungle;}
    public Vector2d getUpperRight(){return upperRight;}
    public Vector2d getUpperRightJungle(){return upperRightJungle;}

    public void removeEatenGrass(Grass tuft) {
        grass.remove(tuft.getPosition(),tuft);
    }

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

    public abstract void removeDeadAnimal(Animal animal);

    public void removeAnimalFromPosition(Animal animal, Vector2d oldPosition) {
        animals.get(oldPosition).remove(animal);
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
}



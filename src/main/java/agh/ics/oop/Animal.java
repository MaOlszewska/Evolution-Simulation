package agh.ics.oop;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Animal {
    private Vector2d position;
    private MapDirection orientation;
    private final AbstractWorldMap map;
    private int energy;
    private final AnimalGenes genes;
    private int numberOfChildren;
    private int numberOfDays;
    private final ArrayList<IPositionChangeObserver> observers;
    private final int initialEnergy;
    private boolean ifTracked;


    public Animal(Vector2d initialPosition, int initialEnergy, AbstractWorldMap map, AnimalGenes genes){
        this.orientation = MapDirection.getRandomOrientation();
        this.position = initialPosition;
        this.energy = initialEnergy;
        this.map = map;
        this.genes = genes;
        this.numberOfChildren = 0;
        this.numberOfDays = 0;
        this.observers = new ArrayList<>();
        this.initialEnergy = initialEnergy;
        this.ifTracked = false;
    }

    public void addOneDay(){this.numberOfDays += 1;}

    public int getNumberOfDays(){return numberOfDays;}

    public void addChild(){this.numberOfChildren += 1;}

    public int getNumberOfChildren(){return numberOfChildren;}

    public Vector2d getPosition() {return this.position;}

    public int getEnergy(){return this.energy;}

    public AnimalGenes getGenes(){return this.genes;}

    public void subtractEnergy(int i ){this.energy -= i;}

    public void addEnergy(int i){this.energy += i;}

    public void addObserver(IPositionChangeObserver observer){this.observers.add(observer);}

    public void removeObserver (IPositionChangeObserver observer){this.observers.remove(observer);}

    public int findDominantGenotype(){
        int[] counter = new int[8];
        int[] gen = this.getGenes().getGenes();
        for(int i = 0; i< 32; i++){counter[gen[i]] += 1;}
        int dominantGenotype = 0;
        int maxGenotype =0;
        for(int i = 0; i < 8; i++ ){
            if(counter[i] > maxGenotype){
                maxGenotype = counter[i];
                dominantGenotype = i;
            }
        }
        return dominantGenotype;
    }

    public void move(int movement){
        Vector2d newPosition;
        switch (movement) {
            case 0 :
                newPosition = map.selectPosition(this.position, this.orientation);
                if(this.map.canMoveTo(newPosition)){
                    Vector2d oldPosition = this.position;
                    this.position = newPosition;
                    positionChanged(oldPosition,newPosition);
                }
            case 4:
                newPosition = map.selectPosition(this.position,this.orientation);
                if(this.map.canMoveTo(newPosition)){
                    Vector2d oldPosition = this.position;
                    this.position = newPosition;
                    positionChanged(oldPosition,newPosition);
                }
                break;
            default: rotate(movement);
        }
    }

    private void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        for (IPositionChangeObserver observer : this.observers){observer.positionChanged(this, oldPosition, newPosition);}
    }

    private void rotate(int movement){
        for(int i = 1; i <= movement; i++ ){if(i != 4){this.orientation = this.orientation.next();}}
    }

    public int getImage(){
        if(this.energy >= this.initialEnergy / 4 * 3 && ifTracked) return 8;
        else if(this.energy >= this.initialEnergy / 4 * 2 && ifTracked) return  7;
        else if(this.energy >= this.initialEnergy / 4 && ifTracked) return 6;
        else if(ifTracked) return 5;
        if(this.energy >= this.initialEnergy / 4 * 3 ) return 4;
        else if(this.energy >= this.initialEnergy / 4 * 2) return  3;
        else if(this.energy >= this.initialEnergy / 4) return 2;
        else return 1;
    }

    public int selectMovement(){return genes.selectMovement();}

    public Animal newBornAnimal(Animal parent){
        int newBornEnergy = this.getEnergy()/ 4 + parent.getEnergy()/ 4;
        Vector2d newBornPosition = this.getPosition();
        AnimalGenes newBornGenes = createNewBornGenes(parent);
        this.subtractEnergy(this.getEnergy()/ 4);
        parent.subtractEnergy(parent.getEnergy()/ 4);
        parent.addChild();
        Animal child = new Animal( newBornPosition,newBornEnergy, map, newBornGenes);
        this.addChild();
        return child;
    }

    public AnimalGenes createNewBornGenes(Animal secondParent) {
        int[] newBornGenes;
        int firstAnimalEnergy = this.getEnergy();
        int secondAnimalEnergy = secondParent.getEnergy();
        int[] firstAnimalGenes = this.genes.getGenes();
        int[] secondAnimalGenes = secondParent.genes.getGenes();
        int div = (int) ((((float)(firstAnimalEnergy)/(firstAnimalEnergy + secondAnimalEnergy))) * 32 - 1);

        // true-left false-right
        Random random = new Random(); // true-left false-right
        if(random.nextBoolean()){newBornGenes = leftSide(div, secondAnimalGenes, firstAnimalGenes);}
        else{newBornGenes = rightSide(div, secondAnimalGenes, firstAnimalGenes);}
        Arrays.sort(newBornGenes);
        return new AnimalGenes(newBornGenes);
    }

    private int[] rightSide(int div, int[] firstAnimalGenes, int[] secondAnimalGenes ){
        int[] newBornGenes = new int[32];
        if (32 - (32 - div - 1) >= 0) System.arraycopy(secondAnimalGenes, 32 - div - 1, newBornGenes, 32 - div - 1, 32 - (32 - div - 1));
        if (32 - div - 1 >= 0) System.arraycopy(firstAnimalGenes, 0, newBornGenes, 0, 32 - div - 1);
        return newBornGenes;
    }
    private int[] leftSide(int div, int[] firstAnimalGenes, int[] secondAnimalGenes){
        int[] newBornGenes = new int[32];
        if (div + 1 >= 0) System.arraycopy(secondAnimalGenes, 0, newBornGenes, 0, div + 1);
        if (32 - (div + 1) >= 0) System.arraycopy(firstAnimalGenes, div + 1, newBornGenes, div + 1, 32 - (div + 1));
        return newBornGenes;
    }

    public void addStatusTracked(){this.ifTracked = true;}

    public void removeAnimalStatusTracked(){this.ifTracked = false;}


    public boolean ifTracked(){return this.ifTracked;}

}

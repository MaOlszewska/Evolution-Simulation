package agh.ics.oop;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Animal implements IMapElement, IPositionChangeObserver {
    private Vector2d position;
    private MapDirection orientation;
    private final IWorldMap map;
    private final ArrayList<IPositionChangeObserver> observers;
    private int energy;
    private final AnimalGenes genes;



    public Animal(Vector2d initialPosition, int initialEnergy, IWorldMap map, AnimalGenes genes){
        this.orientation = MapDirection.randomOrientation();
        this.position = initialPosition;
        this.observers = new ArrayList<>();
        this.energy = initialEnergy;
        this.map = map;
        this.genes = genes;
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    public int getEnergy(){
        return this.energy;
    }

    public AnimalGenes getGenes(){return this.genes;}

    public void substractEnergy(int i ){
        this.energy -= i;
    }

    public void addEnergy(int i){
        this.energy += i;
    }


    public void addObserver(IPositionChangeObserver observer){
        this.observers.add(observer);
    }

    public void removeObserver (IPositionChangeObserver observer){
        this.observers.remove(observer);
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        observers.forEach(observer -> observer.positionChanged(oldPosition,newPosition));
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

    public Animal newBornAnimal( Animal dad){
        int newBornEnergy = this.getEnergy()/4 + dad.getEnergy()/4;
        Vector2d newBornPosition = this.getPosition();
        AnimalGenes newBornGenes = createNewBornGenes(dad);
        return new Animal( newBornPosition,newBornEnergy, map, newBornGenes);

    }

    private AnimalGenes createNewBornGenes(Animal dad) {
        int[] newBornGenes = new int[32];
        int momEnergy = this.getEnergy();
        int dadEnergy = dad.getEnergy();
        int[] momGenes = this.genes.getGenes();
        int[] dadGenes = dad.genes.getGenes();
        System.out.println(momEnergy);
        System.out.println(dadEnergy);

        if(momEnergy >= dadEnergy){ // dziecko dostaje wiecej genów mamy
            Random random = new Random();// true-lewa false-prawa
            int div = (int) ((((float)(momEnergy)/(momEnergy + dadEnergy))) * 32 - 1);
            if(random.nextBoolean()){ // wylosowano stronę lewa
                for(int i = 0  ; i <= div; i++){newBornGenes[i] = momGenes[i];}
                for(int i = div + 1; i < 32; i++){newBornGenes[i] = dadGenes[i];}
            }
            //WYlosowano stronę prawą
            else{
                for(int i = 32 - div - 1; i < 32; i++){newBornGenes[i] = momGenes[i];}
                for(int i = 0; i < 32 - div -1; i++){newBornGenes[i] = dadGenes[i];}
            }
        }
        else{
            Random random = new Random();// true-lewa false-prawa
            int div = (int) ((((float)(momEnergy)/(momEnergy + dadEnergy))) * 32 - 1);
            if(random.nextBoolean()){ // wylosowano stronę lewa
                for(int i = 0  ; i <= div; i++){newBornGenes[i] = dadGenes[i];}
                for(int i = div + 1; i < 32; i++){newBornGenes[i] = momGenes[i];}
            }
            //WYlosowano stronę prawą
            else{
                for(int i = 32 - div - 1; i < 32; i++){newBornGenes[i] = dadGenes[i];}
                for(int i = 0; i < 32 - div - 1; i++){newBornGenes[i] = momGenes[i];}
            }
        }
        this.substractEnergy(this.getEnergy()/4);
        dad.substractEnergy(dad.getEnergy()/4);
        Arrays.sort(newBornGenes);
        return new AnimalGenes(newBornGenes);
    }
}
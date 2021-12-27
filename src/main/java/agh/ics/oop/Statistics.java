package agh.ics.oop;

import java.util.ArrayList;

public class Statistics {
    private int numberOfAliveAnimals;
    private int numberOfGrass;
    private int numberOfDeadAnimals;
    private int worldDays;
    private int avgLifeDaysOfDeadAnimal;
    private int avgEnergy;
    private int sumOfDaysDeadAnimals;
    private int avgChildren;
    private int dominantGenotype;

    public Statistics(int startEnergy, int numberOfAliveAnimals) {
        this.worldDays = 0;
        this.numberOfAliveAnimals = numberOfAliveAnimals;
        this.numberOfGrass = 0;
        this.numberOfDeadAnimals = 0;
        this.avgEnergy = startEnergy;
        this.avgLifeDaysOfDeadAnimal = 0;
        this.sumOfDaysDeadAnimals = 0;
        this.avgChildren = 0;
        this.dominantGenotype = 0;
    }
    public int getWorldDays(){ return  this.worldDays;}
    public int getNumberOfGrass(){return this.numberOfGrass;}
    public int getNumberOfAliveAnimals(){return this.numberOfAliveAnimals;}
    public int getAvgEnergy(){return  this.avgEnergy;}
    public int getAvgLifeDaysOfDeadAnimal(){return this.avgLifeDaysOfDeadAnimal;}
    public int getNumberOfDeadAnimals(){return this.numberOfDeadAnimals;}
    public int getAvgChildren(){return this.avgChildren;}
    public int getDominantGenotype(){return this.dominantGenotype;}

    public void addDay(){this.worldDays += 1;}

    public void addLiveAnimal(){this.numberOfAliveAnimals += 1;}

    public void addDeadAnimal(){
        this.numberOfDeadAnimals += 1;
        this.numberOfAliveAnimals -= 1;
    }
    public void addDaysDeadAnimal(int days){this.sumOfDaysDeadAnimals += days;}

    public void addGrass(){this.numberOfGrass += 1;}

    public void subtractGrass(){this.numberOfGrass -= 1;}

    public void calculateOfAvgEnergy(int energy){if(numberOfAliveAnimals != 0)this.avgEnergy = energy / this.numberOfAliveAnimals;}
    
    public void calculateAvgChildrenOfAliveAnimals(ArrayList<Animal> animals){
        int numberOfChildren = 0;
        if(animals.size() == 0){this.avgChildren = 0;}
        else{
            for(Animal animal : animals) {
                if (animal.getNumberOfChildren() > 0) {
                    numberOfChildren += animal.getNumberOfChildren();
                }
            }
            this.avgChildren =  numberOfChildren / animals.size();
        }
    }

    public void calculateAvgLifeDaysDeadAnimals(){
        if(numberOfDeadAnimals != 0) {
            this.avgLifeDaysOfDeadAnimal =  this.sumOfDaysDeadAnimals / this.numberOfDeadAnimals;}
    }

    public void findDominantGenotype(ArrayList<Animal> animals){
        int[] counter = new int[8];
        for(Animal animal : animals){
            counter[animal.findDominantGenotype()] += 1;
        }
        int dominant = 0;
        int maxGenotype =0;
        for(int i = 0; i < 8; i++ ){
            if(counter[i] > maxGenotype){
                maxGenotype = counter[i];
                dominant = i;
            }
        }
        this.dominantGenotype = dominant;
    }
}


package agh.ics.oop;


import agh.ics.oop.gui.App;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Simulation implements Runnable{
    private final AbstractWorldMap map;
    private final Parameters parameters;
    private ArrayList<Animal> animals;
    private ArrayList<Grass> grass;
    private Statistics statistics;
    private int energyToMove;
    private App app;

    public Simulation(Parameters parameters, App applic){
        this.map = new RightMap(parameters.getHeight(),parameters.getHeight(),parameters.getJungleSize(), parameters.getCaloriesGrass());
        this.parameters = parameters;
        this.animals = new ArrayList<>();
        this.grass = new ArrayList<>();
        this.energyToMove = parameters.getEnergyToMove();
        this.statistics = new Statistics(parameters.getStartEnergy(), parameters.getNumberOfAnimals());
        this.app = applic;
        placeAnimalsFirstTime(parameters.getNumberOfAnimals());
    }

    public ArrayList<Animal> getAnimals(){return this.animals;}

    public ArrayList<Grass> getGrass() {return this.grass;}

    private void placeAnimalsFirstTime(int animalNumber){  //place the first animals in random places on the map
        Vector2d position;
        Random random = new Random();
        int x ;
        int y ;
        for(int i = 0; i < animalNumber; i++){
            do{
                x = random.nextInt(parameters.getWidth());
                y = random.nextInt(parameters.getHeight());
                position = new Vector2d(x,y);
            }
            while(map.isOccupied(position) && (map.objectAt(position)) instanceof Animal);
            AnimalGenes genes = new AnimalGenes();
            Animal animal = new Animal(position, parameters.getStartEnergy(), map, genes);
            animals.add(animal);
            map.place(animal);
        }
    }

//    public void anotherDay() {
//        while (animals.size() > 0) {
//            removeDeadAnimals();
//            animalsMove();
//            plantTuft();
//            consumptionGrass();
//            //animalReproduction();
//            statistics.addOneDay();
//            app.drawMap();
//        }
//    }

    public void run(){
        while (animals.size() > 0) {
            try {
                Thread.sleep(400);
                removeDeadAnimals();
                animalsMove();
                plantTuft();
                consumptionGrass();
                //animalReproduction();
                statistics.addOneDay();
                app.drawMap();
            } catch (InterruptedException ex) {
                System.out.println(ex.toString());
            }
        }
    }

    private void plantTuft(){
        if(map.isEmptyJungle()){
            Vector2d tuft = map.plantTuftInJungle();
            grass.add(new Grass(tuft));
            statistics.addOneGrass();
        }
        if(map.isEmptySteppe()){
            Vector2d tuft = map.plantTuftInSteppe();
            grass.add(new Grass(tuft));
            statistics.addOneGrass();
        }

    }
    private void animalsMove(){    // rotating or moving animals according to their genes
        int allEnergy = 0;
        for(Animal animal : animals){
            int movement = animal.selectMovement();
            animal.move(movement);
            animal.substractEnergy(energyToMove);
            allEnergy += animal.getEnergy();
            animal.addOneDay();
        }
        //statistics.CounterOfAvgEnergy(allEnergy);
    }
    private void removeDeadAnimals() {  // remove animals if have not enough energy
        ArrayList<Animal> animalToRemove = new ArrayList<>();
        for (Animal animal : animals) {
            if (animal.getEnergy() <= 0) {
                animalToRemove.add(animal);
                statistics.addOneDeadAnimal();
                statistics.addDaysDeadAnimal(animal.getNumberOfDays());
            }
        }
        for (Animal animal : animalToRemove) {
            animals.remove(animal);
            map.removeDeadAnimal(animal);
        }
    }

    // if there is more than one animal i one field, the strongest animal recevie all energy but if there are several animals
    // that have the same amount of energy, taht energy is shared between them

    private void consumptionGrass() {
        LinkedList<Grass> grassToRemove = new LinkedList<>();
        for (Grass tuft : grass) {
            LinkedList<Animal> animalsOnPosition = map.hungryAnimalsInPosition(tuft.getPosition());
            if (animalsOnPosition != null) {

                for (Animal animal : animalsOnPosition) {
                    int calories = parameters.getCaloriesGrass();
                    animal.addEnergy(calories / animalsOnPosition.size());
                }
                grassToRemove.add(tuft);
            }
        }

        for (Grass tuft : grassToRemove) {
            grass.remove(tuft);
            map.removeEatenGrass(tuft);
            statistics.substractOneGrass();
        }
        System.out.println(statistics.getNumberOfGrass());
    }

//    private void animalReproduction(){
//        LinkedList<LinkedList<Animal>> pairsOfAnimal = map.findPairOfAnimal(this.parameters.getStartEnergy());
//        for(LinkedList<Animal> parents : pairsOfAnimal){
//            Animal mom = parents.peek();
//            Animal dad = parents.peek();
//            Animal child = mom.newBornAnimal(dad);
//            animals.add(child);
//            map.addPriotityAnimal(child, child.getPosition());
//            statistics.addOneLiveAnimal();
//            dad.addChild();
//            mom.addChild();
//        }
//    }

}
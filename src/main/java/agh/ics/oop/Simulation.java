package agh.ics.oop;

import agh.ics.oop.gui.App;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Simulation implements Runnable{
    private final AbstractWorldMap map;
    private final StartParameters parameters;
    private final ArrayList<Animal> animals;
    private final ArrayList<Grass> grass;
    private final Statistics statistics;
    private final int energyToMove;
    private final App app;
    private Boolean run;
    private final boolean magic;
    private int useMagic;
    private final StatisticFile file;
    private int childerenOfTrackedAnimal;
    private boolean ifTrackedAnimal;
    private boolean typeMap;
    private int deathDay;


    public Simulation(StartParameters parameters, App applic, boolean TypeMap, StatisticFile file){
        if(TypeMap) {
            this.map = new RightMap(parameters.getWidth(),parameters.getHeight(),parameters.getJungleRatio(), parameters.getEnergyGrass(), parameters.getMagicRight());
            this.magic = parameters.getMagicRight();
        }
        else {
            this.map = new LeftMap(parameters.getWidth(),parameters.getHeight(), parameters.getJungleRatio(), parameters.getEnergyGrass(), parameters.getMagicLeft());
            this.magic = parameters.getMagicLeft();
        }
        this.parameters = parameters;
        this.animals = new ArrayList<>();
        this.grass = new ArrayList<>();
        this.energyToMove = parameters.getEnergyToMove();
        this.statistics = new Statistics(parameters.getStartEnergy(), parameters.getNumberOfAnimals());
        this.app = applic;
        this.run = false;
        this.useMagic = 0;
        this.file = file;
        this.childerenOfTrackedAnimal = 0;
        this.ifTrackedAnimal = false;
        this.typeMap = TypeMap;
        this.deathDay = 0;
        placeAnimalsFirstTime(parameters.getNumberOfAnimals());
    }

    private void placeAnimalsFirstTime(int animalNumber){
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

    public void changeStatus(){this.run = !this.run;}
    public AbstractWorldMap getMap(){return this.map;}
    public StartParameters getParameters(){return this.parameters;}
    public ArrayList<Grass> getGrass() {return this.grass;}
    public Statistics getStatistics(){return this.statistics;}
    public void stopStatus(){this.run = false;}

    public void run(){
        while (true) {
            try {
                if(animals.size() == 5 && magic && useMagic < 3){
                    app.changeStatusSimulation();
                    placeMagicAnimals();
                    useMagic += 1;
                    app.addMagicAnimals();
                }
                day();
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                System.out.println(ex);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void day() throws IOException {
        if(run) {
            removeDeadAnimals();
            movingAnimals();
            consumptionGrass();
            animalsReproduction();
            plantTuft();
            app.showMap();
            statistics.addOneDay();
            file.writeDataInFile(statistics);
        }
    }

    public void show(String genotype){
        if(!this.run) app.showAnimalGenotype(genotype);
    }

    private void removeDeadAnimals() {
        int sumDays = 0;
        ArrayList<Animal> animalToRemove = new ArrayList<>();
        for (Animal animal : animals) {
            if (animal.getEnergy() <= 0) {
                if(animal.ifTracked()) this.deathDay = statistics.getWorldDays();
                sumDays += animal.getNumberOfDays();
                animalToRemove.add(animal);
            }
        }
        for (Animal animal : animalToRemove) {
            statistics.addOneDeadAnimal();
            animals.remove(animal);
            map.removeDeadAnimal(animal);
        }
        statistics.counterAvgChildrenOfAliveAnimals(animals);
        statistics.addDaysDeadAnimal(sumDays);
        statistics.counterOfAvgLifeDaysDeadAnimals();
    }

    private void movingAnimals(){
        int sumEnergy = 0;
        for(Animal animal : animals){
            int movement = animal.selectMovement();
            animal.move(movement);
            animal.substractEnergy(energyToMove);
            animal.addOneDay();
            sumEnergy += animal.getEnergy();
        }
        statistics.counterOfAvgEnergy(sumEnergy);
    }


    private void consumptionGrass() {
        LinkedList<Grass> grassToRemove = new LinkedList<>();
        int calories = parameters.getEnergyGrass();
        for (Grass tuft : grass) {
            LinkedList<Animal> animalsOnPosition = map.hungryAnimalsAtPosition(tuft.getPosition());
            if (animalsOnPosition != null) {
                for (Animal animal : animalsOnPosition) {
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
    }

    private void animalsReproduction(){
        LinkedList<LinkedList<Animal>> allPairToReproduce = map.findPair(energyToMove * (0.5f));
        for (LinkedList<Animal> parents : allPairToReproduce){
            Animal parentFirst = parents.poll();
            Animal parentSecond = parents.poll();
            if(parentFirst.ifTracked() || parentSecond.ifTracked()) this.childerenOfTrackedAnimal +=1;
            Animal child = parentFirst.newBornAnimal(parentSecond);
            animals.add(child);
            map.place(child);
            statistics.addOneLiveAnimal();
        }
        statistics.findDominantGenotype(animals);
    }

    private void plantTuft(){
        if(map.isEmptyPlaceInJungle()){
            Grass tuft = map.plantTuftInJungle();
            grass.add(tuft);
            statistics.addOneGrass();
        }
        if(map.isEmptyPlaceInSteppe()){
            Grass tuft = map.plantTuftInSteppe();
            grass.add(tuft);
            statistics.addOneGrass();
        }
    }

    private void placeMagicAnimals() {
        Vector2d position;
        Random random = new Random();
        int x;
        int y;
        for (int i = 0; i < 5; i++) {
            if (map.isEmptyPlaceInJungle() || map.isEmptyPlaceInSteppe()) {
                do {
                    x = random.nextInt(parameters.getWidth());
                    y = random.nextInt(parameters.getHeight());
                    position = new Vector2d(x, y);
                }
                while (map.isOccupied(position));
                AnimalGenes genes = animals.get(i).getGenes();
                Animal animal = new Animal(position, parameters.getStartEnergy(), map, genes);
                animals.add(animal);
                map.place(animal);
                statistics.addOneLiveAnimal();
            }
        }
    }

    public void trackedAnimal(Animal animalTracked){
        if(!run) {
            animalTracked.addStatusTracked();
            if (typeMap) app.changeStatusTrackedRight();
            else app.changeStatusTrackedLeft();
        }
    }

    public void addStatusTracked(){ this.ifTrackedAnimal = true;}

    public void removeStatusTracked(){
        this.deathDay = 0;
        this.childerenOfTrackedAnimal = 0;
        this.ifTrackedAnimal = false;
        for (Animal animal : animals) {
            animal.removeStatusChild();
            animal.removeStatusTracked();
        }
    }

    public int getChildrenOfTrackedAnimal(){return childerenOfTrackedAnimal;}

    public boolean getIfTrackedAnimal(){ return ifTrackedAnimal;}

    public boolean getRun(){return run;}

    public int getDeathDay(){ return deathDay;}
}

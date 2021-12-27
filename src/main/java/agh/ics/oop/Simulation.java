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
    private int childrenOfTrackedAnimal;
    private boolean ifTrackedAnimal;
    private final boolean typeMap;
    private int deathDay;


    public Simulation(StartParameters parameters, App app, boolean TypeMap, StatisticFile file){
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
        this.app = app;
        this.run = false;
        this.useMagic = 0;
        this.file = file;
        this.ifTrackedAnimal = false;
        this.childrenOfTrackedAnimal = 0;
        this.typeMap = TypeMap;
        this.deathDay = 0;
        placeAnimalsFirstTime(parameters.getNumberOfAnimals());
    }

    public int getChildrenOfTrackedAnimal(){return childrenOfTrackedAnimal;}
    public boolean getIfTrackedAnimal(){ return ifTrackedAnimal;}
    public boolean getRun(){return run;}
    public int getDeathDay(){ return deathDay;}
    public AbstractWorldMap getMap(){return this.map;}
    public StartParameters getParameters(){return this.parameters;}
    public ArrayList<Grass> getGrass() {return this.grass;}
    public Statistics getStatistics(){return this.statistics;}
    public void changeStatus(){this.run = !this.run;}
    public void show(String genotype){
        if(!this.run) app.showAnimalGenotype(genotype);
    }
    public void addStatusTracked(){ this.ifTrackedAnimal = true;}
    public void stopStatus(){this.run = false;}

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

    public void run(){
        while (true) {
            try {
                if(animals.size() == 5 && magic && useMagic < 3){
                    app.stopSimulation();
                    placeMagicAnimals();
                    useMagic += 1;
                    app.addMagicAnimals();
                }
                day();
                Thread.sleep(500);
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
            if(typeMap)app.updateRightMap();
            else app.updateLeftMap();
            statistics.addDay();
            file.writeDataInFile(statistics);
        }
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
            statistics.addDeadAnimal();
            animals.remove(animal);
            map.removeDeadAnimal(animal);
        }
        statistics.calculateAvgChildrenOfAliveAnimals(animals);
        statistics.addDaysDeadAnimal(sumDays);
        statistics.calculateAvgLifeDaysDeadAnimals();
    }

    private void movingAnimals(){
        int sumEnergy = 0;
        for(Animal animal : animals){
            int movement = animal.selectMovement();
            animal.move(movement);
            animal.subtractEnergy(energyToMove);
            animal.addOneDay();
            sumEnergy += animal.getEnergy();
        }
        statistics.calculateOfAvgEnergy(sumEnergy);
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
            statistics.subtractGrass();
        }
    }

    private void animalsReproduction(){
        LinkedList<LinkedList<Animal>> allPairToReproduce = map.findPair(energyToMove * (0.5f));
        for (LinkedList<Animal> parents : allPairToReproduce){
            Animal parentFirst = parents.poll();
            Animal parentSecond = parents.poll();
            if(parentFirst.ifTracked() || parentSecond.ifTracked()) this.childrenOfTrackedAnimal +=1;
            Animal child = parentFirst.newBornAnimal(parentSecond);
            animals.add(child);
            map.place(child);
            statistics.addLiveAnimal();
        }
        statistics.findDominantGenotype(animals);
    }

    private void plantTuft(){
        if(map.isEmptyPlaceInJungle()){
            Grass tuft = map.plantTuftInJungle();
            grass.add(tuft);
            statistics.addGrass();
        }
        if(map.isEmptyPlaceInSteppe()){
            Grass tuft = map.plantTuftInSteppe();
            grass.add(tuft);
            statistics.addGrass();
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
                statistics.addLiveAnimal();
            }
        }
    }

    public void trackedAnimal(Animal animalTracked){
        if(!run) {
            removeStatusTracked();
            animalTracked.addStatusTracked();
            if (typeMap) app.changeStatusTrackedRight();
            else app.changeStatusTrackedLeft();
        }
    }

    public void removeStatusTracked(){
        this.deathDay = 0;
        this.childrenOfTrackedAnimal = 0;
        this.ifTrackedAnimal = false;
        for (Animal animal : animals) {
            animal.removeAnimalStatusTracked();
        }
    }
}

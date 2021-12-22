package agh.ics.oop;

import agh.ics.oop.gui.App;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Simulation implements Runnable{
    private final AbstractWorldMap map;
    private final StartParameters parameters;
    private ArrayList<Animal> animals;
    private ArrayList<Grass> grass;
    private Statistics statistics;
    private int energyToMove;
    private App app;
    private Boolean run;
    private boolean magic;
    private int useMagic;
    private StatisticFile file;


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

        placeAnimalsFirstTime(parameters.getNumberOfAnimals());
    }

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

    public void changeStatus(){this.run = !this.run;}
    public AbstractWorldMap getMap(){return this.map;}
    public StartParameters getParameters(){return this.parameters;}
    public ArrayList<Grass> getGrass() {return this.grass;}
    public Statistics getStatistics(){return this.statistics;}

    public void run(){
        while (true) {
            try {
                if(animals.size() == 5 && magic && useMagic < 3){
                    app.changeStatus();
                    placeMagicAnimals();
                    useMagic += 1;
                    app.addMagicAnimals();
                }
                day();
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                System.out.println(ex.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void day() throws IOException {
        if(run) {
            removeDeadAnimals();
            animalsMove();
            consumptionGrass();
            animalReproduction();
            plantTuft();
            app.showMap();
            statistics.addOneDay();
            file.writeDataInFileMap(statistics);
        }
    }

    private void removeDeadAnimals() {  // remove animals if have not enough energy
        int sumDays = 0;
        ArrayList<Animal> animalToRemove = new ArrayList<>();
        for (Animal animal : animals) {
            if (animal.getEnergy() <= 0) {
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

    private void animalsMove(){ // rotating or moving animals according to their genes
        int sumEnergy = 0;
        for(Animal animal : animals){
            animal.addOneDay();
            int movement = animal.selectMovement();
            animal.move(movement);
            animal.substractEnergy(energyToMove);
            sumEnergy += animal.getEnergy();
        }
        statistics.counterOfAvgEnergy(sumEnergy);
    }

    // if there is more than one animal i one field, the strongest animal recevie all energy but if there are several animals
    // that have the same amount of energy, taht energy is shared between them
    private void consumptionGrass() {
        LinkedList<Grass> grassToRemove = new LinkedList<>();
        int calories = parameters.getEnergyGrass();
        for (Grass tuft : grass) {
            LinkedList<Animal> animalsOnPosition = map.hungryAnimalsInPosition(tuft.getPosition());
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

    private void animalReproduction(){
        LinkedList<LinkedList<Animal>> allPairToREproduce = map.findPair(energyToMove * (0.5f));
        for (LinkedList<Animal> parents : allPairToREproduce){
            Animal child = parents.poll().newBornAnimal(parents.poll());
            animals.add(child);
            map.place(child);
            statistics.addOneLiveAnimal();
        }
        statistics.findDominantGenotype(animals);
    }

    private void plantTuft(){
        if(map.isEmptyJungle()){
            Grass tuft = map.plantTuftInJungle();
            grass.add(tuft);
            statistics.addOneGrass();
        }
        if(map.isEmptySteppe()){
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
            if (map.isEmptyJungle() || map.isEmptySteppe()) {
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
}

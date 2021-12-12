package agh.ics.oop;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Simulation{
    private final AbstractWorldMap map;
    private final Parameters parameters;
    private ArrayList<Animal> animals;
    private ArrayList<Grass> grass;

    public Simulation(Parameters parameters){
        this.map = new RightMap(parameters.getHeight(),parameters.getHeight(),parameters.getJungleSize(), parameters.getCaloriesGrass());
        this.parameters = parameters;
        this.animals = new ArrayList<>();
        this.grass = new ArrayList<>();
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
        }
    }

    public void anotherDay(){
        if(animals.size() > 0){
            removeDeadAnimals();
            animalsMove();
            consumptionGrass();
            plantTuft();
            animalReproduction();
        }
    }

    private void animalsMove(){    // rotating or moving animals according to their genes
        for(Animal animal : animals){
            int movement = animal.selectMovement();
            animal.move(movement);
            animal.substractEnergy(1);
        }
    }

    private void removeDeadAnimals() {  // remove animals if have not enough energy
        LinkedList<Animal> deadAnimals = new LinkedList<>();
        for (Animal animal : animals) {
            if (animal.getEnergy() <= 0) {
                deadAnimals.add(animal);
                map.removeDeadAnimal(animal);
            }
        }
    }
    // if there is more than one animal i one field, the strongest animal recevie all energy but if there are several animals
    //that have the same amount of energy, taht energy is shared between them
    private void consumptionGrass(){
        for(Grass tuft : grass){
            LinkedList<Animal> hungryAnimals = map.hungryAnimalsInPosition(tuft.getPosition());
            if(hungryAnimals != null){
                if(hungryAnimals.size() == 1){
                    hungryAnimals.get(0).addEnergy(parameters.getCaloriesGrass());
                }
                else{
                    int calories = parameters.getCaloriesGrass() / hungryAnimals.size();
                    while(hungryAnimals.size() > 0){
                        hungryAnimals.get(0).addEnergy(calories);
                    }

                }
            }
            map.removeEatenGrass(tuft);
            grass.remove(tuft);
        }
    }

    private void plantTuft(){
        if(map.isEmptyPlaceInJungle()){  // Checking if a tuft can still be planted in Jungle
            map.plantTuftInJungle();     // If True, plant in random place in jungle
        }
        if(map.isEmptyPlaceInSteppe()){  // same as above
            map.plantTuftInSteppe();
        }
    }

    private void animalReproduction(){
        LinkedList<LinkedList<Animal>> pairsOfAnimal = map.findPairOfAnimal(this.parameters.getStartEnergy());
        for(LinkedList<Animal> parents : pairsOfAnimal){
            Animal mom = parents.peek();
            Animal dad = parents.peek();
            Animal child = mom.newBornAnimal(dad);
            animals.add(child);
        }
    }
}

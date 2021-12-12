package agh.ics.oop;

import java.util.LinkedHashMap;
import java.util.PriorityQueue;
import java.util.Random;

public class RightMap extends AbstractWorldMap implements IWorldMap{  // mapa z murem
    private Vector2d lowerLeft;
    private Vector2d upperRight;
    private Vector2d lowerLeftJungle;
    private Vector2d upperRightJungle;
    private int caloriesGrass;
    protected LinkedHashMap<Vector2d, PriorityQueue<Animal>> animals;
    protected LinkedHashMap<Vector2d, Grass> grass;

    public RightMap(int height, int width, float jungleRatio, int caloriesGrass) {
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width, height);
        this.lowerLeftJungle = jungleLowerLeft(height, width, jungleRatio);
        this.upperRightJungle = jungleUpperRight(height, width, jungleRatio);
        this.caloriesGrass = caloriesGrass;
        this.animals = new LinkedHashMap<>();
        this.grass = new LinkedHashMap<>();
    }


    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.precedes(upperRight) && position.follows(lowerLeft);
    }

    @Override
    public void place(Animal animal) {
        animal.addObserver(this);
        Vector2d position = animal.getPosition();
        addPriotityAnimal(animal,position);
    }

    public void addPriotityAnimal(Animal animal, Vector2d position){
        PriorityQueue<Animal> animalsOnPosition = animals.get(position);
        if(animalsOnPosition == null){
            PriorityQueue<Animal> animalsPriority = new PriorityQueue<>((animal1, animal2) -> animal2.getEnergy() - animal1.getEnergy());
            animalsPriority.add(animal);
            animals.put(position,animalsPriority);
        }
        else{
            animalsOnPosition.add(animal);
        }
    }


    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
    }

    public boolean isEmptyPlaceInJungle(){
        for(int i = lowerLeftJungle.x; i <= upperRightJungle.x; i++){
            for(int j = lowerLeftJungle.y; j <= upperRightJungle.y; j++) {
                if (!isOccupied(new Vector2d(i,j)))
                    return true;
            }
        }
        return false;
    }

    public void plantTuftInJungle(){
        Vector2d newJungleGrass;
        Random random = new Random();
        do{
            int positionX = this.lowerLeftJungle.x + random.nextInt(this.upperRightJungle.x - this.lowerLeftJungle.x + 1);
            int positionY = this.lowerLeftJungle.y + random.nextInt(this.upperRightJungle.y - this.lowerLeftJungle.y + 1);
            newJungleGrass = new Vector2d(positionX,positionY);
        } while(isOccupied(newJungleGrass));
        this.grass.put(newJungleGrass, new Grass(newJungleGrass));
    }

    @Override
    public boolean isEmptyPlaceInSteppe() {
        for(int i = lowerLeftJungle.x; i <= upperRightJungle.x; i++){
            for(int j = lowerLeftJungle.y; j <= upperRightJungle.y; j++) {
                Vector2d position = new Vector2d(i,j);
                if(!(position.precedes(upperRightJungle) && position.follows(lowerLeftJungle))){
                    if (!isOccupied(new Vector2d(i,j)))
                        return true;
                }
            }
        }
        return false;
    }

    @Override
    public void plantTuftInSteppe() {
        Vector2d newJungleGrass;
        Random random = new Random();
        do{
            int positionX = random.nextInt(this.upperRight.x + 1);
            int positionY = random.nextInt(this.upperRight.y + 1);
            newJungleGrass = new Vector2d(positionX,positionY);
        } while(isOccupied(newJungleGrass) || !((newJungleGrass.precedes(upperRightJungle) && newJungleGrass.follows(lowerLeftJungle))));
        this.grass.put(newJungleGrass, new Grass(newJungleGrass));
    }
}

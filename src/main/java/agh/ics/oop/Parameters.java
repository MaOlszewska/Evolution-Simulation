package agh.ics.oop;

public class Parameters {
    private int width = 10;
    private int height = 10;
    private float jungleSize = 0.4f;
    private int caloriesGrass = 200;
    private int numberOfAnimals = 20;
    private int startEnergy = 200;
    private int energyToMove = 10;

    public int getEnergyToMove() {return energyToMove;}

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getJungleSize() {
        return jungleSize;
    }

    public int getStartEnergy() {
        return startEnergy;
    }

    public int getCaloriesGrass() {
        return caloriesGrass;
    }

    public int getNumberOfAnimals() {
        return numberOfAnimals;
    }
}

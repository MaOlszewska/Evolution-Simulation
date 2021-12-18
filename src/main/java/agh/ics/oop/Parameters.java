package agh.ics.oop;

public class Parameters {
    private int width = 10;
    private int height = 10;
    private float jungleSize = 0.5f;
    private int caloriesGrass = 30;
    private int numberOfAnimals = 40;
    private int startEnergy = 100;
    private int energyToMove = 5;

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

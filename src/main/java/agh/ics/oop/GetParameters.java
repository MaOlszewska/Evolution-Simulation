package agh.ics.oop;

public class GetParameters {
    private int width;
    private int height;
    private float jungleRatio;
    private int caloriesGrass;
    private int numberOfAnimals;
    private int startEnergy;
    private int energyToMove;

    public GetParameters(int width, int height, float jungleRatio, int caloriesGrass, int numberOfAnimals, int startEnergy, int energyToMove){
        this.width = width;
        this.height = height;
        this.jungleRatio = jungleRatio;
        this.caloriesGrass = caloriesGrass;
        this.numberOfAnimals = numberOfAnimals;
        this.startEnergy = startEnergy;
        this.energyToMove = energyToMove;
}
    public int getEnergyToMove() {return energyToMove;}

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getJungleRatio() {
        return jungleRatio;
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

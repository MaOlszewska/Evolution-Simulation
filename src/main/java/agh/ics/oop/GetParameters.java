package agh.ics.oop;

public class GetParameters {
    private int width;
    private int height;
    private float jungleRatio;
    private int caloriesGrass;
    private int numberOfAnimals;
    private int startEnergy;
    private int energyToMove;
    private boolean magicLeft;
    private boolean magicRight;

    public GetParameters(int width, int height, float jungleRatio, int caloriesGrass, int numberOfAnimals, int startEnergy, int energyToMove, boolean magicLeft, boolean magicRight){
        this.width = width;
        this.height = height;
        this.jungleRatio = jungleRatio;
        this.caloriesGrass = caloriesGrass;
        this.numberOfAnimals = numberOfAnimals;
        this.startEnergy = startEnergy;
        this.energyToMove = energyToMove;
        this.magicLeft = magicLeft;
        this.magicRight = magicRight;
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

    public boolean getMagicLeft(){ return magicLeft;}

    public boolean getMagicRight(){return magicRight;}
}

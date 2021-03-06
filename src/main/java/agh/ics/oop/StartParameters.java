package agh.ics.oop;

public class StartParameters {
    private final int width;
    private final int height;
    private final float jungleRatio;
    private final int energyGrass;
    private final int numberOfAnimals;
    private final int startEnergy;
    private final int energyToMove;
    private final boolean magicLeft;
    private final boolean magicRight;

    public StartParameters(int width, int height, float jungleRatio, int caloriesGrass, int numberOfAnimals, int startEnergy, int energyToMove, boolean magicLeft, boolean magicRight){
        this.width = width;
        this.height = height;
        this.jungleRatio = jungleRatio;
        this.energyGrass = caloriesGrass;
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
    public int getEnergyGrass() {
        return energyGrass;
    }
    public int getNumberOfAnimals(){ return numberOfAnimals; }
    public boolean getMagicLeft(){ return magicLeft; }
    public boolean getMagicRight(){ return magicRight; }
}

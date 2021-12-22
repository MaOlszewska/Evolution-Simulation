package agh.ics.oop;

public class RightMap extends AbstractWorldMap{  // map with brick wall

    public RightMap(int width, int height, float jungleRatio, int caloriesGrass, boolean magicRight) {
        super(width,height,jungleRatio,caloriesGrass, magicRight);
    }

    @Override
    public Vector2d selectPosition(Vector2d oldPosition, MapDirection orientation) {
        return oldPosition.add(orientation.toUnitVector());
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.precedes(upperRight) && position.follows(lowerLeft);
    }


}


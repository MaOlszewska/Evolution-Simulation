package agh.ics.oop;


public class LeftMap extends AbstractWorldMap{  // folding Map

    public LeftMap( int width, int height,float jungleSize, int caloriesGrass, boolean magicLeft) {
        super(width, height, jungleSize, caloriesGrass, magicLeft);
    }

    @Override
    public Vector2d selectPosition(Vector2d oldPosition, MapDirection orientation) {
        int x;
        int y;
        Vector2d newPosition = oldPosition.add(orientation.toUnitVector());
        if(newPosition.follows(lowerLeft) && newPosition.precedes(upperRight)) return newPosition;
        else{
            if(newPosition.x < 0) x = upperRight.x;
            else if(newPosition.x > upperRight.x) x = 0;
            else x = newPosition.x;

            if(newPosition.y < 0) y = upperRight.y;
            else if(newPosition.y > upperRight.y) y = 0;
            else y = newPosition.y;

        }
        return new Vector2d(x,y);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return true;
    }
}

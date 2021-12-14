package agh.ics.oop;

public interface IPositionChangeObserver {
    /**
     *  Delete oldPosition and add newPosition.
     */

    void positionChanged(Animal animal,Vector2d oldPosition, Vector2d newPosition);


}

package agh.ics.oop;

public interface IPositionChangeObserver {
    /**
     *  Delete oldPosition and add newPosition of animal.
     */

    void positionChanged(Animal animal,Vector2d oldPosition, Vector2d newPosition);


}

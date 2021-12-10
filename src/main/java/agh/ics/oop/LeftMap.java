//package agh.ics.oop;
//
//import java.util.LinkedHashMap;
//
//public class LeftMap extends AbstractWorldMap{  // mapa z zawijaniem
//    private Vector2d lowerLeft;
//    private Vector2d upperRight;
//    private Vector2d lowerLeftJungle;
//    private Vector2d upperRightJungle;
//    private int caloriesGrass;
//    private LinkedHashMap<Vector2d, Grass> grass;
//    //private LinkedHashMap<Vector2d, Animal> animal;
//
//    public LeftMap(int height, int width, int jungleSize, int caloriesGrass) {
//        this.lowerLeft = new Vector2d(0, 0);
//        this.upperRight = new Vector2d(width, height);
//        this.lowerLeftJungle = jungleLowerLeft(height, width, jungleSize);
//        this.upperRightJungle = jungleUpperRight(height, width, jungleSize);
//        this.caloriesGrass = caloriesGrass;
//        //this.animal = new LinkedHashMap<>();
//        this.grass = new LinkedHashMap<>();
//    }
//}

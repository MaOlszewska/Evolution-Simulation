package agh.ics.oop;


import agh.ics.oop.gui.App;
import javafx.application.Application;
import javafx.stage.Stage;

public class World {

    public static void main(String[] args) {
//        IWorldMap map = new RightMap(10,10,0.5f,20);
//        Animal animal = new Animal(new Vector2d(1,1),40,map,new AnimalGenes());
//        Animal animal2 = new Animal(new Vector2d(2,1),28,map,new AnimalGenes());
//        int[] genes1 = animal.getGenes().getGenes();
//        int[] genes2 = animal2.getGenes().getGenes();
//        for(int i = 0; i< 32; i++){
//            System.out.print(genes1[i]);
//        }
//
//        System.out.println("\n" + " " + animal.findDominantGenotype());
//        for(int i = 0; i< 32; i++){
//            System.out.print(genes2[i]);
//        }
//        System.out.println("\n");
//        Animal child = animal.newBornAnimal(animal2);
//        System.out.println("\n");
//        System.out.println(child.getEnergy());
          Application.launch(App.class,args);
    }


}
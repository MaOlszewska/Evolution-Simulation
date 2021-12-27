package agh.ics.oop.gui;

import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Images {
    public Image grassImage;
    public Image elephantImage;
    public Image giraffeImage;
    public Image catImage;
    public Image mouseImage;
    public Image elephantTrackedImage;
    public Image giraffeTrackedImage;
    public Image catTrackedImage;
    public Image mouseTrackedImage;

    public Images(){
        try {
            this.grassImage = new javafx.scene.image.Image(new FileInputStream("src/main/resources/grass.png"));
            this.elephantImage = new javafx.scene.image.Image(new FileInputStream("src/main/resources/elephant.png"));
            this.giraffeImage = new javafx.scene.image.Image(new FileInputStream("src/main/resources/giraffe.png"));
            this.catImage = new javafx.scene.image.Image(new FileInputStream("src/main/resources/cat.png"));
            this.mouseImage = new javafx.scene.image.Image(new FileInputStream("src/main/resources/mouse.png"));
            this.elephantTrackedImage = new javafx.scene.image.Image(new FileInputStream("src/main/resources/trackedElephant.png"));
            this.giraffeTrackedImage = new javafx.scene.image.Image(new FileInputStream("src/main/resources/trackedGiraffe.png"));
            this.catTrackedImage = new javafx.scene.image.Image(new FileInputStream("src/main/resources/trackedCat.png"));
            this.mouseTrackedImage = new javafx.scene.image.Image(new FileInputStream("src/main/resources/trackedMouse.png"));
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
}

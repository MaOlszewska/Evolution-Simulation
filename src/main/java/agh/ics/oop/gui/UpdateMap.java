package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class UpdateMap {
    private Simulation engine;
    private GridPane gridPane;
    private VBox stats;

    public UpdateMap(Simulation engine) throws FileNotFoundException {
        this.engine = engine;
        this.gridPane = new GridPane();
        this.stats = showStatistic();
        for (int i = 0; i < engine.getParameters().getWidth(); i++){
            this.gridPane.getColumnConstraints().add(new ColumnConstraints(20));
        }
        for (int i = 0; i < engine.getParameters().getHeight(); i++){
            this.gridPane.getRowConstraints().add(new RowConstraints(20));
        }
        createMap();
    }

    public GridPane getGridPane(){ return this.gridPane;}
    public VBox getStats(){return this.stats;}

    private void createMap() throws FileNotFoundException {
        gridPane.getChildren().clear();
        gridPane.setGridLinesVisible(true);
        coloringMap();
        drawObjects();
    }

    private VBox showStatistic(){
        Statistics statistics = engine.getStatistics();
        Label title = new Label("SIMULATION STATISTIC");
        title.setFont(new Font(15));
        Label worldDays = new Label("World days: " + statistics.getWorldDays());
        Label numberOfAliveAnimals = new Label("Number of alive Animals: " + statistics.getNumberOfAliveAnimals());
        Label numberOfGrass = new Label("Number of Grass: " + statistics.getNumberOfGrass());
        Label numberOfDeadAnimals = new Label("Number of Dead Animals: " + statistics.getNumberOfDeadAnimals());
        Label avgEnergy = new Label("Average of energy: " + statistics.getAvgEnergy());
        Label avgLifeDaysDeadAnimal = new Label("Average of days: " + statistics.getAvgLifeDaysOfDeadAnimal());
        Label avgChildren = new Label("Average of Children: " + statistics.getAvgChildren());
        Label dominantGentype = new Label("Dominant Genotype:  " + statistics.getDominantGenotype());

        VBox stats = new VBox();
        stats.getChildren().addAll(title,worldDays, numberOfAliveAnimals, numberOfGrass, numberOfDeadAnimals, avgEnergy, avgLifeDaysDeadAnimal, avgChildren, dominantGentype);
        stats.setAlignment(Pos.TOP_LEFT);
        stats.setSpacing(10);
        return stats;
    }

    private void drawObjects() throws FileNotFoundException {
        Image grassImage = new Image(new FileInputStream("src/main/resources/grass.png"));
        Image elephantImage = new Image(new FileInputStream("src/main/resources/elephant.png"));
        Image giraffeImage = new Image(new FileInputStream("src/main/resources/giraffe.png"));
        Image catImage = new Image(new FileInputStream("src/main/resources/cat.png"));
        Image mouseImage = new Image(new FileInputStream("src/main/resources/mouse.png"));

        IWorldMap map = engine.getMap();
        AbstractWorldMap abstractMap = (AbstractWorldMap) map;
        ArrayList<Animal> animals = abstractMap.getAnimals();
        ImageView imageView  = new ImageView();
        for(Animal animal : animals){
            switch (animal.getImage()) {
                case 4 :
                    imageView = new ImageView(elephantImage);
                    break;
                case 3 :
                    imageView = new ImageView(giraffeImage);
                    break;
                case 2 :
                    imageView = new ImageView(catImage);
                    break;
                case 1 :
                    imageView = new ImageView(mouseImage);
                    break;
            }
            imageView.setFitWidth(20);
            imageView.setFitHeight(20);
            gridPane.add(imageView, animal.getPosition().x, animal.getPosition().y);
        }

        ArrayList<Grass> grass = engine.getGrass();
        for(Grass gras : grass){
            imageView = new ImageView(grassImage);
            imageView.setFitWidth(15);
            imageView.setFitHeight(15);
            gridPane.add(imageView, gras.getPosition().x,gras.getPosition().y);
        }
    }

    private void coloringMap(){
        IWorldMap map = engine.getMap();
        AbstractWorldMap abstractMap = (AbstractWorldMap) map;
        Vector2d lowerLeft = abstractMap.getLowerLeft();
        Vector2d upperRight = abstractMap.getUpperRight();
        Vector2d lowerLeftJungle = abstractMap.getLowerLeftJungle() ;
        Vector2d upperRightJungle = abstractMap.getUpperRightJungle();

        for(int i = 0; i <= upperRight.x; i++ ) {
            for (int j = 0; j <= upperRight.y; j++) {
                Vector2d pos = new Vector2d(i, j);
                StackPane till = new StackPane();
                if(pos.follows(lowerLeftJungle) && pos.precedes(upperRightJungle)) {
                    till.setBackground(new Background(new BackgroundFill(Color.DARKGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                    gridPane.add(till, i, j);
                } else {
                    till.setBackground(new Background(new BackgroundFill(Color.OLIVEDRAB, CornerRadii.EMPTY, Insets.EMPTY)));
                    gridPane.add(till, i, j);
                }
            }
        }
    }
}

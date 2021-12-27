package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import java.util.ArrayList;

public class UpdateMap {
    private final Simulation engine;
    private final GridPane gridPane;
    private final VBox stats;
    private final Application app;
    private final Images images = new Images();


    public UpdateMap(Simulation engine, Application app) {
        this.app = app;
        this.engine = engine;
        this.gridPane = new GridPane();
        this.stats = updateStatistic();
        for (int i = 0; i < engine.getParameters().getWidth(); i++){
            this.gridPane.getColumnConstraints().add(new ColumnConstraints(1200/ (3 * engine.getMap().getWidth() )));
        }
        for (int i = 0; i < engine.getParameters().getHeight(); i++){
            this.gridPane.getRowConstraints().add(new RowConstraints(1000/ (3 * engine.getMap().getHeight()) ));
        }
        updateMap();
    }

    public GridPane getGridPane(){ return this.gridPane;}

    public VBox getStats(){return this.stats;}

    private void updateMap(){
        gridPane.getChildren().clear();
        gridPane.setGridLinesVisible(true);
        coloringMap();
        drawingObjects();
    }

    private VBox updateStatistic(){
        Statistics statistics = engine.getStatistics();
        Label title = new Label("STATISTIC");
        Label worldDays = new Label("World days: " + statistics.getWorldDays());
        Label numberOfAliveAnimals = new Label("Number of alive Animals: " + statistics.getNumberOfAliveAnimals());
        Label numberOfGrass = new Label("Number of Grass: " + statistics.getNumberOfGrass());
        Label numberOfDeadAnimals = new Label("Number of Dead Animals: " + statistics.getNumberOfDeadAnimals());
        Label avgEnergy = new Label("Average of energy: " + statistics.getAvgEnergy());
        Label avgLifeDaysDeadAnimal = new Label("Average of days: " + statistics.getAvgLifeDaysOfDeadAnimal());
        Label avgChildren = new Label("Average of Children: " + statistics.getAvgChildren());
        Label dominantGenotype = new Label("Dominant Genotype:  " + statistics.getDominantGenotype());

        VBox stats = new VBox();
        stats.getChildren().addAll(title,worldDays, numberOfAliveAnimals, numberOfGrass, numberOfDeadAnimals, avgEnergy, avgLifeDaysDeadAnimal, avgChildren, dominantGenotype);
        stats.setAlignment(Pos.TOP_CENTER);
        stats.setSpacing(10);
        title.setFont(new Font(15));
        title.setStyle("-fx-font-weight: bold");
        return stats;
    }

    private void drawingObjects(){
        AbstractWorldMap map = engine.getMap();
        ArrayList<Animal> animals = map.getAnimals();
        ImageView imageView  = new ImageView();
        for(Animal animal : animals){
            switch (animal.getImage()) {
                case 8 -> imageView = new ImageView(images.elephantTrackedImage);
                case 7 -> imageView = new ImageView(images.giraffeTrackedImage);
                case 6 -> imageView = new ImageView(images.catTrackedImage);
                case 5 -> imageView = new ImageView(images.mouseTrackedImage);
                case 4 -> imageView = new ImageView(images.elephantImage);
                case 3 -> imageView = new ImageView(images.giraffeImage);
                case 2 -> imageView = new ImageView(images.catImage);
                case 1 -> imageView = new ImageView(images.mouseImage);
            }
            imageView.setFitWidth(1000 / (4 * engine.getMap().getWidth() ));
            imageView.setFitHeight(1000 / (4 * engine.getMap().getWidth() ));
            imageView.setOnMouseClicked(event -> {
                if(!engine.getRun()){
                    AnimalGenes genes = animal.getGenes();
                    engine.show(genes.getString());
                    engine.trackedAnimal(animal);
                }
            });
            gridPane.add(imageView, animal.getPosition().x, animal.getPosition().y);
            gridPane.setAlignment(Pos.CENTER);
        }

        ArrayList<Grass> grass = engine.getGrass();
        for(Grass gras : grass){
            imageView = new ImageView(images.grassImage);
            imageView.setFitWidth(800 / (4 * engine.getMap().getWidth() ));
            imageView.setFitHeight(800 / (4 * engine.getMap().getWidth() ));

            gridPane.add(imageView, gras.getPosition().x,gras.getPosition().y);
            gridPane.setAlignment(Pos.CENTER);
        }
    }

    private void coloringMap(){
        AbstractWorldMap map = engine.getMap();
        Vector2d upperRight = map.getUpperRight();
        Vector2d lowerLeftJungle = map.getLowerLeftJungle() ;
        Vector2d upperRightJungle = map.getUpperRightJungle();

        for(int i = 0; i <= upperRight.x; i++ ) {
            for (int j = 0; j <= upperRight.y; j++) {
                Vector2d pos = new Vector2d(i, j);
                StackPane till = new StackPane();
                if(pos.follows(lowerLeftJungle) && pos.precedes(upperRightJungle)) {
                    till.setStyle("-fx-background-color: #00b27a; ");
                } else {
                    till.setStyle("-fx-background-color: #00ce8e; ");
                }
                gridPane.add(till, i, j);
            }
        }
    }
}

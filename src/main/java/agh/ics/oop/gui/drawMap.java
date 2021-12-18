package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class drawMap {
    private Simulation engine;
    private GridPane gridPane;
    private VBox stats;

    public drawMap(Simulation engine){
        this.engine = engine;
        this.gridPane = new GridPane();
        this.stats = showStatistic();
        for (int i = 0; i < engine.getParameters().getWidth(); i++){
            this.gridPane.getColumnConstraints().add(new ColumnConstraints(20));
        }
        for (int i = 0; i < engine.getParameters().getHeight(); i++){
            this.gridPane.getRowConstraints().add(new RowConstraints(20));
        }
        drawMap();
    }

    public GridPane getGridPane(){ return this.gridPane;}
    public VBox getStats(){return this.stats;}

    private void drawMap(){
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

    private void drawObjects()  {
        ArrayList<Animal> animals = engine.getAnimals();
        for(Animal animal : animals){
            Label a = new Label("A");
            gridPane.add(a, animal.getPosition().x, animal.getPosition().y);
        }
        ArrayList<Grass> grass = engine.getGrass();
        for(Grass gras : grass){
            Label g= new Label("#");
            gridPane.add(g, gras.getPosition().x, gras.getPosition().y);
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
                if (pos.follows(lowerLeftJungle) && pos.precedes(upperRightJungle)) {
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

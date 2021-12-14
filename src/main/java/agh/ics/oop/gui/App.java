package agh.ics.oop.gui;
import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.util.ArrayList;


public class App extends Application {
    private Simulation engine;
    private RightMap rightMap;
    private GridPane gridPane;
    private Stage stage;


    public void start(Stage primaryStage) throws Exception {
        agh.ics.oop.Parameters param = new agh.ics.oop.Parameters();
        gridPane = new GridPane();
        stage = primaryStage;
        engine = new Simulation(param ,this );
        init();
    }

    @Override
    public void init() throws Exception {
        Thread engineThread = new Thread(engine);
        engineThread.start();
    }
    public void drawMap(){
        Platform.runLater(() ->{
            gridPane.getChildren().clear();
            this.gridPane = new GridPane();
            drawObjects();
            gridPane.setGridLinesVisible(true);
            stage.setScene(new Scene(gridPane, 400,400));
            stage.show();
        });
    }

    private void drawObjects(){
        ArrayList<Animal> animals = engine.getAnimals();
        for(Animal animal : animals){
            Label a = new Label("A");
            gridPane.add(a, animal.getPosition().x, animal.getPosition().y);
            gridPane.getRowConstraints().add(new RowConstraints(25));
            gridPane.getColumnConstraints().add(new ColumnConstraints(25));
        }
        ArrayList<Grass> grass = engine.getGrass();
        for(Grass gras : grass){
            Label g = new Label("*");
            gridPane.add(g, gras.getPosition().x, gras.getPosition().y);
            gridPane.getRowConstraints().add(new RowConstraints(25));
            gridPane.getColumnConstraints().add(new ColumnConstraints(25));
        }
    }
}

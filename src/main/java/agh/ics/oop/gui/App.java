package agh.ics.oop.gui;
import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;


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
    public void init()  {
        Thread engineThread = new Thread(engine);
        engineThread.start();
    }
    public void drawMap(){
        Platform.runLater(() ->{
            gridPane.getChildren().clear();
            this.gridPane = new GridPane();

            drawObjects();

            //gridPane.setGridLinesVisible(true);
//            BorderPane borderPane = new BorderPane();
//            borderPane.centerProperty(gridPane);
            stage.setScene(new Scene(gridPane, 400,600));
            stage.show();
        });
    }

    private void drawObjects()  {
        ArrayList<Animal> animals = engine.getAnimals();
        coloringMap();
        for(Animal animal : animals){
            Label a = new Label("A");
            //till.getChildren().add(a);
            gridPane.add(a, animal.getPosition().x, animal.getPosition().y);
            gridPane.getRowConstraints().add(new RowConstraints(25));
            gridPane.getColumnConstraints().add(new ColumnConstraints(25));
        }
        ArrayList<Grass> grass = engine.getGrass();
        for(Grass gras : grass){
            Label g= new Label("#");
            gridPane.add(g, gras.getPosition().x, gras.getPosition().y);
            gridPane.getRowConstraints().add(new RowConstraints(25));
            gridPane.getColumnConstraints().add(new ColumnConstraints(25));
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
                Vector2d pos = new Vector2d(i,j);
                StackPane till = new StackPane();
                if(pos.follows(lowerLeftJungle) && pos.precedes(upperRightJungle)){
                    till.setBackground(new Background(new BackgroundFill(Color.DARKGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                    gridPane.add(till, i, j);
                }
                else{
                    till.setBackground(new Background(new BackgroundFill(Color.OLIVEDRAB, CornerRadii.EMPTY, Insets.EMPTY)));
                    gridPane.add(till,i,j);
                }
            }
        }
    }
}

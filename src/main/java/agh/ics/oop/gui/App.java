package agh.ics.oop.gui;
import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.swing.border.Border;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;


public class App extends Application {
    private Simulation engine;
    private GridPane gridPane;
    private GridPane secondGridPane;
    private Simulation secondEngine;
    private agh.ics.oop.Parameters param;
    private BorderPane border;
    private VBox stats;
    private VBox secondStats;



    public void start(Stage primaryStage) throws Exception {
        border = new BorderPane();
        primaryStage.setTitle("Evolution");
        primaryStage.setScene(new Scene(border, 600,600));
        primaryStage.show();
    }

    @Override
    public void init()  {
        agh.ics.oop.Parameters param = new agh.ics.oop.Parameters();
        engine = new Simulation(param ,this );
        secondEngine = new Simulation(param, this);
        Thread engineThread = new Thread(engine);
        Thread secondEngineThread = new Thread(secondEngine);
        engineThread.start();
        secondEngineThread.start();
    }

    public void showMap(){
        Platform.runLater(() ->{
            borderClear();
            drawMap Map = new drawMap(engine);
            drawMap secondMap = new drawMap(secondEngine);
            gridPane = Map.getGridPane();
            secondGridPane = secondMap.getGridPane();
            stats = Map.getStats();
            secondStats = secondMap.getStats();
            VBox right = new VBox(gridPane, stats);
            border.setCenter(right);
            border.setAlignment(gridPane, Pos.CENTER);
            VBox left =  new VBox(secondGridPane, secondStats);
            border.setLeft(left);
            border.setMargin(left, new Insets(20,20,0,30));
            border.setMargin(right, new Insets(20,20,0,30));
        });
    }

    private void borderClear(){
        border.setCenter(null);
        border.setLeft(null);
    }

}

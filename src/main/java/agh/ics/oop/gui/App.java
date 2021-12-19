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
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;


public class App extends Application {
    private Simulation engine;
    private GridPane gridPane;
    private GridPane secondGridPane;
    private Simulation secondEngine;
    private GetParameters param;
    private BorderPane border = new BorderPane();
    private VBox stats;
    private VBox secondStats;

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Evolution");
        primaryStage.setScene(new Scene(border, 600,600));
        primaryStage.show();
    }

    @Override
    public void init() {
        getParam();


    }

    public void getParam(){
        VBox listOfDate = new VBox();
        TextField w = new TextField("Width");
        TextField h = new TextField("Height");
        TextField j = new TextField("Jungle ratio");
        TextField c = new TextField("Calories of grass");
        TextField n = new TextField("Number of animal");
        TextField s = new TextField("Start Energy");
        TextField e = new TextField("Energy to move");

        w.setMaxWidth(120);
        h.setMaxWidth(120);
        j.setMaxWidth(120);
        c.setMaxWidth(120);
        n.setMaxWidth(120);
        s.setMaxWidth(120);
        e.setMaxWidth(120);
        w.setStyle("-fx-control-inner-background: #85DABD ");


        Button getDate = new Button("CONFIRM");
        getDate.setStyle("-fx-control-inner-background: #85DABD ");
        listOfDate.getChildren().addAll(w,h,j,c,n,s,e, getDate);
        listOfDate.setSpacing(10);
        getDate.setOnAction(actionEvent -> {
            int width = Integer.parseInt(w.getText());
            int height = Integer.parseInt(h.getText());
            float jungleRatio = Float.parseFloat(j.getText());
            int caloriesGrass = Integer.parseInt(c.getText());
            int numberOfAnimals = Integer.parseInt(n.getText());
            int startEnergy = Integer.parseInt(s.getText());
            int energyToMOve = Integer.parseInt(e.getText());
            param = new GetParameters(width,height,jungleRatio,caloriesGrass,numberOfAnimals,startEnergy,energyToMOve);
            engine = new Simulation(param ,this, "RIGHT" );
            secondEngine = new Simulation(param, this, "LEFT");
            Thread engineThread = new Thread(engine);
            Thread secondEngineThread = new Thread(secondEngine);
            engineThread.start();
            secondEngineThread.start();
        });
        Label H = new Label("Enter the width of the map ");
        Label W = new Label("Enter the height of the map ");
        Label J = new Label("Enter the Jungle Ratio  ");
        Label C = new Label("Enter the energy of eating grass  ");
        Label N = new Label("Enter the number of Animal ");
        Label S = new Label("Enter the start energy " );
        Label E = new Label("Enter the energy to move ");

        Label title = new Label("The mystery of the beginning of all things is unsolved, but you can try... ");
        title.setStyle("-fx-font-weight: bold");
        title.setFont(new Font(15));
        VBox list = new VBox();
        list.getChildren().addAll(W,H, J, C, N, S, E);
        list.setAlignment(Pos.TOP_LEFT);
        list.setSpacing(18);
        border.setCenter(listOfDate);
        border.setAlignment(listOfDate, Pos.CENTER);
        border.setLeft(list);
        border.setAlignment(list, Pos.CENTER);
        border.setTop(title);
        border.setAlignment(title, Pos.CENTER);
        border.setMargin(title,new Insets(20,0,20,0));
        border.setMargin(list, new Insets(0,10,0,20));
    }

    public void showMap(){
        Platform.runLater(() ->{
            borderClear();
            UpdateMap Map = null;
            try {
                Map = new UpdateMap(engine);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            UpdateMap secondMap = null;
            try {
                secondMap = new UpdateMap(secondEngine);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Label title = new Label("The mystery of the beginning of all things is unsolved, but you can try... ");
            title.setStyle("-fx-font-weight: bold");
            title.setFont(new Font(15));
            gridPane = Map.getGridPane();
            secondGridPane = secondMap.getGridPane();
            stats = Map.getStats();
            secondStats = secondMap.getStats();
            VBox right = new VBox(gridPane, stats);
            border.setCenter(right);
            border.setAlignment(gridPane, Pos.CENTER);
            VBox left =  new VBox(secondGridPane, secondStats);
            border.setLeft(left);
            border.setTop(title);
            border.setTop(title);
            border.setAlignment(title, Pos.CENTER);
            border.setMargin(title,new Insets(20,0,20,0));
            border.setMargin(left, new Insets(20,20,0,30));
            border.setMargin(right, new Insets(20,20,0,30));
        });
    }

    private void borderClear(){
        border.getChildren().clear();
    }

}

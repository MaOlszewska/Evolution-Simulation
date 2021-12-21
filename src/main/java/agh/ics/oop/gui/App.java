package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileInputStream;
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
    private Button start = new Button("START");
    private Button Start = new Button("START/STOP");
    private Stage stage;


    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.setTitle("Evolution");
        stage.setScene(new Scene(border, 1200,750));
        stage.show();
    }

    @Override
    public void init() throws FileNotFoundException {
        getParam();
    }

    public void getParam() throws FileNotFoundException {
        VBox listOfDate = new VBox();
        TextField w = new TextField("10");
        TextField h = new TextField("10");
        TextField j = new TextField("0.5");
        TextField c = new TextField("50");
        TextField n = new TextField("30");
        TextField s = new TextField("100");
        TextField e = new TextField("5");
        TextField mL = new TextField("false");
        TextField mR = new TextField("false");

        w.setMaxWidth(120);w.setStyle("-fx-background-color: #00ce8e; ");
        h.setMaxWidth(120);h.setStyle("-fx-background-color: #00ce8e; ");
        j.setMaxWidth(120);j.setStyle("-fx-background-color: #00ce8e; ");
        c.setMaxWidth(120);c.setStyle("-fx-background-color: #00ce8e; ");
        n.setMaxWidth(120);n.setStyle("-fx-background-color: #00ce8e; ");
        s.setMaxWidth(120);s.setStyle("-fx-background-color: #00ce8e; ");
        e.setMaxWidth(120);e.setStyle("-fx-background-color: #00ce8e; ");
        mL.setMaxWidth(120);e.setStyle("-fx-background-color: #00ce8e; ");
        mR.setMaxWidth(120);e.setStyle("-fx-background-color: #00ce8e; ");

        Button getDate = new Button("CONFIRM");

        getDate.setStyle("-fx-background-color: #d79097; ");
        listOfDate.getChildren().addAll(w,h,j,c,n,s,e,mL, mR, getDate);
        listOfDate.setSpacing(10);


        Label H = new Label("Enter the width of the map ");
        Label W = new Label("Enter the height of the map ");
        Label J = new Label("Enter the Jungle Ratio  ");
        Label C = new Label("Enter the energy of eating grass  ");
        Label N = new Label("Enter the number of Animal ");
        Label S = new Label("Enter the start energy " );
        Label E = new Label("Enter the energy to move ");
        Label ML = new Label("Enter true if left map is magic  ");
        Label MR = new Label("Enter true if right map is magic ");

        Label title = new Label("The mystery of the beginning of all things is unsolved, but you can try... ");
        title.setStyle("-fx-font-weight: bold");
        title.setFont(new Font(15));
        VBox list = new VBox();
        list.getChildren().addAll(W,H, J, C, N, S, E, ML, MR);
        list.setSpacing(18);
        HBox lists = new HBox();
        lists.getChildren().addAll(list, listOfDate);
        lists.setAlignment(Pos.TOP_CENTER);
        Image worldIamge = new Image(new FileInputStream("src/main/resources/world.png"));
        ImageView imageView = new ImageView(worldIamge);
        border.setAlignment(imageView, Pos.TOP_CENTER);
        imageView.setFitWidth(300);
        imageView.setFitHeight(300);
        border.setBottom(imageView);
        border.setCenter(lists);
        border.setTop(title);
        border.setAlignment(title, Pos.CENTER);
        border.setMargin(title,new Insets(20,0,20,0));
        border.setBackground(new Background(new BackgroundFill(Color.PALETURQUOISE, CornerRadii.EMPTY, Insets.EMPTY)));
        getDate.setOnAction(actionEvent -> {
            borderClear();
            int width = Integer.parseInt(w.getText());
            int height = Integer.parseInt(h.getText());
            float jungleRatio = Float.parseFloat(j.getText());
            int caloriesGrass = Integer.parseInt(c.getText());
            int numberOfAnimals = Integer.parseInt(n.getText());
            int startEnergy = Integer.parseInt(s.getText());
            int energyToMOve = Integer.parseInt(e.getText());
            boolean magicLeft = Boolean.parseBoolean(mL.getText());
            boolean magicRight = Boolean.parseBoolean(mR.getText());
            border.setCenter(start);
            param = new GetParameters(width,height,jungleRatio,caloriesGrass,numberOfAnimals,startEnergy,energyToMOve, magicLeft, magicRight);
            engine = new Simulation(param ,this, "RIGHT" );
            secondEngine = new Simulation(param, this, "LEFT");
            startApp();

        });
    }


    public void startApp(){
        Thread engineThread = new Thread(engine);
        Thread secondEngineThread = new Thread(secondEngine);
        start.setStyle("-fx-background-color: #d79097; ");
        border.setCenter(start);
        start.setOnAction(action->{
            engine.changeStatus();
            secondEngine.changeStatus();
            engineThread.start();
            secondEngineThread.start();
        });
    }

    public void showMagic(){
        Platform.runLater(() ->{
            Button magicButton = new Button("WOW! You get 5 magic animals!");
            border.setCenter(magicButton);
            magicButton.setOnAction(action ->{
                engine.changeStatus();
                secondEngine.changeStatus();
            });
        });
    }

    public void changeStatus(){
        engine.changeStatus();
        secondEngine.changeStatus();
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
            border.setRight(right);
            VBox left =  new VBox(secondGridPane, secondStats);
            border.setLeft(left);
            border.setTop(title);
            Button exitButton = new Button("EXIT");
            HBox buttons = new HBox();
            buttons.setSpacing(10);
            buttons.getChildren().addAll(Start, exitButton);
            border.setBottom(buttons);
            buttons.setAlignment(Pos.CENTER);
            //border.setAlignment(buttons, Pos.CENTER);
            exitButton.setStyle("-fx-background-color: #d79097; ");
            Start.setStyle("-fx-background-color: #d79097; ");
            Start.setOnAction(action -> {
                engine.changeStatus();
                secondEngine.changeStatus();
            });
            exitButton.setOnAction(action ->{
                stage.close();
            });
            BarChartMaps chart = new BarChartMaps(engine.getStatistics(), secondEngine.getStatistics());
            BarChart barChart = chart.getBarChart();
            border.setCenter(barChart);
            border.setAlignment(barChart,Pos.TOP_CENTER);
            border.setAlignment(title, Pos.CENTER);
            border.setMargin(title,new Insets(20,0,20,0));
            border.setMargin(left, new Insets(20,20,0,30));
            border.setMargin(right, new Insets(20,20,0,30));
            border.setMargin(buttons, new Insets(10,0,10,0));
        });
    }

    private void borderClear(){
        border.getChildren().clear();
    }
}

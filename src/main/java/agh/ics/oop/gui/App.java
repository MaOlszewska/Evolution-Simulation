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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class App extends Application {
    private Simulation engineRight;
    private GridPane gridPaneRight;
    private GridPane gridPaneLeft;
    private Simulation engineLeft;
    private GetParameters params;
    private BorderPane border = new BorderPane();
    private VBox statsRight;
    private VBox statsLeft;
    private Button startButton = new Button("START");
    private Button buttonStartStop = new Button("START/STOP");
    private Button exitButton = new Button("EXIT");
    private StatisticFile fileRight;
    private StatisticFile fileLeft;


    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Evolution");
        primaryStage.setScene(new Scene(border, 1200,750));
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            changeStatus();
            try {
                fileRight.writeAveragedValues(engineRight.getStatistics());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fileLeft.writeAveragedValues(engineLeft.getStatistics());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void initBorder(){
        Label title = new Label("The mystery of the beginning of all things is unsolved, but you can try... ");
        title.setStyle("-fx-font-weight: bold");
        title.setFont(new Font(15));
        border.setTop(title);
        border.setAlignment(title, Pos.CENTER);
        border.setMargin(title,new Insets(20,0,20,0));
    }

    @Override
    public void init() throws IOException {
        initBorder();
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
        fileRight = new StatisticFile("src/main/resources/rightMapFile.txt");
        fileLeft = new StatisticFile("src/main/resources/leftMapFile.txt");
        w.setMaxWidth(120);w.setStyle("-fx-background-color: #00ce8e; ");
        h.setMaxWidth(120);h.setStyle("-fx-background-color: #00ce8e; ");
        j.setMaxWidth(120);j.setStyle("-fx-background-color: #00ce8e; ");
        c.setMaxWidth(120);c.setStyle("-fx-background-color: #00ce8e; ");
        n.setMaxWidth(120);n.setStyle("-fx-background-color: #00ce8e; ");
        s.setMaxWidth(120);s.setStyle("-fx-background-color: #00ce8e; ");
        e.setMaxWidth(120);e.setStyle("-fx-background-color: #00ce8e; ");
        mL.setMaxWidth(120);mL.setStyle("-fx-background-color: #00ce8e; ");
        mR.setMaxWidth(120);mR.setStyle("-fx-background-color: #00ce8e; ");

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
            border.setCenter(startButton);
            params = new GetParameters(width,height,jungleRatio,caloriesGrass,numberOfAnimals,startEnergy,energyToMOve, magicLeft, magicRight);
            engineRight = new Simulation(params,this, "RIGHT", fileRight );
            engineLeft = new Simulation(params, this, "LEFT", fileLeft);
            startApp();

        });
    }


    private void addButtons(){
        HBox buttons = new HBox();
        buttons.setSpacing(20);

        exitButton.setOnAction(action ->{
            changeStatus();
            Platform.exit();
            try {
                fileRight.writeAveragedValues(engineRight.getStatistics());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fileLeft.writeAveragedValues(engineLeft.getStatistics());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        buttonStartStop.setOnAction(action -> {
            engineRight.changeStatus();
            engineLeft.changeStatus();
        });

        buttons.getChildren().addAll(buttonStartStop, exitButton);
        buttons.setAlignment(Pos.CENTER);
        border.setAlignment(buttons, Pos.CENTER);
        exitButton.setStyle("-fx-background-color: #d79097; ");
        buttonStartStop.setStyle("-fx-background-color: #d79097; ");
        border.setMargin(buttons, new Insets(10,0,10,0));
        border.setBottom(buttons);

    }
    public void startApp(){
        Thread engineThread = new Thread(engineRight);
        Thread secondEngineThread = new Thread(engineLeft);
        startButton.setStyle("-fx-background-color: #d79097; ");
        border.setCenter(startButton);
        startButton.setOnAction(action->{
            engineRight.changeStatus();
            engineLeft.changeStatus();
            engineThread.start();
            secondEngineThread.start();
            addButtons();
        });
    }

    public void showMagic(){
        Platform.runLater(() ->{
            borderClear();
            Button magicButton = new Button(" WOW! There are 5 more magical animals! ");
            magicButton.setStyle("-fx-background-color: #d79097; ");
            border.setCenter(magicButton);
            magicButton.setOnAction(action ->{
                engineRight.changeStatus();
                engineLeft.changeStatus();
                addButtons();
            });
        });
    }

    public void changeStatus(){
        engineRight.changeStatus();
        engineLeft.changeStatus();
    }

    public void showMap(){
        Platform.runLater(() ->{
            borderClear();
            UpdateMap rightMap = null;
            UpdateMap leftMap = null;
            try {
                rightMap = new UpdateMap(engineRight);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                leftMap = new UpdateMap(engineLeft);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            gridPaneRight = rightMap.getGridPane();
            gridPaneLeft = leftMap.getGridPane();
            statsRight = rightMap.getStats();
            statsLeft =leftMap.getStats();

            VBox right = new VBox(gridPaneRight, statsRight);
            VBox left =  new VBox(gridPaneLeft, statsLeft);

            Chart chart = new Chart(engineRight.getStatistics(), engineLeft.getStatistics());
            javafx.scene.chart.BarChart barChart = chart.getBarChart();

            border.setLeft(left);
            border.setRight(right);
            border.setCenter(barChart);


            border.setAlignment(barChart,Pos.TOP_CENTER);
            border.setMargin(left, new Insets(20,20,0,30));
            border.setMargin(right, new Insets(20,20,0,30));

        });
    }

    private void borderClear(){
        border.setCenter(null);
        border.setRight(null);
        border.setLeft(null);
    }

}

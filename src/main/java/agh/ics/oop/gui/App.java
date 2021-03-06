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
    private final BorderPane border = new BorderPane();
    private Simulation engineRight;
    private Simulation engineLeft;
    private GridPane gridPaneRight;
    private GridPane gridPaneLeft;
    private StartParameters params;
    private VBox statsRight;
    private VBox statsLeft;
    private final Button startButton = new Button("START");
    private final Button exitButton = new Button("EXIT");
    private final Button buttonStartStopRight = new Button("START/STOP Right Map");
    private final Button buttonStartStopLeft = new Button("START/STOP Left Map");
    private final Button buttonEndTracking = new Button("END TRACKING");
    private StatisticFile fileRight;
    private StatisticFile fileLeft;
    private final Charts animalChart = new Charts("Animals");
    private final Charts grassChart = new Charts("Grass");
    private final Charts energyChart = new Charts("Average Energy");
    private final Charts avgLifeLength = new Charts("Average life length");

    public void start(Stage primaryStage) throws Exception{
        primaryStage.getIcons().add(new Image(new FileInputStream("src/main/resources/block.png")));
        primaryStage.setTitle("Evolution of the World");
        primaryStage.setScene(new Scene(border, 1200,800));
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            try {
                if(engineRight != null && engineLeft != null && engineLeft.getStatistics().getWorldDays() != 0) {
                    changeStatusSimulation();
                    fileRight.writeAveragedValues(engineRight.getStatistics());
                    fileLeft.writeAveragedValues(engineLeft.getStatistics());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.exit(0);
        });
    }

    private void initBorder(){
        Label title = new Label("The mystery of the beginning of all things is unsolved, but you can try... ");
        title.setStyle("-fx-font-weight: bold");
        title.setFont(new Font(25));
        border.setTop(title);
        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setMargin(title,new Insets(20,0,20,0));
    }

    @Override
    public void init() throws IOException {
        initBorder();
        fileRight = new StatisticFile("src/main/resources/rightMapFile.csv");
        fileLeft = new StatisticFile("src/main/resources/leftMapFile.csv");
        VBox listOfTextField = new VBox();
        TextField widthField = new TextField("5");
        TextField heightField = new TextField("5");
        TextField jungleRatioField = new TextField("0.5");
        TextField energyGrassField = new TextField("50");
        TextField numberOfAnimalsField = new TextField("15");
        TextField startEnergyField = new TextField("100");
        TextField energyToMoveField = new TextField("5");
        TextField magicLeftField = new TextField("false");
        TextField magicRightField = new TextField("false");
        widthField.setMaxWidth(120);widthField.setStyle("-fx-background-color: #00ce8e; ");
        heightField.setMaxWidth(120);heightField.setStyle("-fx-background-color: #00ce8e; ");
        jungleRatioField.setMaxWidth(120);jungleRatioField.setStyle("-fx-background-color: #00ce8e; ");
        energyGrassField.setMaxWidth(120);energyGrassField.setStyle("-fx-background-color: #00ce8e; ");
        numberOfAnimalsField.setMaxWidth(120);numberOfAnimalsField.setStyle("-fx-background-color: #00ce8e; ");
        startEnergyField.setMaxWidth(120);startEnergyField.setStyle("-fx-background-color: #00ce8e; ");
        energyToMoveField.setMaxWidth(120);energyToMoveField.setStyle("-fx-background-color: #00ce8e; ");
        magicLeftField.setMaxWidth(120);magicLeftField.setStyle("-fx-background-color: #00ce8e; ");
        magicRightField.setMaxWidth(120);magicRightField.setStyle("-fx-background-color: #00ce8e; ");

        Button getDate = new Button("CONFIRM");

        getDate.setStyle("-fx-background-color: #d79097; ");
        listOfTextField.getChildren().addAll(widthField, heightField, jungleRatioField, energyGrassField, numberOfAnimalsField,
                startEnergyField, energyToMoveField, magicLeftField, magicRightField, getDate);
        listOfTextField.setSpacing(10);

        Label widthLabel = new Label("Enter the height of the map ");
        Label heightLabel = new Label("Enter the width of the map ");
        Label jungleRatioLabel = new Label("Enter the Jungle Ratio  ");
        Label energyGrassLabel = new Label("Enter the energy of eating grass  ");
        Label numberOfAnimalsLabel = new Label("Enter the number of Animal ");
        Label startEnergyLabel = new Label("Enter the start energy " );
        Label energyToMoveLabel = new Label("Enter the energy to move ");
        Label magicLeftLabel = new Label("Enter true if left map is magic  ");
        Label magicRightLabel = new Label("Enter true if right map is magic ");

        VBox listOfLabel = new VBox();
        listOfLabel.getChildren().addAll(widthLabel, heightLabel, jungleRatioLabel, energyGrassLabel, numberOfAnimalsLabel,
                startEnergyLabel, energyToMoveLabel, magicLeftLabel, magicRightLabel);
        listOfLabel.setSpacing(18);

        HBox inputList = new HBox();
        inputList.getChildren().addAll(listOfLabel, listOfTextField);
        inputList.setAlignment(Pos.TOP_CENTER);

        Image worldImage = new Image(new FileInputStream("src/main/resources/world.png"));
        ImageView imageView = new ImageView(worldImage);
        imageView.setFitWidth(300);
        imageView.setFitHeight(300);
        BorderPane.setAlignment(imageView, Pos.TOP_CENTER);
        border.setBottom(imageView);
        border.setCenter(inputList);
        border.setBackground(new Background(new BackgroundFill(Color.PALETURQUOISE, CornerRadii.EMPTY, Insets.EMPTY)));

        getDate.setOnAction(actionEvent -> {
            borderClear();
            int width = Integer.parseInt(widthField.getText());
            int height = Integer.parseInt(heightField.getText());
            float jungleRatio = Float.parseFloat(jungleRatioField.getText());
            int caloriesGrass = Integer.parseInt(energyGrassField.getText());
            int numberOfAnimals = Integer.parseInt(numberOfAnimalsField.getText());
            int startEnergy = Integer.parseInt(startEnergyField.getText());
            int energyToMOve = Integer.parseInt(energyToMoveField.getText());
            boolean magicLeft = Boolean.parseBoolean(magicLeftField.getText());
            boolean magicRight = Boolean.parseBoolean(magicRightField.getText());
            border.setCenter(startButton);
            params = new StartParameters(width,height,jungleRatio,caloriesGrass,numberOfAnimals,startEnergy,energyToMOve, magicLeft, magicRight);
            engineRight = new Simulation(params,this, true, fileRight);
            engineLeft = new Simulation(params, this, false, fileLeft);
            startApp();
        });
    }

    private void addButtons(){
        HBox buttons = new HBox();
        buttons.setSpacing(300);
        buttonStartStopRight.setOnAction(action -> {
            engineRight.changeStatus();
        });

        buttonStartStopLeft.setOnAction(action -> {
            engineLeft.changeStatus();
        });

        HBox centerButtons = new HBox(exitButton, buttonEndTracking);
        centerButtons.setSpacing(15);
        exitButton.setOnAction(action ->{
            changeStatusSimulation();
            try {
                fileRight.writeAveragedValues(engineRight.getStatistics());
                fileLeft.writeAveragedValues(engineLeft.getStatistics());
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.exit(0);
        });
        buttonEndTracking.setOnAction(action -> {
            endTracking();
        });

        buttons.getChildren().addAll(buttonStartStopLeft,centerButtons, buttonStartStopRight);
        buttons.setAlignment(Pos.CENTER);
        border.setBottom(buttons);
        BorderPane.setAlignment(buttons, Pos.CENTER);
        BorderPane.setMargin(buttons, new Insets(10,0,10,0));
        exitButton.setStyle("-fx-background-color: #d79097; ");
        buttonStartStopLeft.setStyle("-fx-background-color: #d79097; ");
        buttonStartStopRight.setStyle("-fx-background-color: #d79097; ");
        buttonEndTracking.setStyle("-fx-background-color: #d79097; ");
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

    public void addMagicAnimals(){
        Platform.runLater(() ->{
            borderClear();
            Button magicButton = new Button(" WOW! There are 5 more magical animals! ");
            magicButton.setStyle("-fx-background-color: #d79097; ");
            border.setCenter(magicButton);
            magicButton.setOnAction(action ->{
                changeStatusSimulation();
                updateLeftMap();
                updateRightMap();
                addButtons();
            });
        });
    }

    public void showAnimalGenotype(String genotype){
        Platform.runLater(() ->{
            stopSimulation();
            borderClear();
            Label text = new Label("Genotype of clicked animal is: ");
            text.setFont(new Font(15));
            Label genotypeString = new Label(genotype);
            genotypeString.setStyle("-fx-font-weight: bold");
            genotypeString.setFont(new Font(25));
            Button okButton = new Button("OK");
            okButton.setStyle("-fx-background-color: #d79097; ");
            VBox box = new VBox();
            box.getChildren().addAll(text, genotypeString, okButton);
            box.setSpacing(10);
            border.setCenter(box);
            box.setAlignment(Pos.CENTER);
            okButton.setOnAction(action ->{
                changeStatusSimulation();
                updateRightMap();
                updateLeftMap();
                addButtons();
            });
        });
    }

    public void updateLeftMap(){
        Platform.runLater(() -> {
            border.setLeft(null);
            UpdateMap leftMap = null;
            leftMap = new UpdateMap(engineLeft, this);

            gridPaneLeft = leftMap.getGridPane();
            statsLeft =leftMap.getStats();
            VBox left =  new VBox(gridPaneLeft, statsLeft);
            border.setLeft(left);
            left.setSpacing(10);
            BorderPane.setMargin(left, new Insets(20,20,0,30));

            grassChart.updateGrassChart(engineRight.getStatistics(), engineLeft.getStatistics());
            animalChart.updateAnimalsChart(engineRight.getStatistics(), engineLeft.getStatistics());
            energyChart.updateAvgEnergy(engineRight.getStatistics(), engineLeft.getStatistics());
            avgLifeLength.updateAvgLifeLength(engineRight.getStatistics(), engineLeft.getStatistics());
            VBox charts = new VBox(animalChart.getChart(), grassChart.getChart(), energyChart.getChart(), avgLifeLength.getChart());
            border.setCenter(charts);

            if(engineLeft.getIfTrackedAnimal()){
                Label deathDay;
                Label numberOfChildren = new Label("Number of children tracked animal: " + engineLeft.getChildrenOfTrackedAnimal());
                if(engineLeft.getDeathDay() == 0) {deathDay = new Label("Tracked animal is still alive ");}
                else {deathDay = new Label("Day of death of tracked Animal: " + engineLeft.getDeathDay());}
                numberOfChildren.setStyle("-fx-font-weight: bold");
                deathDay.setStyle("-fx-font-weight: bold");
                left.setAlignment(Pos.CENTER);
                left.getChildren().addAll(numberOfChildren, deathDay);
            }
        });
    }

    public void updateRightMap(){
        Platform.runLater(() -> {
            border.setRight(null);
            UpdateMap rightMap = null;
            rightMap = new UpdateMap(engineRight, this);

            gridPaneRight = rightMap.getGridPane();
            statsRight = rightMap.getStats();
            VBox right = new VBox(gridPaneRight, statsRight);
            border.setRight(right);
            right.setSpacing(10);
            BorderPane.setMargin(right, new Insets(20,20,0,30));

            grassChart.updateGrassChart(engineRight.getStatistics(), engineLeft.getStatistics());
            animalChart.updateAnimalsChart(engineRight.getStatistics(), engineLeft.getStatistics());
            energyChart.updateAvgEnergy(engineRight.getStatistics(), engineLeft.getStatistics());
            avgLifeLength.updateAvgLifeLength(engineRight.getStatistics(), engineLeft.getStatistics());
            VBox charts = new VBox(animalChart.getChart(), grassChart.getChart(), energyChart.getChart(), avgLifeLength.getChart());
            border.setCenter(charts);

            if(engineRight.getIfTrackedAnimal()){
                Label deathDay;
                Label numberOfChildren = new Label("Number of children tracked animal: " + engineRight.getChildrenOfTrackedAnimal());
                if(engineRight.getDeathDay() == 0) {deathDay = new Label("Tracked animal is still alive ");}
                else {deathDay = new Label("Day of death of tracked Animal: " + engineRight.getDeathDay());}
                numberOfChildren.setStyle("-fx-font-weight: bold");
                deathDay.setStyle("-fx-font-weight: bold");
                right.setAlignment(Pos.CENTER);
                right.getChildren().addAll(numberOfChildren, deathDay);
            }
        });
    }

    public void stopSimulation(){
        engineRight.stopStatus();
        engineLeft.stopStatus();
    }

    public void changeStatusSimulation(){
        engineRight.changeStatus();
        engineLeft.changeStatus();
    }

    public void changeStatusTrackedLeft(){
        engineRight.removeStatusTracked();
        engineLeft.addStatusTracked();
    }

    public void changeStatusTrackedRight(){
        engineRight.addStatusTracked();
        engineLeft.removeStatusTracked();
    }

    public void endTracking(){
        engineRight.removeStatusTracked();
        engineLeft.removeStatusTracked();
    }

    private void borderClear(){
        border.setCenter(null);
        border.setRight(null);
        border.setLeft(null);
    }
}
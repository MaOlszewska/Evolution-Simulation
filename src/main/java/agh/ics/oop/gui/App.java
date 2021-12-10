package agh.ics.oop.gui;
import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.ArrayList;


public class App extends Application {
    GridPane gridPane = new GridPane();

    public void start(Stage primaryStage) throws FileNotFoundException {
        try{
            Object[] args = getParameters().getRaw().toArray();
            MoveDirection[] directions = new OptionsParser().parse(args);
            IWorldMap map = new GrassField(10);
            //IWorldMap map = new RectangularMap(10,10);
            Vector2d[] positions = { new Vector2d(2,2), new Vector2d(3,4) };
            SimulationEngine engine = new SimulationEngine(directions, map, positions);
            engine.run();
            System.out.println(map);

            AbstractWorldMap abstractMap = (AbstractWorldMap) map;
            ArrayList<IMapElement> mapElements = abstractMap.getMapElements();
            Vector2d upperRight = abstractMap.getMapBoundary().getRightCorner();
            Vector2d lowerLeft = abstractMap.getMapBoundary().getLeftCorner();

            drawFrame(upperRight,lowerLeft);
            drawObjects(mapElements, lowerLeft, upperRight);

            gridPane.setGridLinesVisible(true);
            Scene scene = new Scene(gridPane, 400, 400);
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch (IllegalArgumentException ex){System.out.println(ex);}
    }

    private void drawFrame(Vector2d upperRight, Vector2d lowerLeft){
        Label label = new Label("y/x");
        gridPane.add(label,0,0);
        gridPane.getColumnConstraints().add(new ColumnConstraints(20));
        gridPane.getRowConstraints().add(new RowConstraints(20));
        GridPane.setHalignment(label, HPos.CENTER);

        int i = lowerLeft.x;
        int position = 1;
        while(i < upperRight.x + 1){
            Label number = new Label(Integer.toString(i));
            gridPane.add(number, position, 0);
            gridPane.getColumnConstraints().add(new ColumnConstraints(35));
            GridPane.setHalignment(number, HPos.CENTER);
            position++;
            i++;
        }

        i = upperRight.y;
        position = 1;
        while(i >= lowerLeft.y){
            Label number = new Label(Integer.toString(i));
            gridPane.add(number, 0,  position);
            gridPane.getRowConstraints().add(new RowConstraints(35));
            GridPane.setHalignment(number, HPos.CENTER);
            position++;
            i--;
        }
    }

    private void drawObjects(ArrayList<IMapElement> mapElements, Vector2d lowerLeft, Vector2d upperRight) throws FileNotFoundException {
        for(IMapElement element : mapElements) {
            if(element instanceof Animal) {
                VBox animal = new GuiElementBox(element).getvBox();
                gridPane.add(animal, element.getPosition().x - lowerLeft.x + 1 , upperRight.y - element.getPosition().y + 1);
            }
            else {
                VBox grass = new GuiElementBox(element).getvBox();
                gridPane.add(grass, element.getPosition().x - lowerLeft.x + 1, upperRight.y - element.getPosition().y + 1);
            }
        }
    }



}

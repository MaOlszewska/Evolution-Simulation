package agh.ics.oop.gui;

import agh.ics.oop.Statistics;
import javafx.scene.Node;
import javafx.scene.chart.*;

public class Charts{

    private LineChart chart;
    private  XYChart.Series rightMapSeries;
    private  XYChart.Series leftMapSeries;
    public Charts(String string){
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Day");
        this.chart = new LineChart(xAxis, yAxis);
        rightMapSeries = new XYChart.Series();
        leftMapSeries = new XYChart.Series();

        chart.setTitle(string);
        rightMapSeries.setName( "Right");
        leftMapSeries.setName( "Left");
        chart.setCreateSymbols(false);
        chart.lookup(".chart-legend").setStyle("-fx-background-color: transparent;");
    }

    public LineChart getChart(){return chart;}

    public void updateAnimalsChart(Statistics stats, Statistics leftStats){
        leftMapSeries.getData().add(new XYChart.Data(leftStats.getWorldDays(), leftStats.getNumberOfAliveAnimals()));
        rightMapSeries.getData().add(new XYChart.Data(stats.getWorldDays(),stats.getNumberOfAliveAnimals()));
        chart.getData().add(leftMapSeries);
        chart.getData().add(rightMapSeries);
    }

    public void updateGrassChart(Statistics stats, Statistics leftStats){
        leftMapSeries.getData().add(new XYChart.Data(leftStats.getWorldDays(), leftStats.getNumberOfGrass()));
        rightMapSeries.getData().add(new XYChart.Data(stats.getWorldDays(),stats.getNumberOfGrass()));
        chart.getData().add(leftMapSeries);
        chart.getData().add(rightMapSeries);
    }


}

package agh.ics.oop.gui;

import agh.ics.oop.Statistics;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class Chart {
    private Statistics statisticRight;
    private Statistics statisticLeft;
    private javafx.scene.chart.BarChart barChart;

    public Chart(Statistics statsRight, Statistics statsLeft){
        this.statisticRight = statsRight;
        this.statisticLeft = statsLeft;
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis(0,Math.max(statisticLeft.getMax(), statisticRight.getMax()) + 5,20);
        xAxis.setTickMarkVisible(true);
        //yAxis.setTickMarkVisible(true);
        this.barChart = new javafx.scene.chart.BarChart(xAxis, yAxis);
        barChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent;");
        createBarChart();

    }

    private void createBarChart(){

        XYChart.Series<String,Float> seriesAnimals = new XYChart.Series<>();
        seriesAnimals.setName("Animals");
        seriesAnimals.getData().add(new XYChart.Data("LeftMap",statisticLeft.getNumberOfAliveAnimals()));
        seriesAnimals.getData().add(new XYChart.Data("RightMap", statisticRight.getNumberOfAliveAnimals() ));


        XYChart.Series<String,Float> seriesDeadAnimals = new XYChart.Series<>();
        seriesDeadAnimals.setName("Dead Animal");
        seriesDeadAnimals.getData().add(new XYChart.Data("LeftMap",statisticLeft.getNumberOfDeadAnimals()));
        seriesDeadAnimals.getData().add(new XYChart.Data("RightMap", statisticRight.getNumberOfDeadAnimals() ));

        XYChart.Series<String,Float> seriesGrass = new XYChart.Series<>();
        seriesGrass.setName("Grass");
        seriesGrass.getData().add(new XYChart.Data("LeftMap",statisticLeft.getNumberOfGrass()));
        seriesGrass.getData().add(new XYChart.Data("RightMap", statisticRight.getNumberOfGrass() ));

        XYChart.Series<String,Float> seriesEnergy = new XYChart.Series<>();
        seriesEnergy.setName("Energy");
        seriesEnergy.getData().add(new XYChart.Data("LeftMap",statisticLeft.getAvgEnergy()));
        seriesEnergy.getData().add(new XYChart.Data("RightMap", statisticRight.getAvgEnergy() ));

        barChart.setLegendSide(Side.BOTTOM);
        barChart.lookup(".chart-legend").setStyle("-fx-background-color: transparent;");
        barChart.getData().addAll(seriesAnimals, seriesDeadAnimals, seriesGrass, seriesEnergy);
        //barChart.lookup(".default-color0.chart-bar").setStyle("-fx-bar-fill: #d79097;");
    }

    public javafx.scene.chart.BarChart getBarChart(){return this.barChart;}

}

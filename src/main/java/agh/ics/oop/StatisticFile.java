package agh.ics.oop;

import java.io.*;

public class StatisticFile {
    private File statisticMapFile;
    private int sumAnimals;
    private int sumGrass;
    private int sumAvgEnergy;
    private int sumLengthLife;
    private int sumAvgChildren;

    public StatisticFile(String path) throws IOException {
        this.statisticMapFile = new File(path);
        BufferedWriter bw = null;
        bw = new BufferedWriter(new FileWriter(statisticMapFile));
        bw.write("day,number of animals,number of grass,avg energy,length of life,avg children");
        bw.newLine();bw.flush();bw.close();
    }

    public void writeDataInFileMap(Statistics stats) throws IOException {
        BufferedWriter bw = null;
        updatedAveragedValues(stats);
        bw = new BufferedWriter(new FileWriter(statisticMapFile));
        bw.write(stats.getWorldDays() + "," +
                stats.getNumberOfAliveAnimals() + "," +
                stats.getNumberOfGrass() + "," +
                stats.getAvgEnergy() + "," +
                stats.getAvgLifeDaysOfDeadAnimal() + "," +
                stats.getAvgChildren());
        bw.newLine();bw.flush();bw.close();
    }

    private void updatedAveragedValues(Statistics stats){
        sumAnimals += stats.getNumberOfAliveAnimals();
        sumGrass += stats.getNumberOfGrass();
        sumAvgEnergy += stats.getAvgEnergy();
        sumLengthLife += stats.getAvgLifeDaysOfDeadAnimal();
        sumAvgChildren += stats.getAvgChildren();
    }
    public void writeAveragedValues(Statistics stats) throws IOException {
        BufferedWriter bw = null;
        int numberOfDays = stats.getWorldDays();
        bw = new BufferedWriter(new FileWriter(statisticMapFile));
        bw.write(sumAnimals / numberOfDays  + "," +
                sumGrass / numberOfDays  + "," +
                sumAvgEnergy / numberOfDays + "," +
                sumLengthLife / numberOfDays  + "," +
                sumAvgChildren / numberOfDays);
        bw.newLine();bw.flush();bw.close();
    }
}

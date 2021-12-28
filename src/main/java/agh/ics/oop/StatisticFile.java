package agh.ics.oop;

import java.io.*;

public class StatisticFile {
    private final File statisticMapFile;
    private int sumAnimals;
    private int sumGrass;
    private int sumAvgEnergy;
    private int sumLengthLife;
    private int sumAvgChildren;

    public StatisticFile(String path) throws IOException {
        this.statisticMapFile = new File(path);
        BufferedWriter bw;
        bw = new BufferedWriter(new FileWriter(statisticMapFile));
        bw.write("day,number of animals,number of grass,avg energy,length of life,avg children");
        bw.newLine();
        bw.flush();
        bw.close();
    }

    public void writeDataInFile(Statistics stats) throws IOException {
        BufferedWriter bw;
        updatedAveragedValues(stats);
        bw = new BufferedWriter(new FileWriter(statisticMapFile, true));
        bw.write(stats.getWorldDays() + "," + stats.getNumberOfAliveAnimals()+ ","+
                stats.getNumberOfGrass() + "," + stats.getAvgEnergy() + "," + stats.getAvgLifeLength()
                + "," + stats.getAvgChildren());
        bw.newLine();
        bw.flush();
        bw.close();
    }

    private void updatedAveragedValues(Statistics stats){
        sumAnimals += stats.getNumberOfAliveAnimals();
        sumGrass += stats.getNumberOfGrass();
        sumAvgEnergy += stats.getAvgEnergy();
        sumLengthLife += stats.getAvgLifeLength();
        sumAvgChildren += stats.getAvgChildren();
    }

    public void writeAveragedValues(Statistics stats) throws IOException {
        BufferedWriter bw;
        int numberOfDays = stats.getWorldDays();
        bw = new BufferedWriter(new FileWriter(statisticMapFile, true));
        bw.write(" ," + sumAnimals/numberOfDays  + "," + sumGrass / numberOfDays  + "," + sumAvgEnergy / numberOfDays
                +","+ sumLengthLife / numberOfDays  + "," + sumAvgChildren / numberOfDays);
        bw.flush();
        bw.close();
    }
}

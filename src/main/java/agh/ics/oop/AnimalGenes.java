package agh.ics.oop;

import java.util.Arrays;
import java.util.Random;

public class AnimalGenes {

    private final int [] genes ;

    public AnimalGenes(){
        this.genes = new int[32];
        createStructureDNA();
    }

    public AnimalGenes(int[] gen){
        this.genes = gen;
    }

    public int[] getGenes(){
        return this.genes;
    }

    public void createStructureDNA(){
        Random random = new Random();
        for(int i = 0; i < 32; i++ ){
            genes[i] = random.nextInt(8);
        }
        Arrays.sort(genes);
    }

    public int selectMovement(){
        Random random = new Random(); return genes[random.nextInt(32)];}

    public String getString(){
        StringBuilder gen = new StringBuilder();
        for(int i = 0; i < 32; i++){
            gen.append(this.genes[i]);
        }
        return gen.toString();
    }
}

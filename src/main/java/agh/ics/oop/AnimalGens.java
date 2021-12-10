package agh.ics.oop;

import java.util.Arrays;
import java.util.Random;

class AnimalGenes {

    private int [] genes ;
    private int genesNumber = 8;

    public AnimalGenes(){
        this.genes = new int[32];
        createStructureDNA();
    }

    public void createStructureDNA(){
        Random random = new Random();
        for(int i = 0; i < genesNumber; i++ ){
            genes[i] = random.nextInt(7);
        }
        Arrays.stream(genes).sorted();
    }


    public int selectMovemnet(){
        Random random = new Random();
        return genes[random.nextInt(32)];
    }
}

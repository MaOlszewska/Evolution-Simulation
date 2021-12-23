package agh.ics.oop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AnimalGenesTest {

    @Test
    public void createStructureDNATest(){
        AnimalGenes genes = new AnimalGenes();
        int[] gen = genes.getGenes();
        assertTrue(gen.length == 32);
        for(int i = 0; i < 32; i++){
            assertTrue(gen[i] >= 0 && gen[i] <= 7);
        }
    }

    @Test
    public void selectMovement(){
        AnimalGenes genes = new AnimalGenes();
        int genom = genes.selectMovement();
        assertTrue(genom >= 0 && genom <= 7);
    }

}

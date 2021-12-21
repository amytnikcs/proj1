package agh.ics.oop;
//crucial class creating random starting genes for our animals

import java.util.Arrays;
import java.util.Random;

public class GenesSoup{

    public int[] getRandomGenes(){
        Random random = new Random();
        int[] genesPool = random.ints(32,0,8).toArray();
        Arrays.sort(genesPool);
        return genesPool;
    }
}
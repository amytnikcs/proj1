package agh.ics.oop;


import java.util.Arrays;
import java.util.Random;

public class GenesSoup{ //some people may say GenesSoup started this project
    public int[] getRandomGenes(){
        Random random = new Random();
        int[] genesPool = random.ints(32,0,8).toArray();
        Arrays.sort(genesPool);
        return genesPool;
    }
    public Vector2d placeOfRandomGenes(int width,int height){
        Random random = new Random();
        return new Vector2d(random.nextInt(width), random.nextInt( height));
    }
}
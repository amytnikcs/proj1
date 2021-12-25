package agh.ics.oop;

import java.util.Arrays;
import java.util.List;

public class DataTracker {


    private double averageAnimalLiveSpan;
    private int numberOfDeadAnimals;
    private SimulationEngine engine;
    private BoundedWorldMap map;

    public DataTracker(SimulationEngine engine, BoundedWorldMap map){
        this.engine = engine;
        this.map = map;
        averageAnimalLiveSpan = 0;
        numberOfDeadAnimals = 0;
    }



    public double calculateAverageChildren(){
        double sum = 0;
        if(engine.getAnimals().size() == 0)
            return 0;
        for(Animal animal : engine.getAnimals()){
            sum += animal.getNumberOfChildren();
        }
        sum /= engine.getAnimals().size();
        return sum;
    }

    public double calculateAverageEnergy(){
        double sum = 0;
        if(engine.getAnimals().size() == 0)
            return 0;
        for(Animal animal : engine.getAnimals()){
            sum += animal.getEnergy();
        }
        sum /= engine.getAnimals().size();
        return sum;
    }


    public int[] findDominantGenes(){
        int[] dominantGenes = new int[32];
        int maxNumberOfOccurences = 0;
        for(int i = 0; i<engine.getAnimals().size();i++) {
            int numberOfOccurences = 1;
            for (int j = i + 1; j < engine.getAnimals().size(); j++){
                if (Arrays.equals(engine.getAnimals().get(i).getGenes(), engine.getAnimals().get(j).getGenes()))
                    numberOfOccurences++;
            }
            if(numberOfOccurences > maxNumberOfOccurences) {
                maxNumberOfOccurences = numberOfOccurences;
                dominantGenes = engine.getAnimals().get(i).getGenes();
            }
        }
        if(maxNumberOfOccurences > 0)
            return dominantGenes;
        return null;
    }

    public void animalDied(Animal animal){
        numberOfDeadAnimals++;
        calculateAverageAnimalLiveSpan(animal.getLivedDays());
    }

    public void calculateAverageAnimalLiveSpan(int lifeSpan){
        if(numberOfDeadAnimals == 0)
            return;
        averageAnimalLiveSpan = (averageAnimalLiveSpan * (numberOfDeadAnimals - 1) + lifeSpan) / numberOfDeadAnimals;
    }

    public double getAverageAnimalLiveSpan(){
        return averageAnimalLiveSpan;
    }

    public int getHowMuchGrassOnMap() {
        return map.getGrassAmount();
    }
}

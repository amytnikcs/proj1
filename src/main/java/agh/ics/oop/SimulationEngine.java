package agh.ics.oop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimulationEngine implements IAnimalLifeCycleObserver {
    private int magicCounter;
    private boolean wasItInMagicState;
    private Integer startingEnergy;
    private Integer moveEnergy;
    private Integer plantEnergy;
    private Integer initialNumberOfAnimals;
    private Integer width;
    private Integer height;
    private double jungleRatio;
    private BoundedWorldMap map;
    private GenesSoup genesSoup;
    private boolean isMagic;
    private List<Animal> magicAnimals;
    private List<Animal> animals;
    private double averageAnimalLiveSpan;
    private int numberOfDeadAnimals;


    public SimulationEngine(Integer startingEnergy, Integer moveEnergy, Integer plantEnergy,
                            Integer initialNumberOfAnimals, Integer width, Integer height, double jungleRatio, boolean isMagic){
        if(initialNumberOfAnimals > width*height) {
            System.out.println("PRZEKROCZONO MAX ZWIERZAT");
            initialNumberOfAnimals = width * height;
        }
        averageAnimalLiveSpan = 0;
        numberOfDeadAnimals = 0;
        animals = new ArrayList<>();
        magicCounter = 0;
        this.isMagic = isMagic;
        this.startingEnergy = startingEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.initialNumberOfAnimals = initialNumberOfAnimals;
        this.width = width;
        this.height = height;
        this.jungleRatio = jungleRatio;
        genesSoup = new GenesSoup();
        map = new BoundedWorldMap(this.width, this.height, this.jungleRatio);
        MapField mapField = new MapField(new Vector2d(0,0));
        mapField.setPlantEnergy(plantEnergy);
        map.addObserverDeath(this);
        //initialazing animals
        for(int i = 0; i < this.initialNumberOfAnimals; i++) {
            Animal animal = new Animal(map, genesSoup.placeOfRandomGenes(width, height),
                    startingEnergy, genesSoup.getRandomGenes());
            if (this.map.initialPlace(animal)) {
                animals.add(animal);
                animal.setMoveEnergy(moveEnergy);
                animal.setStartEnergy(startingEnergy);
            } else
                i -= 1;
        }
    }

    public void run(){
        System.out.println(map.toString());

        while(animals.size() > 0){
            map.removeDeadAnimals();
            if(wasItInMagicState && isMagic)
                magicEvolution();

            if(isMagic && animals.size() == 5) {
                magicStateEnable();
                magicEvolution();
            }

            for(Animal animal : animals) {
                animal.move();
            }

            map.eatGrass();

            map.breedAnimals();
            if(wasItInMagicState && isMagic)
                magicEvolution();

            for(Animal animal : animals){
                animal.decreaseEnergy(moveEnergy);
                animal.anotherDayLived();
            }

            map.spawnGrass();
            System.out.println(Arrays.toString(findDominantGenes()));
            System.out.println(map.toString());
        }
    }

    @Override
    public void animalBorn(Animal animal) {
        animals.add(animal);
        if(animals.size() == 5 && isMagic)
            magicStateEnable();
    }

    @Override
    public void animalDied(Animal animal) {
        numberOfDeadAnimals++;
        calculateAverageAnimalLiveSpan(animal.getLivedDays());
        animals.remove(animal);
        if(animals.size() == 5 && isMagic)
            magicStateEnable();
    }

    public void magicEvolution(){
        if(magicCounter > 3){
            wasItInMagicState = false;
            return;
        }
        System.out.println("It's a kind of magic");
        for(int i = 0; i < 5; i++){
            Animal animal = new Animal(map, genesSoup.placeOfRandomGenes(width, height),
                    startingEnergy, magicAnimals.get(i).getGenes());
            if(this.map.initialPlace(animal)){
                animals.add(animal);
                animal.setMoveEnergy(moveEnergy);
            }
            else
                i -= 1;
        }
        magicCounter++;
        wasItInMagicState = false;
    }

    public void magicStateEnable(){
        if(magicCounter > 3){
            wasItInMagicState = false;
            return;
        }
        magicAnimals = animals;
        wasItInMagicState = true;
    }

    public double calculateAverageChildren(){
        double sum = 0;
        if(animals.size() == 0)
            return 0;
        for(Animal animal : animals){
            sum += animal.getNumberOfChildren();
        }
        sum /= animals.size();
        return sum;
    }

    public double calculateAverageEnergy(){
        double sum = 0;
        if(animals.size() == 0)
            return 0;
        for(Animal animal : animals){
            sum += animal.getEnergy();
        }
        sum /= animals.size();
        return sum;
    }

    public void calculateAverageAnimalLiveSpan(int lifeSpan){
        averageAnimalLiveSpan = (averageAnimalLiveSpan * (numberOfDeadAnimals - 1) + lifeSpan) / numberOfDeadAnimals;
    }

    public int[] findDominantGenes(){
        int[] dominantGenes = new int[32];
        int maxNumberOfOccurences = 0;
        for(int i = 0; i<animals.size();i++) {
            int numberOfOccurences = 1;
            for (int j = i + 1; j < animals.size(); j++){
                if (animals.get(i).getGenes().equals(animals.get(j).getGenes()))
                    numberOfOccurences++;
            }
            if(numberOfOccurences > maxNumberOfOccurences)
                dominantGenes = animals.get(i).getGenes();
        }

        return dominantGenes;
    }
}

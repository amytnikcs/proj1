package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

public class SimulationEngine implements IAnimalDeathObserver {
    private Integer startingEnergy;
    private Integer moveEnergy;
    private Integer plantEnergy;
    private Integer initialNumberOfAnimals;
    private Integer width;
    private Integer height;
    private double jungleRatio;
    private BoundedWorldMap map;
    private GenesSoup genesSoup;
    private List<Animal> animals = new ArrayList<>();

    public SimulationEngine(Integer startingEnergy, Integer moveEnergy, Integer plantEnergy,
                            Integer initialNumberOfAnimals, Integer width, Integer height, double jungleRatio){
        if(initialNumberOfAnimals > width*height) {
            System.out.println("PRZEKROCZONO MAX ZWIERZAT");
            initialNumberOfAnimals = width * height;
        }
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
        for(int i = 0; i < this.initialNumberOfAnimals; i++){
            Animal animal = new Animal(map, genesSoup.placeOfRandomGenes(width, height),
                    startingEnergy, genesSoup.getRandomGenes());
            if(this.map.initialPlace(animal)){
                animals.add(animal);
                animal.setMoveEnergy(moveEnergy);
            }
            else
                i -= 1;
        }

        System.out.println(map.toString());
        int j = 0;
        while(animals.size() > 0){
            map.removeDeadAnimals();
            for(Animal animal : animals) {
                animal.move();
            }

            map.eatGrass();
            //map.breed
            for(Animal animal : animals){
                animal.decreaseEnergy();
            }
            map.spawnGrass();
            System.out.println(map.toString());

        }
    }

    @Override
    public void animalDied(Animal animal) {
        animals.remove(animal);
    }
}

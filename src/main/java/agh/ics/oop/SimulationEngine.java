package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

public class SimulationEngine {
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
        //initialazing animals
        for(int i = 0; i < this.initialNumberOfAnimals; i++){
            Animal animal = new Animal(map, genesSoup.placeOfRandomGenes(width, height),
                    startingEnergy, genesSoup.getRandomGenes());
            if(this.map.initialPlace(animal)){
                animals.add(animal);
            }
            else
                i -= 1;
        }
        System.out.println(map.toString());

        while(animals.size() > 0){

            //map.removeDeadAnimals();
            //map.moveAnimals();
            //    animal.decreaseEnergy();
            //}
            //map.checkWhoEats();
            //map.breed();
            //map.addGrass();
            System.out.println(map.toString());
            break;
        }

    }
}

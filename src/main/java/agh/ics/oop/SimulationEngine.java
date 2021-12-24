package agh.ics.oop;

import java.util.ArrayList;
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


    public SimulationEngine(Integer startingEnergy, Integer moveEnergy, Integer plantEnergy,
                            Integer initialNumberOfAnimals, Integer width, Integer height, double jungleRatio, boolean isMagic){
        if(initialNumberOfAnimals > width*height) {
            System.out.println("PRZEKROCZONO MAX ZWIERZAT");
            initialNumberOfAnimals = width * height;
        }
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
            }
            map.spawnGrass();
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

}

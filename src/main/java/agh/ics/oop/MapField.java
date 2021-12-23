package agh.ics.oop;

import java.util.*;


public class MapField implements IPositionChangeObserver{
    Comparator<Animal> Comparator = new Comparator<Animal>() {
        @Override
        public int compare(Animal Sheldon, Animal Leonard) { //animals have names, families, houses :D
            return Leonard.getEnergy().compareTo(Sheldon.getEnergy());
            }
        };
    private static int plantEnergy;
    private SortedSet<Animal> animalsOnField = new TreeSet<Animal>(Comparator);
    private Vector2d position;
    private Grass grass;
    private int numberOfAnimals;
    private List<IPositionChangeObserver> animals;

    public static void setPlantEnergy(int plantEnergy) {
        MapField.plantEnergy = plantEnergy;
    }

    public MapField(Vector2d position){
        this.animals = new ArrayList<>();
        this.position = position;
        numberOfAnimals = 0;
    }

    public void addObserver(IPositionChangeObserver Observer){
        animals.add(Observer);
    }

    public void removeObserver(IPositionChangeObserver Observer){
        animals.remove(Observer);
    }

    public void addAnimal(Animal animal){
        numberOfAnimals += 1;
        animalsOnField.add(animal);
        animal.addObserver(this);
    }

    public void removeAnimal(Animal animal){
        numberOfAnimals -= 1;
        animal.removeObserver(this);
        animalsOnField.remove(animal);
    }

    public void growGrass(){
        grass = new Grass();
    }

    public boolean containsGrass(){
        return grass != null;
    }

    @Override
    public String toString(){
        if(numberOfAnimals > 0 )
            return animalsOnField.first().toString();
        else if(this.grass != null)
            return grass.toString();
        return null;
    }

    public void eat(){
        if(containsGrass() && numberOfAnimals > 0){
            int maxenergy = animalsOnField.first().getEnergy();
            int foodContestingAnimals = 1;
            for(Animal animal : animalsOnField){
                if(animal == animalsOnField.first()) continue;
                else if(animal.getEnergy() == maxenergy)
                    foodContestingAnimals++;
                else
                    break;
            }
            int partOfEnergy = plantEnergy / foodContestingAnimals;
            for(Animal animal : animalsOnField){
                if(foodContestingAnimals < 1)
                    break;
                animal.eat(plantEnergy);

            }
            this.grass = null;
        }
    }

    public void moveAnimals(){
        for(Animal animal : animalsOnField){
            animal.move();
        }
    }

    public boolean containAnimals(){
        return numberOfAnimals > 0;
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal) {
        //removeAnimal(animal);
        for (IPositionChangeObserver observer : this.animals) {
            observer.positionChanged(oldPosition, newPosition, animal);
        }
    }
}
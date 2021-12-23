package agh.ics.oop;

import java.util.*;


public class MapField implements IPositionChangeObserver{
    Comparator<Animal> Comparator = new Comparator<Animal>() {
        @Override
        public int compare(Animal Sheldon, Animal Leonard) {
            if(Leonard.getEnergy() == Sheldon.getEnergy())
                if(!(Leonard == Sheldon))
                    return -1;
            return Leonard.getEnergy().compareTo(Sheldon.getEnergy());
            }
        };

    private static int plantEnergy;
    private LinkedList<Animal> animalsOnField = new LinkedList<>();
    // private SortedSet<Animal> animalsOnField = new TreeSet<Animal>(Comparator);
    private Vector2d position;
    private Grass grass;
    private int numberOfAnimals;
    private List<IPositionChangeObserver> animals;
    private List<IAnimalDeathObserver> deathObservers;

    public static void setPlantEnergy(int plantEnergy) {
        MapField.plantEnergy = plantEnergy;
    }

    public MapField(Vector2d position){
        this.deathObservers = new ArrayList<>();
        this.animals = new ArrayList<>();
        this.position = position;
        numberOfAnimals = 0;
    }

    public void addObserverPosition(IPositionChangeObserver Observer){
        animals.add(Observer);
    }

    public void removeObserverPosition(IPositionChangeObserver Observer){
        animals.remove(Observer);
    }

    public void addObserverDeath(IAnimalDeathObserver Observer){
        deathObservers.add(Observer);
    }

    public void removeObserverDeath(IAnimalDeathObserver Observer){
        deathObservers.remove(Observer);
    }

    public void breed(){

    }


    public void addAnimal(Animal animal){
        this.numberOfAnimals += 1;
        this.animalsOnField.add(animal);
        animal.addObserver(this);
    }

    public void removeAnimal(Animal animal){
        this.numberOfAnimals -= 1;
        animal.removeObserver(this);
        this.animalsOnField.remove(animal);
    }

    public void growGrass(){
        grass = new Grass();
    }

    public boolean containsGrass(){
        return grass != null;
    }

    @Override
    public String toString(){
        animalsOnField.sort(Comparator);
        if(numberOfAnimals > 0 )
            return animalsOnField.getFirst().toString();
        else if(this.grass != null)
            return grass.toString();
        return null;
    }

    public void eat(){
        animalsOnField.sort(Comparator);
        if(containsGrass() && numberOfAnimals > 0){
            int maxenergy = animalsOnField.getFirst().getEnergy();
            int foodContestingAnimals = 1;
            for(Animal animal : animalsOnField){
                if(animal == animalsOnField.getFirst())
                    continue;
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

    public String allAnimals(){
        return animalsOnField.toString();
    }

    public boolean containAnimals(){
        return numberOfAnimals > 0;
    }

    public void whoDied(){
        List<Animal> deadAnimals = new ArrayList<>();
        for(Animal animal : animalsOnField){
            if(animal.getEnergy() <= 0)
                deadAnimals.add(animal);
        }
        for(Animal animal : deadAnimals)
            removeAnimal(animal);

        for(Animal animal : deadAnimals)
            notifyAboutDeath(animal);
    }

    public void notifyAboutDeath(Animal animal){
        System.out.println("Animal at position: " + position.toString() + " died.");
        for (IAnimalDeathObserver observer : this.deathObservers) {
            observer.animalDied(animal);
        }
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal){
        removeAnimal(animal);
        for (IPositionChangeObserver observer : this.animals) {
            observer.positionChanged(oldPosition, newPosition, animal);
        }
    }
}
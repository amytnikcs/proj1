package agh.ics.oop;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import static java.lang.System.out;

public class MapField {
    Comparator<Animal> Comparator = new Comparator<Animal>() {
        @Override
        public int compare(Animal Sheldon, Animal Leonard) { //animals have names, families, houses :D
            return Leonard.getEnergy().compareTo(Sheldon.getEnergy());
            }
        };
    private SortedSet<Animal> animalsOnField = new TreeSet<Animal>(Comparator);
    private Vector2d position;
    private Grass grass;
    private int numberOfAnimals;

    public MapField(Vector2d position){
        this.position = position;
        numberOfAnimals = 0;
    }

    public void addAnimal(Animal animal){
        numberOfAnimals += 1;
        animalsOnField.add(animal);
    }

    public void growGrass(){
        grass = new Grass();
    }

    public boolean containsGrass(){
        return grass != null;
    }

    public String toString(){
        if(numberOfAnimals > 0 )
            return animalsOnField.first().toString();
        return grass.toString();
    }
}
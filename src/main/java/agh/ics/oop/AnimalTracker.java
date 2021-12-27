package agh.ics.oop;

import java.util.HashSet;
import java.util.Set;

public class AnimalTracker {
    private Animal trackedAnimal;
    private boolean dead;
    private int daysFromStart;
    private Set<Animal> descendantSet = new HashSet<Animal>();
    public AnimalTracker(){
        daysFromStart = 0;
    }


    public void addTrackedAnimal(Animal animal){
        trackedAnimal = animal;
        dead = false;
        descendantSet.clear();
        descendantSet.add(animal);
    }

    public void checkIfAnimalIsDescendent(){

    }

    public int trackedAnimalChildren(){
        return trackedAnimal.getNumberOfChildren();
    }

    public Integer getDayOfDeath(){
        if(dead)
            return daysFromStart;
        return null;
    }


}

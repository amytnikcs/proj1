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


    public void newTrackedAnimal(Animal animal){
        trackedAnimal = animal;
        dead = false;
        descendantSet.clear();
        descendantSet.add(trackedAnimal);
    }

    public void checkIfAnimalIsDescendent(Animal descendentCandidate){
        Animal parent1 = descendentCandidate.getParent1();
        Animal parent2 =  descendentCandidate.getParent2();
        if( parent1 == null || parent2 == null)
            return;
        if(descendantSet.contains(parent1) || descendantSet.contains(parent2))
            descendantSet.add(descendentCandidate);
    }

    public int trackedAnimalChildren(){
        return trackedAnimal.getNumberOfChildren();
    }

    public int trackedAnimalDescendants(){
        return descendantSet.size() - 1;
    }

    public Integer getDayOfDeath(){
        if(dead)
            return daysFromStart;
        return null;
    }

    public void newDay(){
        daysFromStart++;
    }

    public void checkifTrackedAnimalDied(Animal animal) {
        if(animal == trackedAnimal)
            dead = true;
    }
}

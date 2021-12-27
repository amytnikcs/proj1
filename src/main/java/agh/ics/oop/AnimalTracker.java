package agh.ics.oop;

import java.util.HashSet;
import java.util.Set;

public class AnimalTracker {
    private Animal trackedAnimal;
    private boolean dead;
    private int livedDays;
    private int daysFromStart;
    private int descendetNumber;
    public AnimalTracker(){
        daysFromStart = 0;
    }

    public void newTrackedAnimal(Animal animal){
        trackedAnimal = animal;
        animal.setAnimalAsOriginallyTrackedAnimal();
        descendetNumber = 0;
        dead = false;
    }

    public void checkIfAnimalIsDescendent(Animal descendentCandidate){
        if(descendentCandidate.isDescendentOfTracked())
            descendetNumber++;

    }

    public int trackedAnimalChildren()
    {
        return trackedAnimal.getNumberOfChildren();
    }

    public int trackedAnimalDescendants(){
       return descendetNumber;
    }

    public Integer getDayOfDeath(){
        if(dead)
            return livedDays;
        return null;
    }

    public void newDay(){
        daysFromStart++;
    }

    public void checkifTrackedAnimalDied(Animal animal) {
        if(animal.isOriginallyTrackedAnimal()){
            dead = true;
            livedDays = daysFromStart;
        }
    }

    public Animal getTrackedAnimal(){
        return  trackedAnimal;
    }
}

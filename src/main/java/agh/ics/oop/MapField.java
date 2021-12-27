package agh.ics.oop;

import java.util.*;

public class MapField implements IPositionChangeObserver,IMapElement{
    Comparator<Animal> Comparator = new Comparator<Animal>() {
        @Override
        public int compare(Animal Sheldon, Animal Leonard) {
            return Leonard.getEnergy().compareTo(Sheldon.getEnergy());
            }
        };

    private static int plantEnergy;
    private LinkedList<Animal> animalsOnField = new LinkedList<>();
    private Vector2d position;
    private Grass grass;
    private int numberOfAnimals;
    private List<IPositionChangeObserver> animals;
    private List<IAnimalLifeCycleObserver> lifeCycleObservers;
    private List<IGrassEatenObserver> grassEatenObservers;

    public static void setPlantEnergy(int plantEnergy) {
        MapField.plantEnergy = plantEnergy;
    }

    public MapField(Vector2d position){
        this.grassEatenObservers = new ArrayList<>();
        this.lifeCycleObservers = new ArrayList<>();
        this.animals = new ArrayList<>();
        this.position = position;
        numberOfAnimals = 0;
    }

    public void addObserverGrass(IGrassEatenObserver Observer){
        grassEatenObservers.add(Observer);
    }

    public void removeObserverGrass(IGrassEatenObserver Observer){
        grassEatenObservers.remove(Observer);
    }

    public void addObserverPosition(IPositionChangeObserver Observer){
        animals.add(Observer);
    }

    public void removeObserverPosition(IPositionChangeObserver Observer){
        animals.remove(Observer);
    }

    public void addObserverLifeCycle(IAnimalLifeCycleObserver Observer){
        lifeCycleObservers.add(Observer);
    }

    public void removeObserverLifeCycle(IAnimalLifeCycleObserver Observer){
        lifeCycleObservers.remove(Observer);
    }

    public void breed(){
        if(numberOfAnimals < 2) return;
        animalsOnField.sort(Comparator);
        int FirstMaxEnergy = animalsOnField.getFirst().getEnergy();
        int SecondMaxEnergy = animalsOnField.get(1).getEnergy();
        if(FirstMaxEnergy < animalsOnField.getFirst().getStartEnergy()*0.5) return;
        if(SecondMaxEnergy < animalsOnField.get(1).getStartEnergy()*0.5) return;
        int animalsWithFirstMaxEnergy = 0, animalsWithSecondMaxEnergy = 0;
        for(Animal animal : animalsOnField){
            if(animal.getEnergy() == FirstMaxEnergy)
                animalsWithFirstMaxEnergy++;
            else if(animal.getEnergy() == SecondMaxEnergy)
                animalsWithSecondMaxEnergy++;
        }
        Random random = new Random();
        if(animalsWithFirstMaxEnergy >= 2){
            int x = random.nextInt(animalsWithFirstMaxEnergy);
            Animal firstParent = animalsOnField.get(x);
            int y = random.nextInt(animalsWithFirstMaxEnergy);
            while(y==x)
                y = random.nextInt(animalsWithFirstMaxEnergy);
            Animal secondParent = animalsOnField.get(x);
            mating(firstParent, secondParent);
        }
        else{
            Animal firstParent = animalsOnField.getFirst();
            int x;
            if(animalsWithSecondMaxEnergy >= 2) {
                x = random.nextInt(animalsWithSecondMaxEnergy - 1) + 1; //bo nie moge wziac 0 elementu
            }
            else{
                x = 1;
            }
            Animal secondParent = animalsOnField.get(x);
            mating(firstParent, secondParent);
        }
    }

    public void mating(Animal firstParent, Animal secondParent){
        int [] childGenes = new int[32];
        Random random = new Random();
        int [] firstParentGenes = firstParent.getGenes();
        int [] secondParentGenes = secondParent.getGenes();
        boolean rightSide = random.nextInt(1) == 0;
        double sumOfEnergy = firstParent.getEnergy() + secondParent.getEnergy();
        double genesFromFirstParent = firstParent.getEnergy() / sumOfEnergy;
        double genesFromSecondParent = secondParent.getEnergy() / sumOfEnergy;
        int firstParentGenesPart =  (int) Math.ceil(genesFromFirstParent * 32);
        int secondParentGenesPart =  (int) Math.floor(genesFromSecondParent * 32);
        if(rightSide){
            for(int i = 0; i < 32; i++)
            {childGenes[i] = i<secondParentGenesPart ? secondParentGenes[i] : firstParentGenes[i];}}
        else{
            for(int i = 0; i < 32; i++)
            {childGenes[i] = i<firstParentGenesPart ? firstParentGenes[i] : secondParentGenes[i];}}
        Animal child = new Animal(firstParent.getMap(), this.position, (int) (firstParent.getEnergy()*0.25 +
                        secondParent.getEnergy()*0.25), childGenes);
        addAnimal(child);
        if(firstParent.isOriginallyTrackedAnimal() || secondParent.isOriginallyTrackedAnimal())
            child.firstLineChildOfTracked();

        else if(firstParent.isDescendentOfTracked() || secondParent.isDescendentOfTracked() ||
            firstParent.isFirstLineChild() || secondParent.isFirstLineChild())
            child.childOfTracked();

        notifyAboutBorn(child);
        firstParent.decreaseEnergy((int) (firstParent.getEnergy()*0.25));
        secondParent.decreaseEnergy((int) (secondParent.getEnergy()*0.25));
        firstParent.newChild();
        secondParent.newChild();
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

    @Override
    public String showTypeOfImage() {
        animalsOnField.sort(Comparator);
        if(numberOfAnimals > 0 )
            return animalsOnField.getFirst().showTypeOfImage();

        else if(this.grass != null)
            return grass.showTypeOfImage();

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
                animal.eat(partOfEnergy);
                foodContestingAnimals--;
            }
            notifyEatenGrass();
            this.grass = null;
        }
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
        for (IAnimalLifeCycleObserver observer : this.lifeCycleObservers) {
            observer.animalDied(animal);
        }
    }

    public void notifyAboutBorn(Animal animal){
        System.out.println("Animal born at position: " + position.toString());
        for (IAnimalLifeCycleObserver observer : this.lifeCycleObservers) {
            observer.animalBorn(animal);
        }
    }

    public void notifyEatenGrass(){
        for (IGrassEatenObserver observer : this.grassEatenObservers) {
            observer.grassEaten();
        }
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal){
        removeAnimal(animal);
        for (IPositionChangeObserver observer : this.animals) {
            observer.positionChanged(oldPosition, newPosition, animal);
        }
    }

    public boolean containsGenome(int[] genes){
        for(Animal animal : animalsOnField) {
            if (Arrays.equals(genes,animal.getGenes())){ return true; }
        }
        return false;
    }

    public Animal getFirst(){
        return animalsOnField.getFirst();
    }
}
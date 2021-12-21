package agh.ics.oop;

import java.util.Map;

public class Animal {
    private static Integer snackEnergy;
    private static Integer moveEnergy;
    private Integer energy;
    private Vector2d position;

    private int[] genes;
    private MoveDirection[] translatedGenes; //32 geny
    private MapDirection orientation;

    public Animal(Vector2d initialPosition, Integer energy, int[] genes){
        this.position = initialPosition;
        this.energy = energy;
        this.genes = genes;
        this.orientation = MapDirection.asignRandomDirection();
    }

    public String toString() {
        return switch(this.orientation) {
            case NORTH -> "N";
            case NORTH_EAST -> "NE" ;
            case EAST -> "E";
            case SOUTH_EAST -> "SE";
            case SOUTH -> "S";
            case WEST -> "W";
            case SOUTH_WEST -> "SW";
            case NORTH_WEST -> "NW";
        };
    }

    public void move(MoveDirection direction) { //czy move to tylko prosto i losowanie obrotu przed ruchem?
        decreaseEnergy(); // nie tu energia zmniejsza się co dnia

        switch (direction) {
            case RIGHT ->  this.orientation = this.orientation.next();

            case LEFT ->   this.orientation = this.orientation.previous();

            case FORWARD -> {
                updatePosition(this.position.add(this.orientation.toUnitVector()));
            }

            case BACKWARD -> {
                updatePosition(this.position.subtract(this.orientation.toUnitVector()));
            }

            case FORWARD_RIGHT -> this.orientation = this.orientation.next();

            case FORWARD_LEFT -> updatePosition(this.position.add(this.orientation.toUnitVector()));

            case BACKWARD_LEFT -> updatePosition(this.position.subtract(this.orientation.toUnitVector()));

            case BACKWARD_RIGHT -> updatePosition(this.position.subtract(this.orientation.toUnitVector()));
        }
    }

    private void decreaseEnergy(){
        energy -= moveEnergy;
        checkIfAnimalIsAlive();
    }

    private void checkIfAnimalIsAlive(){
        if(energy <= 0){
            System.out.println("Animal at position: " + position.toString() + " died.");
            //add death proces
        }
    }

    public void updatePosition(Vector2d newPosition){
        this.position = newPosition;
    }

    public void eat(){
        this.energy += snackEnergy;
    }

    //public Animal breed(Animal SecondParent){
        //return new Animal();
    //}
    public void setMoveEnergy(int moveEnergy){
        this.moveEnergy = moveEnergy;
    }
    public void setSnackEnergy(int snackEnergy){
        this.snackEnergy = snackEnergy;
    }

    public Integer getEnergy(){
        return this.energy;
    }
}

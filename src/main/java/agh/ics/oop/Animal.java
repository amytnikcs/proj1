package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animal {
    private static Integer moveEnergy;
    private final static GenesParser parser = new GenesParser();
    private Integer energy;
    private Vector2d position;
    private IWorldMap map;
    private List<IPositionChangeObserver> positions;
    private int[] genes;
    private MoveDirection[] translatedGenes; //32 geny
    private MapDirection orientation;

    public Animal(IWorldMap map, Vector2d initialPosition, Integer energy, int[] genes){
        positions = new ArrayList<>();
        this.map = map;
        this.position = initialPosition;
        this.energy = energy;
        this.genes = genes;
        this.translatedGenes = parser.parse(this.genes);
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

    public void move(){
        switch (chooseDirection()) {
            case HALF_RIGHT -> this.orientation = this.orientation.next();

            case RIGHT ->  this.orientation = this.orientation.next().next();

            case ONE_AND_HALF_RIGHT -> this.orientation = this.orientation.next().next().next();

            case HALF_LEFT -> this.orientation = this.orientation.previous();

            case LEFT ->   this.orientation = this.orientation.previous().previous();

            case ONE_AND_HALF_LEFT -> this.orientation = this.orientation.previous().previous().previous();

            case FORWARD -> {
                updatePosition(this.position.add(this.orientation.toUnitVector()));
            }

            case BACKWARD -> {
                updatePosition(this.position.subtract(this.orientation.toUnitVector()));
            }
        }
    }

    private MoveDirection chooseDirection(){
        Random random = new Random();
        return translatedGenes[random.nextInt(32)];
    }

    private void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        for(IPositionChangeObserver observer : this.positions){
            observer.positionChanged(oldPosition, newPosition, this);
        }
    }

    public void updatePosition(Vector2d newPosition){
        if(this.map.canMoveTo(newPosition)){
            Vector2d OldPosition = this.position;
            this.position = newPosition;
            positionChanged(OldPosition, newPosition);
        }
    }

    public void addObserver(IPositionChangeObserver observer){
        positions.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer){
        positions.remove(observer);
    }

    public void eat(int plantEnergy){
        this.energy += plantEnergy;
    }

    public void decreaseEnergy(){
        this.energy -= this.moveEnergy;
    }

    //public Animal breed(Animal SecondParent){
        //return new Animal();
    //}

    public void setMoveEnergy(int moveEnergy){
        this.moveEnergy = moveEnergy;
    }

    public Integer getEnergy(){
        return this.energy;
    }

    public Vector2d getPosition(){
        return this.position;
    }
}

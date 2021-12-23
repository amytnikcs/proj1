package agh.ics.oop;

import java.util.Objects;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Vector2d {
    public Integer x; //public becouse MapVisualizer
    public Integer y;
    public Vector2d(Integer x, Integer y){
        this.x = x;
        this.y = y;
    }

    public String toString(){
        return "(" + this.x + "," + this.y + ")";
    }

    public boolean precedes(Vector2d other) { return this.x <= other.x && this.y <= other.y;}
    public boolean follows(Vector2d other) { return this.x >= other.x && this.y >= other.y;}

    public boolean inside(Vector2d upperright, Vector2d lowerleft) {
        return precedes(upperright) && follows(lowerleft);
    }

    public Vector2d upperRight(Vector2d other){
        return new Vector2d(max(this.x,other.x) , max(this.y,other.y));
    }
    public Vector2d lowerLeft(Vector2d other){
        return new Vector2d(min(this.x,other.x) , min(this.y,other.y));
    }

    public Vector2d add(Vector2d other){
        return new Vector2d(this.x + other.x , this.y + other.y);
    }
    public Vector2d subtract(Vector2d other){
        return new Vector2d(this.x - other.x , this.y - other.y);
    }

    public boolean equals(Object other){
        if (this == other)
            return true;
        if (!(other instanceof Vector2d))
            return false;
        Vector2d that = (Vector2d) other;
        if(this.x == that.x && this.y == that.y)
            return true;
        return false;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY(){
        return y;
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }
}

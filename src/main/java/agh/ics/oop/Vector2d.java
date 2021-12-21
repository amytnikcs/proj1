package agh.ics.oop;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Vector2d {
    private Integer x;
    private Integer y;
    public Vector2d(Integer x, Integer y){
        this.x = x;
        this.y = y;
    }

    public String toString(){
        return "(" + this.x + "," + this.y + ")";
    }

    public boolean precedes(Vector2d other) { return this.x <= other.x && this.y <= other.y;}
    public boolean follows(Vector2d other) { return this.x >= other.x && this.y >= other.y; }
    public boolean inside(Vector2d upperright, Vector2d lowerleft) {
        if(precedes(upperright) && follows(lowerleft)) return true;
        return false;
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
}

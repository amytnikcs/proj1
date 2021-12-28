package agh.ics.oop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Vector2dTest {

    @Test
    public void toStringtest(){
        assertEquals("(1,1)", new Vector2d(1,1).toString());
        assertEquals("(-1,2)", new Vector2d(-1,2).toString());
    }
    @Test
    public void precedestest(){
        assertEquals(true, new Vector2d(1,1).precedes((new Vector2d(2,2))));
        assertEquals(false, new Vector2d(3,1).precedes((new Vector2d(2,2))));
        assertEquals(false, new Vector2d(1,1).precedes((new Vector2d(0,2))));
        assertEquals(false, new Vector2d(1,0).precedes((new Vector2d(-1,20))));
    }
    @Test
    public void followstest(){
        assertEquals(false, new Vector2d(1,1).follows((new Vector2d(2,2))));
        assertEquals(false, new Vector2d(3,1).follows((new Vector2d(2,2))));
        assertEquals(false, new Vector2d(1,1).follows((new Vector2d(0,2))));
        assertEquals(true, new Vector2d(1,0).follows((new Vector2d(-1,0))));
    }
    @Test
    public void insidetest(){
        assertEquals(true, new Vector2d(2,2).inside((new Vector2d(4,4)),(new Vector2d(0,0))));
        assertEquals(false, new Vector2d(1,2).inside(new Vector2d(2,2),new Vector2d(4,4)));
        assertEquals(false, new Vector2d(2,1).inside((new Vector2d(2,2)),(new Vector2d(4,4))));
        assertEquals(false, new Vector2d(2,5).inside((new Vector2d(0,0)),(new Vector2d(4,4))));
        assertEquals(false, new Vector2d(5,5).inside((new Vector2d(0,0)),(new Vector2d(4,4))));
        assertEquals(false, new Vector2d(5,2).inside((new Vector2d(0,0)),(new Vector2d(4,4))));
    }
    @Test
    public void upperRighttest(){
        assertEquals(new Vector2d(3,5), new Vector2d(3,1).upperRight(new Vector2d(0,5)));
        assertEquals(new Vector2d(3,3), new Vector2d(3,3).upperRight(new Vector2d(0,0)));
        assertEquals(new Vector2d(5,5), new Vector2d(1,1).upperRight(new Vector2d(5,5)));
        assertEquals(new Vector2d(2,5), new Vector2d(2,1).upperRight(new Vector2d(0,5)));
        assertEquals(new Vector2d(0,1), new Vector2d(-3,1).upperRight(new Vector2d(0,0)));
    }
    @Test
    public void lowerLefttest(){
        assertEquals(new Vector2d(0,1), new Vector2d(3,1).lowerLeft(new Vector2d(0,5)));
        assertEquals(new Vector2d(0,0), new Vector2d(3,3).lowerLeft(new Vector2d(0,0)));
        assertEquals(new Vector2d(1,1), new Vector2d(1,1).lowerLeft(new Vector2d(5,5)));
        assertEquals(new Vector2d(0,1), new Vector2d(2,1).lowerLeft(new Vector2d(0,5)));
        assertEquals(new Vector2d(-3,0), new Vector2d(-3,1).lowerLeft(new Vector2d(0,0)));
    }
    @Test
    public void addtest(){
        assertEquals(new Vector2d(3,6), new Vector2d(3,1).add(new Vector2d(0,5)));
        assertEquals(new Vector2d(3,3), new Vector2d(3,3).add(new Vector2d(0,0)));
        assertEquals(new Vector2d(6,6), new Vector2d(1,1).add(new Vector2d(5,5)));
        assertEquals(new Vector2d(2,6), new Vector2d(2,1).add(new Vector2d(0,5)));
        assertEquals(new Vector2d(-3,1), new Vector2d(-3,1).add(new Vector2d(0,0)));
    }

    @Test
    public void substracttest(){
        assertEquals(new Vector2d(3,-4), new Vector2d(3,1).subtract(new Vector2d(0,5)));
        assertEquals(new Vector2d(3,3), new Vector2d(3,3).subtract(new Vector2d(0,0)));
        assertEquals(new Vector2d(-4,-4), new Vector2d(1,1).subtract(new Vector2d(5,5)));
        assertEquals(new Vector2d(2,-4), new Vector2d(2,1).subtract(new Vector2d(0,5)));
        assertEquals(new Vector2d(-3,1), new Vector2d(-3,1).subtract(new Vector2d(0,0)));
    }

    @Test
    public void equalstest(){
        assertEquals(true, new Vector2d(2,2).equals(new Vector2d(2,2)));
        assertEquals(false, new Vector2d(3,3).equals(new Vector2d(0,0)));
        assertEquals(false, new Vector2d(1,1).equals(new Vector2d(5,5)));
        assertEquals(false, new Vector2d(2,1).equals(new Vector2d(0,5)));
    }
}

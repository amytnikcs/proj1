package agh.ics.oop;

public class Grass implements IMapElement {
    @Override
    public String toString() {
        return "*";
    }

    @Override
    public String showTypeOfImage() {
        return "src/main/resources/grass.png";
    }
}

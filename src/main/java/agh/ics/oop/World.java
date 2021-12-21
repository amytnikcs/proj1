package agh.ics.oop;
import agh.ics.oop.gui.App;
import javafx.application.Application;

import java.util.Arrays;
import static java.lang.System.out;

public class World {
    public static void main(String[] args){
        GenesSoup genesSoup = new GenesSoup();
        Vector2d position = new Vector2d(0,0);
        Animal animal = new Animal(position,100,genesSoup.getRandomGenes());
        Animal animal1 = new Animal(position,10,genesSoup.getRandomGenes());
        Animal animal2 = new Animal(position,30,genesSoup.getRandomGenes());
        MapField field = new MapField(position);
        field.addAnimal(animal);
        field.addAnimal(animal1);
        field.addAnimal(animal2);
        /*try {
           out.println("system wystartował");
            Application.launch(App.class);
            out.println("system zakończył działanie");
        }catch(IllegalArgumentException ex){
            out.println(ex);
        }*/
    }
}

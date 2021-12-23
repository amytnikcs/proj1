package agh.ics.oop;
import agh.ics.oop.gui.App;
import javafx.application.Application;

import java.util.Arrays;
import static java.lang.System.out;

public class World {
    public static void main(String[] args){
        GenesSoup genesSoup = new GenesSoup();
        Vector2d position = new Vector2d(1,1);
        SimulationEngine engine = new SimulationEngine(100,1,10,10,10,10,0.5);

        /*try {
           out.println("system wystartował");
            Application.launch(App.class);
            out.println("system zakończył działanie");
        }catch(IllegalArgumentException ex){
            out.println(ex);
        }*/
    }
}

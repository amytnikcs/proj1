package agh.ics.oop;
import agh.ics.oop.gui.App;
import javafx.application.Application;

import java.util.Arrays;
import static java.lang.System.out;

public class World {
    public static void main(String[] args){
        SimulationEngine engine = new SimulationEngine(32,3,2,30,15,10,0.5, true);
        engine.run();
        /*try {
           out.println("system wystartował");
            Application.launch(App.class);
            out.println("system zakończył działanie");
        }catch(IllegalArgumentException ex){
            out.println(ex);
        }*/
    }
}

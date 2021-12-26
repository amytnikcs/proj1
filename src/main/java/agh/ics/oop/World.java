package agh.ics.oop;
import agh.ics.oop.gui.App;
import javafx.application.Application;

import java.util.Arrays;
import static java.lang.System.out;

public class World {
    public static void main(String[] args){
        //BoundedWorldMap map = new BoundedWorldMap(15,15,0.5);
        //SimulationEngine engine = new SimulationEngine(100,10,12,30,false
        //        , map);
        //engine.run();

        try {
           out.println("system wystartował");
            Application.launch(App.class);
            out.println("system zakończył działanie");
        }catch(IllegalArgumentException ex){
            out.println(ex);
        }
    }
}

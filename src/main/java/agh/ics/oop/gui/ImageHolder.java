package agh.ics.oop.gui;

import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.System.out;

public class ImageHolder {
    private Map<String, Image> imageHolder = new ConcurrentHashMap<>();
    public ImageHolder(){
        add("src/main/resources/animal1.png");
        add("src/main/resources/animal2.png");
        add("src/main/resources/animal3.png");
        add("src/main/resources/animal4.png");
        add("src/main/resources/animal5.png");
        add("src/main/resources/animal6.png");
        add("src/main/resources/animal7.png");
        add("src/main/resources/grass.png");
        add("src/main/resources/junglegrass.png");
        add("src/main/resources/sawannagrass.png");
        add("src/main/resources/selected.png");
    }

    private void add(String fileName){
        try {
            InputStream stream;
            stream = new FileInputStream(fileName);
            Image image = new Image(stream);
            imageHolder.put(fileName,image);

        } catch (FileNotFoundException ex) {
            out.print("FileNotFound");
        }
    }

    public Image getImage(String name){
        return imageHolder.get(name);
    }
}

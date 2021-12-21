package agh.ics.oop.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import static java.lang.System.out;

public class App extends Application{
    final double MAX_FONT_SIZE = 20.0;
    private int widowWidth;
    private int windowHeight;
    private VBox vBox;
    private Label title;
    private Button startButton;
    private formTextField poleTestowe;
    private formTextField poleTestowe1;
    private formTextField poleTestowe2;
    private formTextField poleTestowe3;
    private formTextField poleTestowe4;
    private boolean startMenu;
    private Scene scene;
    @Override
    public void start(Stage primaryStage) throws Exception {
        if(startMenu)
            scene = new Scene(vBox, widowWidth, windowHeight);
        else
            scene = new Scene(new Label("GAME"), widowWidth, windowHeight);

        primaryStage.setTitle("Project 1");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void init(){
        startMenu = true;
        widowWidth = 300;
        windowHeight = 600;
        vBox = new VBox(15);
        title = new Label("set Configurations");
        title.setFont(new Font(MAX_FONT_SIZE));
        poleTestowe = new formTextField("Atrybut:","value");
        poleTestowe1 = new formTextField("Atrybut:","value");
        poleTestowe2 = new formTextField("Atrybut:","value");
        poleTestowe3 = new formTextField("Atrybut:","value");
        poleTestowe4 = new formTextField("Atrybut:","value");
        startButton = new Button("Start Simulation");
        vBox.getChildren().add(title);
        vBox.getChildren().add(poleTestowe.getHBox());
        vBox.getChildren().add(poleTestowe1.getHBox());
        vBox.getChildren().add(poleTestowe2.getHBox());
        vBox.getChildren().add(poleTestowe3.getHBox());
        vBox.getChildren().add(poleTestowe4.getHBox());
        vBox.getChildren().add(startButton);
        vBox.setAlignment(Pos.CENTER);

        try {
            startButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    startMenu = false;
                    widowWidth = 600;
                    startButton.setVisible(false);
                }
            });
        }catch(IllegalThreadStateException ex){
            out.printf("PROCES JUZ WYSTARTOWAL");
        }
    }
}

package agh.ics.oop.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class formTextField {
    private HBox hBox;
    private TextField textField;
    private Label label;
    public formTextField(String message, String basicValue){
        hBox = new HBox(35);
        textField = new TextField(basicValue);
        label = new Label(message);

        textField.setPrefWidth(50);
        textField.setMaxWidth(80);
        hBox.getChildren().add(label);
        hBox.getChildren().add(textField);
        hBox.setAlignment(Pos.CENTER);
    }

    public HBox getHBox() {
        return hBox;
    }
}

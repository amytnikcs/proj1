package agh.ics.oop.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import static java.lang.System.out;

public class App extends Application{
    final double MAX_FONT_SIZE = 20.0;
    private int widowWidth;
    private int windowHeight;
    private VBox inputMenuVBox;
    private HBox windowSimulation;

    private Label inputMenuName;
    private Label inputSetMapValues;
    private Label inputSetSimulationValues;
    private Label inputOtherValues;

    private Button startSimulationButton;

    private HBox inputLeftCheckBox;
    private HBox inputRightCheckBox;
    private CheckBox inputLeftMapIsMagic;
    private CheckBox inputRightMapIsMagic;
    private Label inputLeftMapIsMagicLabel;
    private Label inputRightMapIsMagicLabel;

    private Thread engineThreadForLeftMap;
    private Thread engineThreadForRightMap;

    private formTextField widthTextField;
    private formTextField heightTextField;
    private formTextField jungleRatioTextField;
    private formTextField startEnergyTextField;
    private formTextField moveEnergyTextField;
    private formTextField plantEnergyTextField;
    private formTextField initialNumberOfAnimalsTextField;
    private formTextField initialRefreshTimeTextField;

    private Stage primaryStage;


    private boolean startMenu;
    private Scene scene;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        scene = new Scene(inputMenuVBox, widowWidth, windowHeight);
        this.primaryStage.setTitle("Set preferences");
        this.primaryStage.setScene(scene);
        this.primaryStage.show();
    }

    public void init(){
        widowWidth = 300;
        windowHeight = 600;
        inputMenuVBox = new VBox(15);
        inputLeftCheckBox = new HBox(35);
        inputRightCheckBox = new HBox(35);
        inputLeftMapIsMagic = new CheckBox();
        inputRightMapIsMagic = new CheckBox();


        inputMenuName = new Label("SET START VALUES");
        inputSetMapValues = new Label("map options:");
        inputSetSimulationValues = new Label("simulation options:");
        inputOtherValues = new Label("other options:");


        inputLeftMapIsMagicLabel = new Label("is left map magic?");
        inputRightMapIsMagicLabel = new Label( "is right map magic?");

        inputMenuName.setFont(new Font(MAX_FONT_SIZE));

        widthTextField = new formTextField("width:","15");
        heightTextField = new formTextField("height:","15");
        jungleRatioTextField = new formTextField("jungle ratio:","0.5");

        startEnergyTextField = new formTextField("start energy:","100");
        moveEnergyTextField = new formTextField("move energy:","10");
        plantEnergyTextField = new formTextField("plant energy:","100");
        initialNumberOfAnimalsTextField = new formTextField("initial number of animals:","30");
        initialRefreshTimeTextField = new formTextField("refresh time(ms):","30");

        inputLeftCheckBox.getChildren().add(inputLeftMapIsMagicLabel);
        inputLeftCheckBox.getChildren().add(inputLeftMapIsMagic);
        inputLeftCheckBox.setAlignment(Pos.CENTER);
        inputRightCheckBox.getChildren().add(inputRightMapIsMagicLabel);
        inputRightCheckBox.getChildren().add(inputRightMapIsMagic);
        inputRightCheckBox.setAlignment(Pos.CENTER);

        startSimulationButton = new Button("Start Simulation");

        inputMenuVBox.getChildren().add(inputMenuName);
        inputMenuVBox.getChildren().add(inputSetMapValues);
        inputMenuVBox.getChildren().add(widthTextField.getHBox());
        inputMenuVBox.getChildren().add(heightTextField.getHBox());
        inputMenuVBox.getChildren().add(jungleRatioTextField.getHBox());
        inputMenuVBox.getChildren().add(inputLeftCheckBox);
        inputMenuVBox.getChildren().add(inputRightCheckBox);
        inputMenuVBox.getChildren().add(inputSetSimulationValues);
        inputMenuVBox.getChildren().add(startEnergyTextField.getHBox());
        inputMenuVBox.getChildren().add(moveEnergyTextField.getHBox());
        inputMenuVBox.getChildren().add(plantEnergyTextField.getHBox());
        inputMenuVBox.getChildren().add(initialNumberOfAnimalsTextField.getHBox());
        inputMenuVBox.getChildren().add(inputOtherValues);
        inputMenuVBox.getChildren().add(initialRefreshTimeTextField.getHBox());
        inputMenuVBox.getChildren().add(startSimulationButton);
        inputMenuVBox.setAlignment(Pos.CENTER);


        String[] text = {new String()};
        int numberoutput = 10;
        try {
            startSimulationButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    text[0] = widthTextField.getTextField().getText();
                    try {
                        int number = Integer.parseInt(widthTextField.getTextField().getText());
                           //numberoutput = number;
                    }catch(NumberFormatException numberFormatException){
                        System.out.println("NOT A NUMBER");
                    }
                    System.out.println(numberoutput);

                    widowWidth = 600;
                    disableOptionsEdition();
                    showApplicatoinScreen();
                }
            });
        }catch(IllegalThreadStateException ex){
            out.printf("PROCES JUZ WYSTARTOWAL");
        }
    }
    /*public void disableOptionsEdition(){
        widthTextField.getHBox().setDisable(true);
        heightTextField.getHBox().setDisable(true);
        jungleRatioTextField.getHBox().setDisable(true);
        startEnergyTextField.getHBox().setDisable(true);
        moveEnergyTextField.getHBox().setDisable(true);
        plantEnergyTextField.getHBox().setDisable(true);
        initialNumberOfAnimalsTextField.getHBox().setDisable(true);
        initialRefreshTimeTextField.getHBox().setDisable(true);

        inputLeftMapIsMagic.setDisable(true);
        inputRightMapIsMagic.setDisable(true);
        startSimulationButton.setVisible(false);
    }*/

    public void showApplicatoinScreen(){
        Stage simulationStage = new Stage();
        simulationStage.setTitle("Simulation window");
        windowSimulation = new HBox();
        Label label = new Label("Game is HERE");
        windowSimulation.getChildren().add(label);
        Scene scene = new Scene(windowSimulation, 1000, 650);
        simulationStage.setScene(scene);
        simulationStage.show();
    }
}

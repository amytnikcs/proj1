package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Map;

import static java.lang.System.out;

public class App extends Application implements  IUpdateAnimalsSimulation{

    private IUpdateAnimalsSimulation app = this;
    private SimulationEngine leftMapEngine;
    private SimulationEngine rightMapEngine;
    private UnBoundedWorldMap leftMap;
    private BoundedWorldMap rightMap;

    ///////////////////////////////////////////////////////////
    final double MAX_FONT_SIZE = 20.0;
    private int widowWidth;
    private int windowHeight;
    private VBox inputMenuVBox;

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

    private formTextField widthTextField;
    private formTextField heightTextField;
    private formTextField jungleRatioTextField;
    private formTextField startEnergyTextField;
    private formTextField moveEnergyTextField;
    private formTextField plantEnergyTextField;
    private formTextField numberOfAnimalsTextField;
    private formTextField refreshTimeTextField;
    private Stage primaryStage;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private Thread engineThreadForLeftMap;
    private Thread engineThreadForRightMap;

    private GridPane gridPaneLeft;
    private GridPane gridPaneRight;

    private int simulationWidth = 1200;
    private int simulationHeight = 750;

    private HBox windowSimulation;
    ////////////////////////////////////////////////////////////////////////////
    int width = 15;
    int height = 15;
    double jungleRatio = 0.5;
    boolean isMagicLeft = false;
    boolean isMagicRight = false;
    int startingEnergy = 100;
    int moveEnergy = 10;
    int plantEnergy = 100;
    int numberOfAnimals = 30;
    int refreshTime = 30;

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
        createOptionsMenu();
        app = this;
        try {
            startSimulationButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        width = Integer.parseInt(widthTextField.getTextField().getText());
                        height = Integer.parseInt(heightTextField.getTextField().getText());
                        jungleRatio = Double.parseDouble(widthTextField.getTextField().getText());
                        isMagicLeft = inputLeftMapIsMagic.isSelected();
                        isMagicRight = inputRightMapIsMagic.isSelected();
                        startingEnergy = Integer.parseInt(startEnergyTextField.getTextField().getText());
                        moveEnergy = Integer.parseInt(moveEnergyTextField.getTextField().getText());
                        plantEnergy = Integer.parseInt(plantEnergyTextField.getTextField().getText());
                        numberOfAnimals = Integer.parseInt(numberOfAnimalsTextField.getTextField().getText());
                        refreshTime = Integer.parseInt(refreshTimeTextField.getTextField().getText());
                    }catch(NumberFormatException numberFormatException){
                        System.out.println("ERROR 42: GIVEN VALUE IS NOT A NUMBER");
                    }
                    gridPaneLeft = new GridPane();
                    leftMap = new UnBoundedWorldMap(width,height,jungleRatio);
                    leftMapEngine = new SimulationEngine(startingEnergy, moveEnergy, plantEnergy, numberOfAnimals,
                            isMagicLeft , leftMap);
                    leftMapEngine.setMoveDelay(refreshTime);
                    leftMapEngine.addSimulationObserver(app);
                    engineThreadForLeftMap = new Thread(leftMapEngine);

                    gridPaneRight = new GridPane();
                    rightMap = new BoundedWorldMap(width,height,jungleRatio);
                    rightMapEngine = new SimulationEngine(startingEnergy, moveEnergy, plantEnergy, numberOfAnimals,
                            isMagicRight , rightMap);
                    rightMapEngine.setMoveDelay(refreshTime);
                    rightMapEngine.addSimulationObserver(app);
                    engineThreadForRightMap = new Thread(rightMapEngine);

                    disablePrimaryStage();
                    showApplicationScreen();
                    engineThreadForRightMap.start();
                    engineThreadForLeftMap.start();
                }
            });
        }catch(IllegalThreadStateException ex){
            out.printf("PROCES JUZ WYSTARTOWAL");
        }
    }


    public void createSimulationGUI(){
        windowSimulation = new HBox(10);
        windowSimulation.setMaxWidth(simulationWidth + 30);
        windowSimulation.setMaxHeight(simulationHeight + 20);
        windowSimulation.setPadding(new Insets(10,10,10,10));
        windowSimulation.getChildren().add(createLeftSide());
        windowSimulation.getChildren().add(createRightSide());
    }

    public HBox createLeftSide(){
        HBox verticalContainer = new HBox();
        verticalContainer.setMaxHeight(simulationHeight);
        GridPane leftGridPane = createGridPane(this.leftMap, gridPaneLeft);
        verticalContainer.getChildren().add(leftGridPane);
        return verticalContainer;
    }

    public HBox createRightSide(){
        HBox verticalContainer = new HBox();
        verticalContainer.setMaxHeight(simulationHeight);
        GridPane rightGridPane = createGridPane(this.rightMap, gridPaneRight);
        verticalContainer.getChildren().add(rightGridPane);
        return verticalContainer;
    }

    public GridPane createGridPane(BoundedWorldMap map, GridPane gridPane){
        int widthForGridpane = simulationWidth / 2;
        int heightForGridpane = (simulationHeight * 3) / 5;
        int colWidth = (widthForGridpane) / map.getWidth();
        int rowHeight = (heightForGridpane) / map.getHeight();
        gridPane.setMaxWidth (widthForGridpane);
        gridPane.setPrefWidth(widthForGridpane);
        //gridPane.setMinWidth (widthForGridpane);

        gridPane.setMaxHeight(heightForGridpane);
        gridPane.setPrefWidth(heightForGridpane);
        //gridPane.setMinHeight(heightForGridpane);
        ColumnConstraints colConstraint= new ColumnConstraints(colWidth);
        RowConstraints rowConstraint = new RowConstraints(rowHeight);

        gridPane.setGridLinesVisible(true);

        for(int i = 0; i < map.getWidth(); i++){
            gridPane.getColumnConstraints().add(colConstraint);
        }

        for(int i = 0; i < map.getHeight(); i++){
            gridPane.getRowConstraints().add(rowConstraint);
        }
        Map<Vector2d, MapField> Hashmap = map.getActiveMapFields();
        for(Vector2d position : Hashmap.keySet()){
            MapField field = Hashmap.get(position);
            Label label = new Label(field.toString());
            gridPane.add(label,position.getX(),position.getY());
            gridPane.setHalignment(label, HPos.CENTER);
        }
        gridPane.setAlignment(Pos.CENTER);

        return gridPane;
    }

    public void disablePrimaryStage(){
        this.primaryStage.hide();
    }

    public void showApplicationScreen(){
        createSimulationGUI();
        Stage simulationStage = new Stage();
        simulationStage.setTitle("Simulation window");
        Scene scene = new Scene(windowSimulation, simulationWidth + 30, simulationHeight + 20);
        simulationStage.setScene(scene);
        simulationStage.show();
    }

    public void createOptionsMenu(){
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
        numberOfAnimalsTextField = new formTextField("initial number of animals:","30");
        refreshTimeTextField = new formTextField("refresh time(ms):","30");

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
        inputMenuVBox.getChildren().add(numberOfAnimalsTextField.getHBox());
        inputMenuVBox.getChildren().add(inputOtherValues);
        inputMenuVBox.getChildren().add(refreshTimeTextField.getHBox());
        inputMenuVBox.getChildren().add(startSimulationButton);
        inputMenuVBox.setAlignment(Pos.CENTER);
    }

    @Override
    public void animalsUpdate() {
        Platform.runLater(() -> {
            gridPaneLeft.getChildren().clear();
            gridPaneRight.getChildren().clear();
            createSimulationGUI();
        });
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

}

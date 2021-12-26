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
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Map;
import java.util.concurrent.ExecutorService;

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
    private ExecutorService executor;
    private Thread engineThreadForLeftMap;
    private Thread engineThreadForRightMap;

    private GridPane gridPaneLeft;
    private GridPane gridPaneRight;

    private int simulationWidth = 1200;
    private int simulationHeight = 750;


    private LineChart leftLineChartAnimalsGrass;
    private XYChart.Series leftSeriesAnimals = new XYChart.Series();
    private XYChart.Series leftSeriesGrass = new XYChart.Series();

    private LineChart rightLineChartAnimalsGrass;
    private XYChart.Series rightSeriesAnimals = new XYChart.Series();
    private XYChart.Series rightSeriesGrass = new XYChart.Series();

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
        leftLineChartAnimalsGrass = createAnimalGrassLineChart(leftLineChartAnimalsGrass, leftSeriesAnimals,
                leftSeriesGrass);
        rightLineChartAnimalsGrass = createAnimalGrassLineChart(rightLineChartAnimalsGrass, rightSeriesAnimals,
                rightSeriesGrass);
        app = this;
        try {
            startSimulationButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        width = Integer.parseInt(widthTextField.getTextField().getText());
                        height = Integer.parseInt(heightTextField.getTextField().getText());
                        jungleRatio = Double.parseDouble(jungleRatioTextField.getTextField().getText());
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
                    leftMap = new UnBoundedWorldMap(width, height, jungleRatio);
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

                    showApplicationScreen();
                        //engineThreadForRightMap.start();
                        engineThreadForLeftMap.start();

                }
            });
        }catch(IllegalThreadStateException ex){
            out.printf("PROCES JUZ WYSTARTOWAL");
        }
    }


    public void createSimulationGUI(){
        windowSimulation = new HBox(10);
        windowSimulation.setMaxWidth(simulationWidth + 200);
        windowSimulation.setMaxHeight(simulationHeight + 20);
        windowSimulation.setPadding(new Insets(10,10,10,10));
        windowSimulation.getChildren().add(createLeftSide());
        windowSimulation.getChildren().add(createRightSide());
    }

    public VBox createLeftSide(){
        VBox verticalContainer = new VBox();
        HBox horizontalContainer = new HBox(5);
        verticalContainer.setMaxHeight(simulationHeight);
        GridPane leftGridPane = createGridPane(this.leftMap, gridPaneLeft);
        verticalContainer.getChildren().add(leftGridPane);
        verticalContainer.getChildren().add(horizontalContainer);
        horizontalContainer.getChildren().add(leftLineChartAnimalsGrass);
        //updateAnimalGrassChart(leftSeriesAnimals, leftSeriesGrass , leftMapEngine.getTracker());
        return verticalContainer;
    }

    public VBox createRightSide(){
        VBox verticalContainer = new VBox();
        HBox horizontalContainer = new HBox(5);
        verticalContainer.setMaxHeight(simulationHeight);
        GridPane rightGridPane = createGridPane(this.rightMap, gridPaneRight);
        verticalContainer.getChildren().add(rightGridPane);
        verticalContainer.getChildren().add(horizontalContainer);
        horizontalContainer.getChildren().add(rightLineChartAnimalsGrass);
       //updateAnimalGrassChart(rightSeriesAnimals,rightSeriesGrass, rightMapEngine.getTracker());
        return verticalContainer;
    }

    /*public void updateAnimalGrassChart(XYChart.Series animals,XYChart.Series grass, DataTracker tracker){
        animals.getData().add(tracker.daysPassed(), tracker.numberOfAnimals());
        grass.getData().add(tracker.daysPassed(), tracker.getHowMuchGrassOnMap());
    }*/

    public LineChart createAnimalGrassLineChart(LineChart lineChart , XYChart.Series animals, XYChart.Series grass){
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of days");
        yAxis.setLabel("Number of animals/grass");
        animals.setName("animals");
        grass.setName("grass");
        lineChart = new LineChart<Number,Number>(xAxis,yAxis);
        lineChart.setTitle("animals/grass number");
        lineChart.setPrefWidth(simulationWidth / 6);
        lineChart.setMaxWidth(simulationWidth / 6);
        lineChart.getData().addAll(animals, grass);
        return lineChart;
    }


    public GridPane createGridPane(BoundedWorldMap map, GridPane gridPane){
        gridPane.setGridLinesVisible(false); //https://stackoverflow.com/questions/11147788/
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();
        gridPane.getChildren().clear();
        gridPane.setGridLinesVisible(true);
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
        Map<Vector2d, MapField> hashMap = map.getActiveMapFields();

        for(Vector2d position : hashMap.keySet()){
            MapField field = hashMap.get(position);
            Label label = new Label(field.toString());
            gridPane.add(label, position.getX(), position.getY());
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
        primaryStage.setTitle("Simulation window");
        Scene scene = new Scene(windowSimulation, simulationWidth + 30, simulationHeight + 20);
        primaryStage.setScene(scene);
        primaryStage.show();
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
        refreshTimeTextField = new formTextField("refresh time(ms):","300");

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
            showApplicationScreen();
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

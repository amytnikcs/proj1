package agh.ics.oop.gui;

import agh.ics.oop.*;
import com.sun.scenario.animation.AnimationPulseMBean;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Arrays;
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

    private Button leftThreadStopStart = new Button("STOP");
    private boolean leftThreadRunning = true;

    private Button rightThreadStopStart = new Button("STOP");
    private boolean rightThreadRunning = true;

    private GridPane gridPaneLeft;
    private GridPane gridPaneRight;

    private int simulationWidth = 1200;
    private int simulationHeight = 750;

    private Label leftMapTitle = new Label("Wrapped Map");
    private Text leftDominantGenes = new Text();
    private LineChart leftLineChartAnimalsGrass;
    private XYChart.Series leftSeriesAnimals = new XYChart.Series();
    private XYChart.Series leftSeriesGrass = new XYChart.Series();

    private Label rightMapTitle = new Label("Map with wall");
    private Text rightDominantGenes = new Text();
    private LineChart rightLineChartAnimalsGrass;
    private XYChart.Series rightSeriesAnimals = new XYChart.Series();
    private XYChart.Series rightSeriesGrass = new XYChart.Series();

    private LineChart leftLineChildrenChart;
    private LineChart leftLineLifeSpanChart;
    private LineChart leftLineEnergyChart;
    private XYChart.Series leftSeriesEnergy = new XYChart.Series();
    private XYChart.Series leftSeriesLiveSpan = new XYChart.Series();
    private XYChart.Series leftSeriesChildren = new XYChart.Series();

    private LineChart rightLineChildrenChart;
    private LineChart rightLineLifeSpanChart;
    private LineChart rightLineEnergyChart;
    private XYChart.Series rightSeriesEnergy = new XYChart.Series();
    private XYChart.Series rightSeriesLiveSpan = new XYChart.Series();
    private XYChart.Series rightSeriesChildren = new XYChart.Series();

    private HBox windowSimulation;
    private Scene simulationScene;

    private AnimalTracker leftAnimalTracker;
    private Text leftTrackerDayOfDeath = new Text();
    private Text leftTrackerNumberOfAnimalsDescendans = new Text();
    private Text leftTrackerNumberOfChildren = new Text();


    private AnimalTracker rightAnimalTracker;
    private Text rightTrackerDayOfDeath = new Text();
    private Text rightTrackerNumberOfAnimalsDescendans = new Text();
    private Text rightTrackerNumberOfChildren = new Text();

    private Button leftGenomeDisplayButton = new Button("DISPLAY DOMINANT GENOME");
    private Button rightGenomeDisplayButton = new Button("DISPLAY DOMINANT GENOME");

    private Text leftRightClickGenome = new Text();
    private Text rightRightClickGenome = new Text();

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
    private ImageHolder imageHolder;

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
        chartsInit();
        try {
            startSimulationButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        readData();
                    }catch(NumberFormatException numberFormatException){
                        System.out.println("ERROR 42: GIVEN VALUE IS NOT A NUMBER");
                    }
                    simulationInit();
                }
            });
        }catch(IllegalThreadStateException ex){
            out.printf("PROCES JUZ WYSTARTOWAL");
        }
    }
    public App app(){
        return this;
    }

    private void simulationInit(){
        gridPaneLeft = new GridPane();
        leftMap = new UnBoundedWorldMap(width, height, jungleRatio);
        leftMapEngine = new SimulationEngine(startingEnergy, moveEnergy, plantEnergy, numberOfAnimals,
                isMagicLeft , leftMap);
        gridPaneLeft = createGridPane(leftMap,gridPaneLeft,false, null);
        leftMapEngine.setMoveDelay(refreshTime);
        leftMapEngine.addSimulationObserver(app());
        leftAnimalTracker = leftMapEngine.getAnimalTracker();
        engineThreadForLeftMap = new Thread(leftMapEngine);

        gridPaneRight = new GridPane();
        rightMap = new BoundedWorldMap(width,height,jungleRatio);
        rightMapEngine = new SimulationEngine(startingEnergy, moveEnergy, plantEnergy, numberOfAnimals,
                isMagicRight , rightMap);
        gridPaneRight = createGridPane(rightMap,gridPaneRight,false, null);
        rightMapEngine.setMoveDelay(refreshTime);
        rightMapEngine.addSimulationObserver(app());
        rightAnimalTracker = rightMapEngine.getAnimalTracker();
        engineThreadForRightMap = new Thread(rightMapEngine);

        showApplicationScreen();
        engineThreadForRightMap.start();
        engineThreadForLeftMap.start();
    }

    private void readData() {
        imageHolder = new ImageHolder();
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
    }

    private void updateTrackedData(){
        if(leftAnimalTracker.getTrackedAnimal() != null) {
            if (leftAnimalTracker.getDayOfDeath() != null)
                leftTrackerDayOfDeath.setText(Integer.toString(leftAnimalTracker.getDayOfDeath()));
            leftTrackerNumberOfChildren.setText(Integer.toString(leftAnimalTracker.trackedAnimalChildren()));
            leftTrackerNumberOfAnimalsDescendans.setText(Integer.toString(leftAnimalTracker.trackedAnimalDescendants()));
        }
        if(rightAnimalTracker.getTrackedAnimal() != null){
            if(rightAnimalTracker.getDayOfDeath() != null)
                rightTrackerDayOfDeath.setText(Integer.toString(rightAnimalTracker.getDayOfDeath()));
            rightTrackerNumberOfChildren.setText(Integer.toString(rightAnimalTracker.trackedAnimalChildren()));
            rightTrackerNumberOfAnimalsDescendans.setText(Integer.toString(rightAnimalTracker.trackedAnimalDescendants()));
        }
    }


    public void showApplicationScreen(){
        createSimulationGUI();
        primaryStage.setTitle("Simulation window");
        simulationScene = new Scene(windowSimulation, simulationWidth + 40, simulationHeight + 25);
        primaryStage.setScene(simulationScene);
        primaryStage.show();

    }

    public void createSimulationGUI(){
        leftButtonHandle();
        rightButtonHandle();
        windowSimulation = new HBox(10);
        windowSimulation.setMaxWidth(simulationWidth + 200);
        windowSimulation.setMaxHeight(simulationHeight + 20);
        windowSimulation.setPadding(new Insets(10,10,10,10));
        windowSimulation.getChildren().add(createLeftSide());
        windowSimulation.getChildren().add(createRightSide());
    }

    public VBox createLeftSide(){
        VBox verticalContainer = new VBox();
        HBox boxForStartStopTitle = new HBox();
        HBox horizontalContainer = new HBox(5);
        VBox childrenLifeSpanContainer = new VBox();
        VBox animalGrassEnergyContainer = new VBox();
        VBox statsAndTrackContainer = new VBox();
        verticalContainer.getChildren().add(boxForStartStopTitle);
        verticalContainer.setMaxHeight(simulationHeight);
        verticalContainer.getChildren().add(gridPaneLeft);
        verticalContainer.getChildren().add(horizontalContainer);
        horizontalContainer.getChildren().add(statsAndTrackContainer);
        horizontalContainer.getChildren().add(childrenLifeSpanContainer);
        horizontalContainer.getChildren().add(animalGrassEnergyContainer);
        boxForStartStopTitle.getChildren().add(leftMapTitle);
        boxForStartStopTitle.getChildren().add(leftThreadStopStart);
        statsAndTrackContainer.getChildren().add(leftDominantGenes);
        statsAndTrackContainer.getChildren().add(leftTrackerDayOfDeath);
        statsAndTrackContainer.getChildren().add(leftTrackerNumberOfChildren);
        statsAndTrackContainer.getChildren().add(leftTrackerNumberOfAnimalsDescendans);
        statsAndTrackContainer.getChildren().add(leftGenomeDisplayButton);
        statsAndTrackContainer.getChildren().add(leftRightClickGenome);
        animalGrassEnergyContainer.getChildren().add(leftLineEnergyChart);
        animalGrassEnergyContainer.getChildren().add(leftLineChartAnimalsGrass);
        childrenLifeSpanContainer.getChildren().add(leftLineChildrenChart);
        childrenLifeSpanContainer.getChildren().add(leftLineLifeSpanChart);
        return verticalContainer;
    }

    public VBox createRightSide(){
        VBox verticalContainer = new VBox();
        HBox boxForStartStopTitle = new HBox();
        HBox horizontalContainer = new HBox(5);
        VBox childrenLifeSpanContainer = new VBox();
        VBox animalGrassEnergyContainer = new VBox();
        VBox statsAndTrackContainer = new VBox();
        verticalContainer.getChildren().add(boxForStartStopTitle);
        verticalContainer.setMaxHeight(simulationHeight);
        verticalContainer.getChildren().add(gridPaneRight);
        verticalContainer.getChildren().add(horizontalContainer);
        horizontalContainer.getChildren().add(animalGrassEnergyContainer);
        horizontalContainer.getChildren().add(childrenLifeSpanContainer);
        horizontalContainer.getChildren().add(statsAndTrackContainer);
        boxForStartStopTitle.getChildren().add(rightMapTitle);
        boxForStartStopTitle.getChildren().add(rightThreadStopStart);
        statsAndTrackContainer.getChildren().add(rightDominantGenes);
        statsAndTrackContainer.getChildren().add(rightTrackerDayOfDeath);
        statsAndTrackContainer.getChildren().add(rightTrackerNumberOfChildren);
        statsAndTrackContainer.getChildren().add(rightTrackerNumberOfAnimalsDescendans);
        statsAndTrackContainer.getChildren().add(rightGenomeDisplayButton);
        statsAndTrackContainer.getChildren().add(rightRightClickGenome);
        animalGrassEnergyContainer.getChildren().add(rightLineEnergyChart);
        animalGrassEnergyContainer.getChildren().add(rightLineChartAnimalsGrass);
        childrenLifeSpanContainer.getChildren().add(rightLineChildrenChart);
        childrenLifeSpanContainer.getChildren().add(rightLineLifeSpanChart);
        return verticalContainer;
    }


    public GridPane createGridPane(BoundedWorldMap map, GridPane gridPane, boolean isDisplayDominantGenomeMode,
                                   int[] dominantGenes){
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();
        gridPane.getChildren().clear();


        int widthForGridpane = simulationWidth / 2;
        int heightForGridpane = (simulationHeight * 3) / 5;
        int colWidth = (widthForGridpane) / map.getWidth();
        int rowHeight = (heightForGridpane) / map.getHeight();
        int imageWidth = ((widthForGridpane)/(map.getWidth()));
        int imageHeight = (heightForGridpane)/(map.getHeight());

        gridPane.setMaxWidth (widthForGridpane);
        gridPane.setPrefWidth(widthForGridpane);
        gridPane.setMaxHeight(heightForGridpane);
        gridPane.setPrefWidth(heightForGridpane);

        ColumnConstraints colConstraint= new ColumnConstraints(colWidth);
        RowConstraints rowConstraint = new RowConstraints(rowHeight);


        for(int i = 0; i < map.getWidth(); i++){
            gridPane.getColumnConstraints().add(colConstraint);
        }

        for(int i = 0; i < map.getHeight(); i++){
            gridPane.getRowConstraints().add(rowConstraint);
        }

        Map<Vector2d, MapField> hashMap = map.getActiveMapFields();

        for(int i = 0; i < map.getWidth(); i++)
            for(int j = 0; j < map.getHeight(); j++) {
                Vector2d position = new Vector2d(i, j);

                if (map.inJungle(position)) {
                    Image image = imageHolder.getImage("src/main/resources/junglegrass.png");
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(imageWidth);
                    imageView.setFitHeight(imageHeight);
                    gridPane.add(imageView, position.getX(), position.getY());
                }

                else {
                    Image image = imageHolder.getImage("src/main/resources/sawannagrass.png");
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(imageWidth);
                    imageView.setFitHeight(imageHeight);
                    gridPane.add(imageView, position.getX(), position.getY());
                }

                if(isDisplayDominantGenomeMode && hashMap.get(position) != null)
                    if (hashMap.get(position).containsGenome(dominantGenes)){
                    Image image = imageHolder.getImage("src/main/resources/selected.png");
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(imageWidth);
                    imageView.setFitHeight(imageHeight);
                    gridPane.add(imageView, position.getX(), position.getY());
                }

                if(hashMap.get(position) != null) {
                    Image image = imageHolder.getImage(hashMap.get(position).showTypeOfImage());
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(imageWidth);
                    imageView.setFitHeight(imageHeight);
                    gridPane.add(imageView, position.getX(), position.getY());
                    addPane(gridPane, position.getX(), position.getY(), map);
                }

            }

        return gridPane;
    }

    private void addPane(GridPane gridPane, int col, int row, BoundedWorldMap map) {
        Pane pane = new Pane();
        pane.setOnMouseClicked(e -> {
            try {
                Vector2d position = new Vector2d(col, row);
                if(e.getButton() == MouseButton.PRIMARY) {
                    if (map.equals(rightMap)) {
                        if (map.getActiveMapFields().get(position).containAnimals()) {
                            rightAnimalTracker.newTrackedAnimal(map.getActiveMapFields().get(position).getFirst());
                            System.out.println("tracking animal at:" + position);
                        }
                    } else {
                        if (map.getActiveMapFields().get(position).containAnimals()) {
                            leftAnimalTracker.newTrackedAnimal(map.getActiveMapFields().get(position).getFirst());
                            System.out.println("tracking animal at:" + position);
                        }
                    }
                }

                if(e.getButton() == MouseButton.SECONDARY){
                    if (map.equals(rightMap)) {
                        if (map.getActiveMapFields().get(position).containAnimals()) {
                            rightRightClickGenome.setText(Arrays.toString(map.getActiveMapFields().get(position)
                                    .getFirst().getGenes()));
                            rightRightClickGenome.setWrappingWidth(simulationWidth/6);
                        }
                    } else {
                        if (map.getActiveMapFields().get(position).containAnimals()) {
                            leftRightClickGenome.setText(Arrays.toString(map.getActiveMapFields().get(position)
                                    .getFirst().getGenes()));
                            leftRightClickGenome.setWrappingWidth(simulationWidth/6);
                        }
                    }
                }
            }catch(Exception ex){
                System.out.println("tracking problem");
            }
        });

        gridPane.add(pane, col, row);
    }

    @Override
    public void animalsUpdate() {
        Platform.runLater(() -> {
            leftDominantGenes.setText(Arrays.toString(leftMapEngine.getTracker().findDominantGenes()));
            leftDominantGenes.setWrappingWidth(simulationWidth/6);
            gridPaneLeft = createGridPane(this.leftMap, gridPaneLeft, false,
                    null);
            updateChartsValue(leftSeriesEnergy, leftSeriesAnimals, leftSeriesGrass ,
                    leftSeriesLiveSpan, leftSeriesChildren, leftMapEngine.getTracker());

            rightDominantGenes.setText(Arrays.toString(rightMapEngine.getTracker().findDominantGenes()));
            rightDominantGenes.setWrappingWidth(simulationWidth/6);
            gridPaneRight = createGridPane(this.rightMap, gridPaneRight ,false,
                    null);
            updateChartsValue(rightSeriesEnergy, rightSeriesAnimals, rightSeriesGrass, rightSeriesLiveSpan,
                    rightSeriesChildren, rightMapEngine.getTracker());
            updateTrackedData();
        });
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

        widthTextField = new formTextField("width:","25");
        heightTextField = new formTextField("height:","25");
        jungleRatioTextField = new formTextField("jungle ratio:","0.5");

        startEnergyTextField = new formTextField("start energy:","100");
        moveEnergyTextField = new formTextField("move energy:","5");
        plantEnergyTextField = new formTextField("plant energy:","100");
        numberOfAnimalsTextField = new formTextField("initial number of animals:","40");
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

    private void chartsInit() {
        leftLineChartAnimalsGrass = createAnimalGrassLineChart(leftSeriesAnimals, leftSeriesGrass);

        leftLineChildrenChart = createSingleLineChart(leftSeriesChildren, "Number of days",
                "Avg animal children number","Animal avg children","avg children");

        leftLineLifeSpanChart = createSingleLineChart(leftSeriesLiveSpan,"Number of days",
                "Avg animal life span","Animal avg life span", "avg lifeSpan");

        leftLineEnergyChart = createSingleLineChart(leftSeriesEnergy, "Number of days",
                "Avg energy", "Animal average energy", "average energy");

        rightLineChartAnimalsGrass = createAnimalGrassLineChart(rightSeriesAnimals, rightSeriesGrass);

        rightLineChildrenChart =createSingleLineChart(rightSeriesChildren, "Number of days",
                "Avg animal children number","Animal avg children","avg children");

        rightLineLifeSpanChart = createSingleLineChart(rightSeriesLiveSpan,"Number of days",
                "Avg animal life span","Animal avg life span", "avg lifeSpan");

        rightLineEnergyChart = createSingleLineChart(rightSeriesEnergy, "Number of days",
                "Avg energy", "Animal average energy", "average energy");
    }

    public LineChart createAnimalGrassLineChart(XYChart.Series animals, XYChart.Series grass){
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of days");
        yAxis.setLabel("Number of animals/grass");
        animals.setName("animals");
        grass.setName("grass");
        LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);
        lineChart.setTitle("animals/grass");
        lineChart.setPrefWidth(simulationWidth / 6);
        lineChart.setMaxWidth(simulationWidth / 6);
        lineChart.getData().addAll(animals, grass);
        lineChart.setCreateSymbols(false);
        return lineChart;
    }

    public void updateChartsValue(XYChart.Series energy, XYChart.Series animals, XYChart.Series grass,
                                  XYChart.Series liveSpan, XYChart.Series children, DataTracker tracker){
        animals.getData().add(new XYChart.Data(tracker.daysPassed(), tracker.numberOfAnimals()));
        grass.getData().add(new XYChart.Data(tracker.daysPassed(), tracker.getHowMuchGrassOnMap()));
        energy.getData().add(new XYChart.Data(tracker.daysPassed(), tracker.calculateAverageEnergy()));
        liveSpan.getData().add(new XYChart.Data(tracker.daysPassed(), tracker.getAverageAnimalLiveSpan()));
        children.getData().add(new XYChart.Data(tracker.daysPassed(), tracker.calculateAverageChildren()));

    }

    public LineChart createSingleLineChart(XYChart.Series series, String xAxisName, String yAxisName,
                                           String seriesName, String title){
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(xAxisName);
        yAxis.setLabel(yAxisName);
        series.setName(seriesName);
        LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);
        lineChart.setTitle(title);
        lineChart.setPrefWidth(simulationWidth / 6);
        lineChart.setMaxWidth(simulationWidth / 6);
        lineChart.getData().addAll(series);
        lineChart.setCreateSymbols(false);
        return lineChart;
    }

    public void leftButtonHandle(){
        try {
            leftThreadStopStart.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if(leftThreadRunning) {
                        leftThreadRunning = false;
                        engineThreadForLeftMap.suspend();
                    }
                    else {
                        leftThreadRunning = true;
                        engineThreadForLeftMap.resume();
                    }
                }
            });
        }catch(IllegalThreadStateException ex){
            out.printf("PROCES JUZ WYSTARTOWAL");
        }
        leftGenomeDisplayButton.setOnAction((new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gridPaneLeft = createGridPane(leftMap, gridPaneLeft ,true,
                        leftMapEngine.getTracker().findDominantGenes());
            }
        }));

    }

    public void rightButtonHandle(){
        try {
            rightThreadStopStart.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if(rightThreadRunning){
                        rightThreadRunning = false;
                        engineThreadForRightMap.suspend();
                    }
                    else {
                        rightThreadRunning = true;
                        engineThreadForRightMap.resume();
                    }
                }
            });
        }catch(IllegalThreadStateException ex){
            out.printf("PROCES JUZ WYSTARTOWAL");
        }

        rightGenomeDisplayButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gridPaneRight = createGridPane(rightMap, gridPaneRight ,true,
                        rightMapEngine.getTracker().findDominantGenes());
            }
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

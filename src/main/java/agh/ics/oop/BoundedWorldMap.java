package agh.ics.oop;

import java.util.*;

import static java.lang.Math.sqrt;

public class BoundedWorldMap implements IWorldMap,IPositionChangeObserver, IAnimalLifeCycleObserver,IGrassEatenObserver {

    protected Map<Vector2d, MapField> activeMapFields = new HashMap<>();
    protected Set<Vector2d> freeJungleFields = new HashSet<Vector2d>();
    protected Set<Vector2d> freeSawannaFields = new HashSet<Vector2d>();
    protected List<IAnimalLifeCycleObserver> animalLifeCycleObservers;

    protected int width;
    protected int height;
    protected double jungleRatio;
    protected Vector2d topCorner;
    protected Vector2d bottomCorner;
    protected int centreX;
    protected int centreY;
    protected int jungleWidth;
    protected int jungleHeight;
    protected int jungleTopCornerX;
    protected int jungleTopCornerY;
    protected int jungleBottomCornerX;
    protected int jungleBottomCornerY;
    protected Vector2d jungleTopCorner;
    protected Vector2d jungleBottomCorner;
    protected double areaPart;
    protected int grassNumber;

    public BoundedWorldMap(int width, int height, double jungleRatio){
        this.animalLifeCycleObservers = new ArrayList<>();
        this.grassNumber = 0;
        this.jungleRatio = jungleRatio;
        this.width = width;
        this.height = height;

        topCorner = new Vector2d(width - 1 ,height - 1);
        bottomCorner = new Vector2d(0,0);

        calculateJungle();

        for(int i = 0; i < width; i++)
            for(int j = 0; j < height; j++){
                if(inJungle(new Vector2d(i,j)))
                    freeJungleFields.add(new Vector2d(i,j));
                else
                    freeSawannaFields.add(new Vector2d(i,j));
            }
    }

    public void calculateJungle(){
        centreX = this.width / 2;
        centreY = this.height / 2;
        areaPart = partOfArea();
        jungleWidth = (int) (this.width * sqrt(jungleRatio));//trzeba to zmienic bo nie dziala ugułem ale zeby isc dalej jest
        jungleHeight = (int) (this.height * sqrt(jungleRatio));
        jungleTopCornerX = centreX + jungleWidth / 2;
        jungleTopCornerY = centreY + jungleHeight / 2;
        jungleBottomCornerX = centreX - jungleWidth / 2;
        jungleBottomCornerY = centreY - jungleHeight / 2;
        jungleTopCorner = new Vector2d(jungleTopCornerX,jungleTopCornerY);
        jungleBottomCorner = new Vector2d(jungleBottomCornerX,jungleBottomCornerY);
    }

    public boolean canMoveTo(Vector2d position) {
        return position.inside(topCorner,bottomCorner);
    }

    public boolean place(Animal animal) {
        Vector2d position = animal.getPosition();

        if(activeMapFields.get(position) == null) {
            createMapField(position);
        }

        activeMapFields.get(position).addAnimal(animal);

        if(inJungle(position)) // zastanow sie zy napewno tego chcesz
            freeJungleFields.remove(position);
        else
            freeSawannaFields.remove(position);//to mozna ifem ogarnac ktoro wykonac po pozycj
                                               // jak okrasle pozycje jungli
        return true;
    }

    public void createMapField(Vector2d position){
        MapField field = new MapField(position);
        activeMapFields.put(position, field);
        field.addObserverPosition(this);
        field.addObserverLifeCycle(this);
        field.addObserverGrass(this);
    }

    public boolean initialPlace(Animal animal){
        if(activeMapFields.get(animal.getPosition()) != null && activeMapFields.get(animal.getPosition()).containAnimals())
            return false;

        place(animal);
        return true;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position)!= null;
    }

    @Override
    public Object objectAt(Vector2d position) {
        return activeMapFields.get(position);
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal) {
        place(animal);
        freeField(oldPosition);
    }

    public String toString (){
        MapVisualizer mapVisualizer = new MapVisualizer(this);
        return mapVisualizer.draw(bottomCorner, topCorner);
    }

    public boolean inJungle(Vector2d position){//zakladajac ze dziala ok musze to napisac
        return position.inside(jungleTopCorner,jungleBottomCorner);
    }

    public void eatGrass(){
        for (MapField field : activeMapFields.values()) {
            field.eat();
        }
    }

    public void freeField(Vector2d position){
        if(!activeMapFields.get(position).containAnimals()){
            if(!activeMapFields.get(position).containsGrass()){
                if(inJungle(position))// jezeli nic tam nie stoi, ani nie rośnie no to znowu mogę dodac tam trawe
                    freeJungleFields.add(position);
                else
                    freeSawannaFields.add(position);
                activeMapFields.remove(position);
            }
        }
    }

    public void breedAnimals(){
        for (MapField field : activeMapFields.values()) {
            field.breed();
        }
    }

    public void removeDeadAnimals(){
        List<Vector2d> positions = new ArrayList<>();
        for (Vector2d position : activeMapFields.keySet()) {
            activeMapFields.get(position).whoDied();
            positions.add(position);
        }

        for(Vector2d position : positions)
            freeField(position);
    }

    @Override
    public void animalBorn(Animal animal) {
        for(IAnimalLifeCycleObserver observer : animalLifeCycleObservers)
            observer.animalBorn(animal);
    }

    @Override
    public void animalDied(Animal animal) {
        for(IAnimalLifeCycleObserver observer : animalLifeCycleObservers)
            observer.animalDied(animal);
    }

    public void addObserverDeath(IAnimalLifeCycleObserver Observer){
        animalLifeCycleObservers.add(Observer);
    }

    public void removeObserverDeath(IAnimalLifeCycleObserver Observer){
        animalLifeCycleObservers.remove(Observer);
    }


    public void spawnGrass(){
        if(freeSawannaFields.size() != 0)
            spawnGrassAt(freeSawannaFields);
        if(freeJungleFields.size() != 0)
            spawnGrassAt(freeJungleFields);
    }

    public void spawnGrassAt(Set<Vector2d> freeFields){
        int size = freeFields.size();
        Random random = new Random();
        int desiredPositionInSet = random.nextInt(size);
        int i = 0;
        Vector2d position = new Vector2d(0,0);
        Iterator<Vector2d> it = freeFields.iterator();
        while(it.hasNext() ) {
            i++;
            position = it.next();
            if(i == desiredPositionInSet){
                break;
            }
        }
        freeFields.remove(position);
        if(activeMapFields.get(position) == null) {
            createMapField(position);
        }
        grassNumber++;
        activeMapFields.get(position).growGrass();
    }

    private double partOfArea(){
        int nominator = (int)(this.jungleRatio*1000000);
        int denominator = 1000000;

        int gcd = gcd(nominator,denominator);
        double shortenedNominator = (nominator/gcd);
        double shortenedDenominator = (denominator/gcd);
        return shortenedNominator / (shortenedDenominator + shortenedNominator);
    }

    private int gcd (int a , int b){
        return b == 0 ? a : gcd(b, a%b);
    }


    @Override
    public void grassEaten() {
        grassNumber--;
    }

    public int getGrassAmount(){
        return grassNumber;
    }

    public int getWidth() {
        return this.width;
    }
    public int getHeight() {
        return this.height;
    }
}

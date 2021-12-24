package agh.ics.oop;

import java.util.*;

import static java.lang.Math.sqrt;

public class AbstractWorldMap implements IWorldMap,IPositionChangeObserver, IAnimalLifeCycleObserver,IGrassEatenObserver  {
    private Map<Vector2d, MapField> activeMapFields = new HashMap<>();
    private Set<Vector2d> freeJungleFields = new HashSet<Vector2d>();
    private Set<Vector2d> freeSawannaFields = new HashSet<Vector2d>();
    private List<IAnimalLifeCycleObserver> animalLifeCycleObservers;

    private int width;
    private int height;
    private double jungleRatio;
    private Vector2d topCorner;
    private Vector2d bottomCorner;
    private int centreX;
    private int centreY;
    private int jungleWidth;
    private int jungleHeight;
    private int jungleTopCornerX;
    private int jungleTopCornerY;
    private int jungleBottomCornerX;
    private int jungleBottomCornerY;
    private Vector2d jungleTopCorner;
    private Vector2d jungleBottomCorner;
    private double areaPart;
    private int grassNumber;

    public AbstractWorldMap(int width, int height, double jungleRatio){
        this.animalLifeCycleObservers = new ArrayList<>();
        this.grassNumber = 0;
        this.jungleRatio = jungleRatio;
        this.width = width;
        this.height = height;

        topCorner = new Vector2d(width - 1 ,height - 1);
        bottomCorner = new Vector2d(0,0);

        //out.println(this.width+ " " + this.height);
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

        for(int i = 0; i < width; i++)
            for(int j=0; j < height; j++){
                if(inJungle(new Vector2d(i,j)))
                    freeJungleFields.add(new Vector2d(i,j));
                else
                    freeSawannaFields.add(new Vector2d(i,j));
            }
    }

    public boolean initialPlace(Animal animal){
        if(activeMapFields.get(animal.getPosition()) != null && activeMapFields.get(animal.getPosition()).containAnimals())
            return false;

        place(animal);
        return true;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return false;
    }

    @Override
    public boolean place(Animal animal) {
        return false;
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

    public void breedAnimals(){
        for (MapField field : activeMapFields.values()) {
            field.breed();
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
            spawnSawannaGrass();
        if(freeJungleFields.size() != 0)
            spawnJungleGrass();
    }

    public void spawnSawannaGrass(){
        int size = freeSawannaFields.size();
        Random random = new Random();
        int desiredPositionInSet = random.nextInt(size);
        int i = 0;
        Vector2d position = new Vector2d(0,0);
        Iterator<Vector2d> it = freeSawannaFields.iterator();
        while(it.hasNext() ) {
            i++;
            position = it.next();
            if(i == desiredPositionInSet){
                break;
            }
        }
        freeSawannaFields.remove(position);
        if(activeMapFields.get(position) == null) {
            MapField field = new MapField(position);
            activeMapFields.put(position, field);
            field.addObserverPosition(this);
            field.addObserverLifeCycle(this);
            field.addObserverGrass(this);
        }
        grassNumber++;
        activeMapFields.get(position).growGrass();
    }

    public void spawnJungleGrass(){
        int size = freeJungleFields.size();
        Random random = new Random();
        int desiredPositionInSet = random.nextInt(size);
        int i = 0;
        Vector2d position = new Vector2d(0,0);
        Iterator<Vector2d> it = freeJungleFields.iterator();
        while(it.hasNext() ) {
            i++;
            position = it.next();
            if(i == desiredPositionInSet){
                break;
            }
        }

        freeJungleFields.remove(position);
        if(activeMapFields.get(position) == null) {
            MapField field = new MapField(position);
            activeMapFields.put(position, field);
            field.addObserverPosition(this);
            field.addObserverLifeCycle(this);
            field.addObserverGrass(this);
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
}

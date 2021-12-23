package agh.ics.oop;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static java.lang.Math.sqrt;
import static java.lang.System.out;

public class BoundedWorldMap implements IWorldMap,IPositionChangeObserver {
    java.util.Comparator<Vector2d> Comparator = new Comparator<Vector2d>() {
        @Override
        public int compare(Vector2d firstPosition, Vector2d secondPosition) {
          if(firstPosition.getX() != (secondPosition.getX()))
                return firstPosition.getX().compareTo(secondPosition.getX());
            return firstPosition.getY().compareTo(secondPosition.getY());
        }
    };

    private Map<Vector2d, MapField> activeMapFields = new HashMap<>();
    private Set<Vector2d> positionsWithAnimals = new TreeSet<>(Comparator);
    private Set<Vector2d> freeJungleFields = new TreeSet<>(Comparator);
    private Set<Vector2d> freeSawannaFields = new TreeSet<>(Comparator);

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

    public BoundedWorldMap(int width, int height, double jungleRatio){
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

    public boolean canMoveTo(Vector2d position) {
        return position.inside(topCorner,bottomCorner);
    }

    public boolean place(Animal animal) {
        Vector2d position = animal.getPosition();
        if(activeMapFields.get(position) == null) {
            MapField field = new MapField(position);
            activeMapFields.put(position, field);
            field.addObserver(this);
        }
        activeMapFields.get(position).addAnimal(animal);
        positionsWithAnimals.add(position);
        if(inJungle(position))
            freeJungleFields.remove(position);
        else
            freeSawannaFields.remove(position);//to mozna ifem ogarnac ktoro wykonac po pozycj
                                                        // jak okrasle pozycje jungli
        return true;
    }

    public boolean initialPlace(Animal animal){
        if(activeMapFields.get(animal.getPosition()) != null)
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

    public void moveAnimals(){
        for(Vector2d position : positionsWithAnimals)
            activeMapFields.get(position).moveAnimals();
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal) {
        if(activeMapFields.get(oldPosition).containAnimals()){//po prostu przesuwam zwierzę na nowe pole
            place(animal);
        }
        else{
            positionsWithAnimals.remove(oldPosition); // jezeli na danym polu nie ma zwierzat
            //usuwam je z listy pól ze zwierzętami
            place(animal);
            if(!activeMapFields.get(oldPosition).containsGrass()){ //jeżeli obiekt nie trzyma ani zwierząt ani
                // trawy to nie jest nam potrzebny
                //activeMapFields.get(oldPosition).removeObserver(this);
                activeMapFields.remove(oldPosition);
            }
        }

    }

    public void checkWhoEats(){
        for(Vector2d position : positionsWithAnimals)
            activeMapFields.get(position).eat();
    }

    public String toString (){
        MapVisualizer mapVisualizer = new MapVisualizer(this);
        return mapVisualizer.draw(bottomCorner, topCorner);
    }

    public boolean inJungle(Vector2d position){//zakladajac ze dziala ok musze to napisac
        return position.inside(jungleTopCorner,jungleBottomCorner);
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


}

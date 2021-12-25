package agh.ics.oop;

public class UnBoundedWorldMap extends BoundedWorldMap{

    public UnBoundedWorldMap(int width, int height, double jungleRatio) {
        super(width, height, jungleRatio);
    }
    @Override
    public boolean canMoveTo(Vector2d position) {
        return true;
    }

    @Override
    public boolean place(Animal animal) {
        Vector2d position = animal.getPosition();
        if(!position.inside(topCorner,bottomCorner))
        {
            if(position.getX() < bottomCorner.getX())
                position.setX(topCorner.getX());

            else if(position.getX() > topCorner.getX())
                position.setX(bottomCorner.getX());

            if(position.getY() < bottomCorner.getY())
                position.setY(topCorner.getY());

            else if(position.getY() > topCorner.getY())
                position.setY(bottomCorner.getY());

        }
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
}

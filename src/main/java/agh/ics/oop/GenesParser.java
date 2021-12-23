package agh.ics.oop;

public class GenesParser {
    public MoveDirection[] parse(int[] genes){
        int size = 32;
        int counter = 0;
        MoveDirection[] output = new MoveDirection[size];

        for(Integer gene : genes){
            switch(gene){
                case 0 -> output[counter] = MoveDirection.BACKWARD;
                case 1 -> output[counter] = MoveDirection.HALF_RIGHT;
                case 2 -> output[counter] = MoveDirection.RIGHT;
                case 3 -> output[counter] = MoveDirection.ONE_AND_HALF_RIGHT;
                case 4 -> output[counter] = MoveDirection.FORWARD;
                case 5 -> output[counter] = MoveDirection.ONE_AND_HALF_LEFT;
                case 6 -> output[counter] = MoveDirection.LEFT;
                case 7 -> output[counter] = MoveDirection.HALF_LEFT;
            }
            counter += 1;
        }
        return output;
    }
}


public class Location {
    private String[] ID = new String[2];
    private int[] position = new int[2];


    public Location() {

    }


    //constructor
    public Location(String colour, String number, int x, int y) {
        ID[0] = colour;
        ID[1] = number;
        position[0] = x;
        position[1] = y;
    }

    //overloaded setters
    public void setID(String colour, String number) {
        ID[0] = colour;
        ID[1] = number;
    }

    public void setPosition(int x, int y) {
        position[0] = x;
        position[1] = y;
    }

    public int getx() {
        return position[0];
    }

    public int gety() {
        return position[1];
    }

    public String[] getID() {
        return ID;
    }

    public int[] getPosition() {
        return position;
    }

    public Location(String[] ID, int[] position) {
        this.ID = ID;
        this.position = position;
    }

}

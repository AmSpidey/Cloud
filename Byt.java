package chmura;

public class Byt {
    private int x;
    private int y;
    private Chmura mojaChmura;


    Byt(int x, int y, Chmura mojaChmura) {
        this.x = x;
        this.y = y;
        this.mojaChmura = mojaChmura;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    void samobójstwo() {
        mojaChmura = null;
    }

    Chmura getMojaChmura() {
        return mojaChmura;
    }

    synchronized void wektor(int dx, int dy) {
        x = x + dx;
        y = y + dy;
    }
}


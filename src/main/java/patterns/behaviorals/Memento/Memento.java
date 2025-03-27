package patterns.behaviorals.Memento;


/**
  This pattern is used to preserve and restore the state of an object. A Memento twin of the object is created,
  which has the responsibility to restore the previous state.
**/
class Shape {
    int posX;
    int posY;

    Shape(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    void move(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    void displayPos() {
        System.out.println("posX: " + posX + " posY: " + posY);
    }
}

class ShapeMemento {
    Shape shape;

    ShapeMemento(Shape shape) {
        this.shape = new Shape(shape.posX, shape.posY);
    }

    Shape restoreState() {
        return shape;
    }
}

public class Memento {
    public static void main(String[] args) {
        Shape s = new Shape(10,10);
        ShapeMemento sm = new ShapeMemento(s);
        s.displayPos();
        s.move(15,15);
        s.displayPos();
        s = sm.restoreState();
        s.displayPos();
    }
}

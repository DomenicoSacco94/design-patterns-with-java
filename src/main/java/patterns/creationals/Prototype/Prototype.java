package patterns.creationals.Prototype;

/**
    The prototype is a design pattern used when creating an object is really expensive and time-consuming
    so as a shortcut we just clone form an existing object and apply some small tweaks.
**/

// we apply clone to the interface so we are forced to customize clone for every shape
interface Shape extends Cloneable {
    void calculateArea();
    Shape clone();
}

class Square implements Shape {
    public void calculateArea() {
        System.out.println("Calculating the square area");
    }

    @Override
    public Square clone() {
        try {
            return (Square) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}

class Circle implements Shape {
    public void calculateArea() {
        System.out.println("Calculating the circle area");
    }

    @Override
    public Circle clone() {
        try {
            return (Circle) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}

// note the lazy initialization
class PrototypeManager {
    private Shape circle, square;

    public Circle getCircle() {
        if(circle == null) {
            circle = new Circle();
        }
        return (Circle) circle.clone();
    }

    public Square getSquare() {
        if(square == null) {
            square = new Square();
        }
        return (Square) square.clone();
    }

}


public class Prototype {
    public static void main(String[] args) {
        PrototypeManager prototypeManager = new PrototypeManager();
        prototypeManager.getCircle().calculateArea();
        prototypeManager.getSquare().calculateArea();
    }
}
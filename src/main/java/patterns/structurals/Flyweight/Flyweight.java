package patterns.structurals.Flyweight;



import java.util.HashMap;
import java.util.Map;

enum Size {
    SMALL, MEDIUM, BIG
}

/**
    The Flyweight pattern is used to optimize the instantiation of several objects sharing the same intrinsic state.
    A sort of "caching" is implemented, lazily instantiating objects with specific properties.
**/
abstract class Shape {
    Size size;

    Shape(Size size) {
        this.size = size;
    }

    abstract void draw();
}

class Square extends Shape {

    Square(Size size) {
        super(size);
        // assume the instantiation is quite heavy
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void draw() {
        System.out.println("drawing a square");
    }
}

class Circle extends Shape {
    Circle(Size size) {
        super(size);
        // assume the instantiation is quite heavy
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void draw() {
        System.out.println("drawing a circle");
    }
}

class ShapeFlyweightFactory {

    Map<Size, Square> squareMap;
    Map<Size, Circle> circleMap;

    ShapeFlyweightFactory() {
        this.squareMap = new HashMap<>();
        this.circleMap = new HashMap<>();
    }

    Square getSquare(Size size) {
        if(squareMap.get(size) == null) {
            squareMap.put(size, new Square(size));
        }
        return squareMap.get(size);
    }

    Circle getCircle(Size size) {
        if(circleMap.get(size) == null) {
            circleMap.put(size, new Circle(size));
        }
        return circleMap.get(size);
    }

}


public class Flyweight {
    public static void main(String[] args) {
        ShapeFlyweightFactory sff = new ShapeFlyweightFactory();
        for(int i=0;i<10;i++) {
            // It will only instantiate one square
            Square s = sff.getSquare(Size.SMALL);
            s.draw();
        }
        for(int i=0;i<10;i++) {
            // It will only instantiate one circle
            Circle c = sff.getCircle(Size.MEDIUM);
            c.draw();
        }
    }
}

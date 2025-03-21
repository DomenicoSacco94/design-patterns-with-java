package patterns.structurals.Adapter;

import java.util.ArrayList;
import java.util.List;

/**
 The adapters are used to allow a class to communicate with another we cannot modify, key is:
 - adapter classes extend the class we want to adapt TO
 - have in the constructor and in the state a reference to the class we want to adapt FROM
 **/
abstract class Shape {
    int area;
    abstract void calculateArea();

    Shape(int area) {
        this.area = area;
    }
}

class Circle extends Shape {

    Circle(int area) {
        super(area);
    }

    @Override
    public void calculateArea() {
        System.out.println("Calculating area of the circle");
    }
}

class Square extends Shape {

    Square(int area) {
        super(area);
    }

    @Override
    public void calculateArea() {
        System.out.println("Calculating area of the square");
    }
}

class OldSquare {
    int perimeter;

    OldSquare(int perimeter) {
        this.perimeter = perimeter;
    }

    public int getPerimeter() {
        return perimeter;
    }

}

class OldNewSquareAdapter extends Square {
    private OldSquare oldSquare;

    OldNewSquareAdapter(OldSquare oldSquare) {
        super((int) Math.pow(oldSquare.getPerimeter()/4,2));
        this.oldSquare = oldSquare;
    }

}


public class Adapter {
    public static void main(String[] args) {
        List<Shape> shapes = new ArrayList<>();

        shapes.add(new Square(100));
        shapes.add(new Circle(100));

        OldSquare oldSquare = new OldSquare(12);
        shapes.add(new OldNewSquareAdapter(oldSquare));
    }
}
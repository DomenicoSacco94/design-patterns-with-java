package patterns.structurals.Composite;

import java.util.ArrayList;
import java.util.List;

/**
 This pattern is generally used when both the part of an object and the object itself share the same behaviour
 (e.g. calculate the total cost of a menu and the single menu item)
 **/

abstract class Shape {

    int area;

    Shape(int area) {
        this.area = area;
    }

    public int getArea() {
        return area;
    }

    public abstract void addShape(Shape shape);
    public abstract int getTotalArea();

}

class Square extends Shape {

    Square(int area) {
        super(area);
    }

    @Override
    public void addShape(Shape shape) {
        // not used
    }

    @Override
    public int getTotalArea() {
        return area;
    }
}

class ComplexShape extends Shape {

    List<Shape> shapes;

    ComplexShape(int area) {
        super(area);
        shapes = new ArrayList<>();
    }

    @Override
    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    @Override
    public int getTotalArea() {
        return shapes.stream().mapToInt(Shape::getArea).sum();
    }
}


public class Composite {

    public static void main(String[] args) {
        // we can add shapes in complex shapes and so on, with several levels of depth
        ComplexShape shape1 = new ComplexShape(5);
        Square square1 = new Square(3);
        Square square2 = new Square(2);
        ComplexShape shape2 = new ComplexShape(1);
        shape2.addShape(shape1);
        shape2.addShape(square1);
        shape2.addShape(square2);
        System.out.println("Total area is " + shape2.getTotalArea());
    }

}

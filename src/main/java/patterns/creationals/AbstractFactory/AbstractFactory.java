package patterns.creationals.AbstractFactory;

/**
    The abstract factory pattern is used when you want to create a family of related objects
    without having to specify the "Mother factory" as a concrete class (here is even an interface)
**/

interface Shape {
    void calculateArea();
}

class Circle implements Shape {

    @Override
    public void calculateArea() {
        System.out.println("Calculating area of the circle");
    }
}

class Square implements Shape {

    @Override
    public void calculateArea() {
        System.out.println("Calculating area of the square");
    }
}

interface ShapeFactory {
    Shape createShape();
}

class CircleFactory implements ShapeFactory {
    public Shape createShape() {
        return new Circle();
    }
}

class SquareFactory implements ShapeFactory {
    public Shape createShape() {
        return new Square();
    }
}

public class AbstractFactory {
    public static void main(String[] args) {
        // aside from the assignment on the right side, at runtime we do not even know it is a square
        ShapeFactory shapefactory = new SquareFactory();
        Shape aShape = shapefactory.createShape();
        // the shape is a square
        aShape.calculateArea();
    }
}
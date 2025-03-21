package patterns.creationals.Factory;

/**
    This design patterns works well when you want to use a mother factory class and return a child factory
    depending on what it is passed on its constructor
**/

abstract class Shape {
    public abstract void calculateArea();
    double area;

    Shape(double area) {
        this.area = area;
    }
}

class Circle extends Shape {

    private double radius;

    Circle(double radius) {
        super(radius*3.14);
        this.radius = radius;
    }

    @Override
    public void calculateArea() {
        System.out.println("Calculating area of the circle: " + area);
    }
}

class Square extends Shape {

    private double side;

    Square(double side) {
        super(side*side);
        this.side = side;
    }

    @Override
    public void calculateArea() {
        System.out.println("Calculating area of the square: " + area);
    }
}

enum Size {
    BIG,
    MEDIUM,
    SMALL
}

enum ShapeType {
    SQUARE,
    CIRCLE
}

interface InstanceFactory {
    Shape create(Size size);
}

class SquareFactory implements InstanceFactory {

    public Shape create(Size size) {
        return switch (size) {
            case SMALL -> new Square(50);
            case MEDIUM -> new Square(100);
            case BIG -> new Square(150);
        };
    }
}

class CircleFactory implements InstanceFactory {

    public Shape create(Size size) {
        return switch (size) {
            case SMALL -> new Circle(50);
            case MEDIUM -> new Circle(100);
            case BIG -> new Circle(150);
        };
    }
}

// usually the mother factory has a static method
class ShapeFactory {
    public static Shape create(ShapeType shapeType, Size size) {
        InstanceFactory instanceFactory = switch (shapeType) {
            case SQUARE -> new SquareFactory();
            case CIRCLE -> new CircleFactory();
        };

        return instanceFactory.create(size);
    }
}

public class Factory {
    public static void main(String[] args) {
        // the polymorphism is evident: we do not even need to instantiate anything ourselves, just supply the enums
        ShapeFactory.create(ShapeType.SQUARE, Size.BIG).calculateArea();
        ShapeFactory.create(ShapeType.CIRCLE, Size.SMALL).calculateArea();
    }
}
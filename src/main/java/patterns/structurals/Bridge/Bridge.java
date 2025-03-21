package patterns.structurals.Bridge;

/*
    In theory, the Bridge pattern is described as a method to allow an abstraction and an implementation
    to grow de-coupled and be modified without affecting each other.
    In practice, it means that the implementation includes a reference to the abstraction, using
    the composition-over-inheritance approach
 */

interface Shape {
    double calculatePerimeter();
    double calculateArea();
}

class Square implements Shape {
    double side;

    Square(double side) {
        this.side = side;
    }

    public double calculateArea() {
        return side*side;
    }

    public double calculatePerimeter() {
        return side*4;
    }
}

class Circle implements Shape {
    double radius;

    Circle(double radius) {
        this.radius = radius;
    }

    public double calculateArea() {
        return radius * radius * 3.14;
    }

    public double calculatePerimeter() {
        return radius * 3.14;
    }
}

interface ShapeOperations {
    void move();
    void delete();
}


// without the bridge, ShapeEditor could be just implemented inside a Shape abstract class
// but this would force us to modify its children, with the bridge we avoid this
// we can add other operations such as resize, rotate... and we won't touch the whole Shape hierarchy tree
// conversely, we can deepen or breathen the Shape tree and ShapeEditor and ShapeOperations will remain unaffected
class ShapeEditor implements ShapeOperations {
    Shape shape; //keeping the reference to the bridge class

    ShapeEditor(Shape shape) {
        this.shape = shape;
    }

    @Override
    public void move() {
        System.out.println("I am moving a shape with area " + shape.calculateArea());
    }

    @Override
    public void delete() {
        System.out.println("I am deleting a shape with area " + shape.calculateArea());
    }
}


public class Bridge {
    public static void main(String[] args) {
            Shape square = new Square(100);
            ShapeEditor shapeEditor = new ShapeEditor(square);
            shapeEditor.move();
            shapeEditor.delete();
    }
}
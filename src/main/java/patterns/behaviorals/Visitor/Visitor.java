package patterns.behaviorals.Visitor;

/**
 This pattern is used to introduce new functionality into a class hierarchy bringing only a few changes
 It consists in adding a Visitor class and a corresponding method to invoke it the visited class.
 **/

interface Shape extends Visitable {
}

class Square implements Shape {

    double side;

    Square(double side) {
        this.side = side;
    }

    @Override
    public void visit(ShapeVisitor shapeVisitor) {
        shapeVisitor.visit(this);
    }
}

class Circle implements Shape {

    double radius;

    Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public void visit(ShapeVisitor shapeVisitor) {
        shapeVisitor.visit(this);
    }
}


interface Visitable {
    void visit(ShapeVisitor shapeVisitor);
}

interface ShapeVisitor {
    void visit(Shape shape);
}

class DisplayVisitor implements ShapeVisitor {

    @Override
    public void visit(Shape shape) {
        System.out.println("I am displaying the shape " + shape);
    }
}

public class Visitor {
    public static void main(String[] args) {
        Shape circle = new Circle(10);
        Shape square = new Square(10);
        DisplayVisitor displayVisitor = new DisplayVisitor();
        circle.visit(displayVisitor);
        square.visit(displayVisitor);
    }
}

package patterns.structurals.Decorator;

/*
    The decorator pattern is used to add an arbitrary number of properties/changes to a class without having to
    create too many inheritance relationships.
    The important thing to remember is that the decorator itself extends the class it wants to decorate and, at the same time,
    It keeps a reference of it, which can be altered by each decorator.
 */

abstract class Shape {
    String properties;

    Shape() {
        this.properties = "";
    }

    String getProperties() {
        return properties;
    }
}

class Square extends Shape {
}

abstract class ShapeDecorator extends Shape {
    Shape shape;

    ShapeDecorator(Shape shape) {
        super();
        this.shape = shape;
    }

    abstract String getProperties();
}

class ShapeFiller extends ShapeDecorator {
    ShapeFiller(Shape shape) {
        super(shape);
    }

    @Override
    String getProperties() {
        return shape.getProperties() + " FILLED";
    }

}

class ShapeEnlarger extends ShapeDecorator {
    ShapeEnlarger(Shape shape) {
        super(shape);
    }

    @Override
    String getProperties() {
        return shape.getProperties() + " ENLARGED";
    }
}

class ShapeColorer extends ShapeDecorator {
    ShapeColorer(Shape shape) {
        super(shape);
    }

    @Override
    String getProperties() {
        return shape.getProperties() + " COLORED";
    }
}


public class Decorator {
    public static void main (String[] args) {
        Shape s = new Square();
        Shape decoratedShape = new ShapeFiller(s);
        // we can now call an arbitrary number of decorators
        decoratedShape = new ShapeEnlarger(decoratedShape);
        decoratedShape = new ShapeColorer(decoratedShape);
        System.out.println(decoratedShape.getProperties());
    }
}

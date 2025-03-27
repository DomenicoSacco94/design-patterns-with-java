package patterns.creationals.Builder;

/**
    The builder pattern is used when you want to decouple the initialization of an object from its representation.
    It has a Director class specifying how to build the object and the builder itself which does it.
**/

interface Shape {
    void calculateArea();
}

class FilledShape implements Shape {

    @Override
    public void calculateArea() {
        System.out.println("Calculating Area of the filled shape");
    }
}

class EmptyShape implements Shape {

    @Override
    public void calculateArea() {
        System.out.println("Calculating Area of the empty shape");
    }
}

//An abstract class instead of an interface because some of the methods might be optional
abstract class ShapeBuilder {
    void drawPerimeter() {}
    void fillShape() {}
    abstract Shape getShape();
}

class FilledShapeBuilder extends ShapeBuilder {

    Shape filledShape;

    FilledShapeBuilder() {
        filledShape = new FilledShape();
    }

    @Override
    void drawPerimeter() {
        System.out.println("Drawing the shape perimeter");
    }

    @Override
    void fillShape() {
        System.out.println("Filling up the shape");
    }

    @Override
    Shape getShape() {
        return filledShape;
    }
}


// the fillShape method will not be extended
class EmptyShapeBuilder extends ShapeBuilder {

    Shape emptyShape;

    EmptyShapeBuilder() {
        emptyShape = new EmptyShape();
    }

    @Override
    void drawPerimeter() {
        System.out.println("Drawing the shape perimeter");
    }

    @Override
    Shape getShape() {
        return emptyShape;
    }
}

interface ShapeDirector {
    Shape build(ShapeBuilder shapeBuilder);
}

class EmptyShapeDirector implements ShapeDirector {

    @Override
    public Shape build(ShapeBuilder emptyShapeBuilder) {
        emptyShapeBuilder.drawPerimeter();
        return emptyShapeBuilder.getShape();
    }
}

class FilledShapeDirector implements ShapeDirector {

    @Override
    public Shape build(ShapeBuilder filledShapeBuilder) {
        filledShapeBuilder.drawPerimeter();
        filledShapeBuilder.fillShape();
        return filledShapeBuilder.getShape();
    }
}

public class Builder {
    public static void main(String[] args) {
        // at runtime I only know the left part of the assignments
        ShapeBuilder shapeBuilder = new FilledShapeBuilder();
        ShapeDirector shapeDirector = new FilledShapeDirector();
        Shape shape = shapeDirector.build(shapeBuilder);
        shape.calculateArea();
    }
}
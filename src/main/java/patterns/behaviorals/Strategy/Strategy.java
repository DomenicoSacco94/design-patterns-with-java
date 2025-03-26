package patterns.behaviorals.Strategy;

/**
  This pattern is used to decouple the classes and the algorithms used to perform operations on them.
  It can be applied when we want to change at run-time the algorithm we used to perform a certain operation on a class.
**/
abstract class Shape {
    private int degrees;
    private ShapeManipulation shapeManipulation;

    Shape() {
        this.degrees = 0;
    }

    public int getDegrees() {
        return degrees;
    }

    public void setDegrees(int degrees) {
        this.degrees = degrees;
    }

    public void setShapeManipulationStrategy(ShapeManipulation shapeManipulation) {
        this.shapeManipulation = shapeManipulation;
    }

    public void applyStrategy(int degrees) {
        this.shapeManipulation.applyOperation(this, degrees);
        System.out.println("Shape rotation is now at " + getDegrees());
    }

}

class Square extends Shape {

}

interface ShapeManipulation {
    void applyOperation(Shape shape, int degrees);
}

class RotateLeftManipulation implements ShapeManipulation {

    @Override
    public void applyOperation(Shape shape, int degrees) {
            int curDegrees = shape.getDegrees();
            shape.setDegrees(curDegrees-degrees);
    }
}

class RotateRightManipulation implements ShapeManipulation {

    @Override
    public void applyOperation(Shape shape, int degrees) {
        int curDegrees = shape.getDegrees();
        shape.setDegrees(curDegrees+degrees);
    }
}


public class Strategy {
    public static void main(String[] args) {
        Shape square = new Square();
        square.setShapeManipulationStrategy(new RotateRightManipulation());
        square.applyStrategy(10);
        square.setShapeManipulationStrategy(new RotateLeftManipulation());
        square.applyStrategy(10);
    }
}

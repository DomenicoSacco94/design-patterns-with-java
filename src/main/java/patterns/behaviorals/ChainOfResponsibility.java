package patterns.behaviorals;

import java.util.Arrays;

/**
 This design pattern is used when we want to associate multiple (mutually exclusive) consumers to a producer.
 It also allows to establish a hierarchy of consumers to fend off ambiguity: when the produced element satisfies the
 criteria of more than one consumer at a time, only the consumer that comes before will be triggered.
**/

interface ShapeHandler {
    void nextHandler(String s);
    void handleShape(String s);
}

abstract class AbstractShapeHandler implements ShapeHandler {

    ShapeHandler sh;

    public void setNextHandler(ShapeHandler sh) {
        this.sh = sh;
    }

    public void handleShape(String s) {
        if(getShapeName().equals(s)) {
            processShape();
        } else {
            nextHandler(s);
        }
    }

    public void nextHandler(String s) {
        sh.handleShape(s);
    }

    abstract void processShape();
    abstract String getShapeName();
}

class SquareHandler extends AbstractShapeHandler {

    @Override
    void processShape() {
        System.out.println("Processing a square");
    }

    @Override
    String getShapeName() {
        return "square";
    }
}

class CircleHandler extends AbstractShapeHandler {

    @Override
    void processShape() {
        System.out.println("Processing a circle");
    }

    @Override
    String getShapeName() {
        return "circle";
    }
}

class RectangleHandler extends AbstractShapeHandler {

    @Override
    void processShape() {
        System.out.println("Processing a rectangle");
    }

    @Override
    String getShapeName() {
        return "rectangle";
    }
}

public class ChainOfResponsibility {

    public static void processShape(String s) {
        SquareHandler sq = new SquareHandler();
        CircleHandler ci = new CircleHandler();
        RectangleHandler rh = new RectangleHandler();

        rh.setNextHandler(ci);
        ci.setNextHandler(sq);

        rh.handleShape(s);
    }

    public static void main(String[] args) {
        String[] shapes = {"square","circle","rectangle"};
        Arrays.stream(shapes).toList().forEach(ChainOfResponsibility::processShape);
    }
}

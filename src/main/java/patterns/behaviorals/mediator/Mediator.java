package patterns.behaviorals.mediator;

import java.util.ArrayList;
import java.util.List;

/**
  This pattern is usually used to mediate the interaction between different objects without them having to explicitly refer to each other.
  Each object registers itself with the mediator and calls the mediator class for every action.
 **/
abstract class Shape {
    private int posX;
    private int posY;
    private final CanvasShapeMediator canvasShapeMediator;

    Shape(CanvasShapeMediator canvasShapeMediator) {
        this.canvasShapeMediator = canvasShapeMediator;
    }

    void move(int posX, int posY) {
        this.canvasShapeMediator.moveShape(posX, posY);
    }

    void setPosX(int posX) {
        this.posX = posX;
    }

    void setPosY(int posY) {
        this.posY = posY;
    }

    void displayPos() {
        System.out.println("posX: " + posX + " posY: " + posY);
    }
}

class Square extends Shape {
    Square(CanvasShapeMediator canvasShapeMediator) {
        super(canvasShapeMediator);
    }
}

class Circle extends Shape {
    Circle(CanvasShapeMediator canvasShapeMediator) {
        super(canvasShapeMediator);
    }
}

class Canvas {
    private int width;
    private int height;
    private CanvasShapeMediator canvasShapeMediator;

    Canvas(CanvasShapeMediator canvasShapeMediator, int width, int height) {
        this.width = width;
        this.height = height;
        this.canvasShapeMediator = canvasShapeMediator;
    }

    void resize(int width, int height) {
        canvasShapeMediator.resizeCanvas(width, height);
    }

    int getWidth() {
        return this.width;
    }

    int getHeight() {
        return this.height;
    }
}

class CanvasShapeMediator {
    private Canvas canvas;
    private Shape shape;

    void registerCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    void registerShape(Shape shape) {
        this.shape = shape;
    }

    void moveShape(int posX, int posY) {
        shape.setPosX(Math.min(canvas.getWidth(), posX));
        shape.setPosY(Math.min(canvas.getHeight(), posY));
    }

    void resizeCanvas(int posX, int posY) {
        shape.setPosX(Math.min(canvas.getWidth(), posX));
        shape.setPosY(Math.min(canvas.getHeight(), posY));
    }

}

public class Mediator {
    public static void main(String[] args) {
        CanvasShapeMediator canvasShapeMediator = new CanvasShapeMediator();
        Canvas canvas = new Canvas(canvasShapeMediator, 100,100);
        canvasShapeMediator.registerCanvas(canvas);
        Shape square = new Square(canvasShapeMediator);
        canvasShapeMediator.registerShape(square);
        square.move(150,150);
        square.displayPos();
        canvas.resize(50,50);
        square.displayPos();
    }
}

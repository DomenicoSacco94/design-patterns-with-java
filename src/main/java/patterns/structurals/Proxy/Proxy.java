package patterns.structurals.Proxy;

/*
    This design pattern is applied when an object needs to be manipulated indirectly (because for instance it contains a method taking
    a lot of time that can be executed on another thread).
 */

interface Shape {
    void draw();
}

enum ShapeType {
    SQUARE, CIRCLE
}

class Square implements Shape {

    public void draw() {
        //assume the drawing is quite resource-consuming
        try {
            System.out.println("starting to draw a square");
            Thread.sleep(5000);
            System.out.println("drawing a square complete");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class Circle implements Shape {

    public void draw() {
        //assume the drawing is quite resource-consuming
        try {
            System.out.println("starting to draw a circle");
            Thread.sleep(5000);
            System.out.println("drawing a circle complete");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class ShapeProxy {
    Shape shape;

    ShapeProxy(ShapeType shapeType) {
        this.shape = switch (shapeType) {
            case SQUARE -> new Square();
            case CIRCLE -> new Circle();
        };

    }

    public void draw() {
        Thread t = new Thread(() -> shape.draw());
        t.start();
    }
}


public class Proxy {
    public static void main(String[] args) {
        // the main class (or any other method) will not invoke the shape directly
        ShapeProxy sp = new ShapeProxy(ShapeType.CIRCLE);
        sp.draw();
        System.out.println("drawing method was called, I do not need to wait for it to finish");
    }
}

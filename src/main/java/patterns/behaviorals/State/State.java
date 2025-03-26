package patterns.behaviorals.State;

/**
 The State pattern is used to implement an object which can behave differently
 depending on the state it is in. Each Object state models also the transition to
 the next one
**/
abstract class Shape {
    private int size;
    private int rotation;

    Shape() {
        size = 10;
        rotation = 0;
    }

    int getSize() {
        return size;
    }

    void setSize(int size) {
        this.size = size;
    }

    int getRotation() {
        return rotation;
    }

    void setRotation(int rotation) {
        this.rotation = rotation;
    }

    void display() {
        System.out.println("The shape has size " + size + " and is rotated of " + rotation + " degrees");
    }
}

class Square extends Shape {

}

class ShapeState {
    private final Shape shape;
    private UIController currentController;

    private final UIController sizeController;
    private final UIController rotationController;
    private final UIController deselectedController;

    ShapeState(Shape shape) {
        this.shape = shape;

        this.sizeController = new SizeController(this);
        this.rotationController = new RotationController(this);
        this.deselectedController = new DeselectedController(this);

        // initial state
        this.currentController = deselectedController;
    }

    Shape getShape() {
        return shape;
    }

    public void setCurrentController(UIController currentController) {
        this.currentController = currentController;
    }

    public void dragLeft() {
        this.currentController.dragLeft();
    }

    public void dragRight() {
        this.currentController.dragRight();
    }

    public void click() {
        this.currentController.click();
    }

    public void getDescription() {
        this.currentController.getDescription();
    }

    public UIController getRotationController() {
        return rotationController;
    }

    public UIController getDeselectedController() {
        return deselectedController;
    }

    public UIController getSizeController() {
        return sizeController;
    }
}

interface UIController {
    void click();
    void dragLeft();
    void dragRight();
    void getDescription();
}

class DeselectedController implements UIController {

    ShapeState shapeState;

    DeselectedController(ShapeState shapeState) {
        this.shapeState = shapeState;
    }

    @Override
    public void click() {
        shapeState.setCurrentController(shapeState.getSizeController());
    }

    @Override
    public void dragLeft() {
        System.out.println("ignoring");
    }

    @Override
    public void dragRight() {
        System.out.println("ignoring");
    }

    @Override
    public void getDescription() {
        System.out.println("Shape is deselected");
    }
}

class SizeController implements UIController {

    ShapeState shapeState;

    SizeController(ShapeState shapeState) {
        this.shapeState = shapeState;
    }

    @Override
    public void click() {
        shapeState.setCurrentController(shapeState.getRotationController());
    }

    @Override
    public void dragLeft() {
        int size = shapeState.getShape().getSize();
        shapeState.getShape().setSize(++size);
    }

    @Override
    public void dragRight() {
        int size = shapeState.getShape().getSize();
        shapeState.getShape().setSize(--size);
    }

    @Override
    public void getDescription() {
        System.out.println("Setting the size...");
    }
}

class RotationController implements UIController {

    ShapeState shapeState;

    RotationController(ShapeState shapeState) {
        this.shapeState = shapeState;
    }

    @Override
    public void click() {
        shapeState.setCurrentController(shapeState.getDeselectedController());
    }

    @Override
    public void dragLeft() {
        int rotation = shapeState.getShape().getRotation();
        shapeState.getShape().setRotation(++rotation);
    }

    @Override
    public void dragRight() {
        int rotation = shapeState.getShape().getRotation();
        shapeState.getShape().setRotation(--rotation);
    }

    @Override
    public void getDescription() {
        System.out.println("Setting the rotation...");
    }
}

public class State {

    public static void fiddleWithShape(ShapeState shapeState) {
        shapeState.getDescription();
        shapeState.dragLeft();
        shapeState.getShape().display();
        shapeState.dragRight();
        shapeState.getShape().display();
    }

    public static void main(String[] args) {
        Shape square = new Square();
        ShapeState shapeState = new ShapeState(square);

        // SET TO DESELECTED MODE
        fiddleWithShape(shapeState);

        // SET TO RESIZE MODE
        shapeState.click();
        fiddleWithShape(shapeState);

        // SET TO ROTATE MODE
        shapeState.click();
        fiddleWithShape(shapeState);

        // SET TO DESELECTED MODE
        shapeState.click();
        fiddleWithShape(shapeState);
    }
}

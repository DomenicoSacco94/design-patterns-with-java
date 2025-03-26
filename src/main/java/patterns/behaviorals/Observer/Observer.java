package patterns.behaviorals.Observer;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;

/**
  This pattern is used when a particular class (Subject) needs to be monitored by one or more classes (Observers).
  In this example the java interface EventListeners and class EventObject will be used.
**/
interface Shape {
}

class Square implements Shape {
    Square() {
        System.out.println("creating a square");
    }
}

class ShapeCreationEvent extends EventObject {

    private Shape shape;

    public ShapeCreationEvent(Object source) {
        super(source);
        this.shape = (Shape) source;
    }

    public Shape getShape() {
        return shape;
    }
}

interface ShapeListener extends EventListener {
    void reactToEvent(ShapeCreationEvent shapeCreationEvent);
}

class EditorUIListener implements ShapeListener {
    public void reactToEvent(ShapeCreationEvent shapeCreationEvent) {
        System.out.println("Drawing a shape: " + shapeCreationEvent.getShape());
    }
}

class SidebarListener implements ShapeListener {
    public void reactToEvent(ShapeCreationEvent shapeCreationEvent) {
        System.out.println("A shape was created: " + shapeCreationEvent.getShape());
    }
}

class EditorManager {

    List<Shape> shapes;
    List<ShapeListener> listeners;

    EditorManager() {
        listeners = new ArrayList<>();
        shapes = new ArrayList<>();
    }

    void addListener(ShapeListener shapeListener) {
        listeners.add(shapeListener);
    }

    void removeListener(ShapeListener shapeListener) {
        listeners.remove(shapeListener);
    }

    void addShape(Shape shape) {
        shapes.add(shape);
        ShapeCreationEvent shapeCreationEvent = new ShapeCreationEvent(shape);
        listeners.forEach(listener-> listener.reactToEvent(shapeCreationEvent));
    }

}


public class Observer {
    public static void main(String[] args) {
        EditorManager em = new EditorManager();
        EditorUIListener eui = new EditorUIListener();
        SidebarListener sl = new SidebarListener();
        em.addListener(eui);
        em.addListener(sl);
        Shape s = new Square();
        em.addShape(s);
    }
}

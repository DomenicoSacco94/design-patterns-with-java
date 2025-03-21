package patterns.structurals.Facade;

/*
    The Facade is a design pattern used to handle a complicated set of class and objects in a simple way.
    It operates by offering a class easy to use, having a small number of methods.
    The facade class methods will handle in the background many objects and their respective methods
 */

interface Shape {
    void draw();
    String getName();
}

class Square implements Shape {
    public void draw() {
        System.out.println("draw a square");
    }

    public String getName() {
        return "SQUARE";
    }
}

class Editor {
    void drawPerimeter(Shape s) {
        System.out.println("draw the shape perimeter for: " + s.getName());
    }

    void fillShape(Shape s) {
        System.out.println("filling the shape for: " + s.getName());
    }

    void displayShape(Shape s) {
        System.out.println("drawing the shape for: " + s.getName());
    }
}

class ShapeFacade {

    Editor editor;

    ShapeFacade(Editor editor) {
        this.editor = editor;
    }

    public void drawShape(Shape s) {
        editor.drawPerimeter(s);
        editor.fillShape(s);
        editor.displayShape(s);
    }

}

public class Facade {

    public static void main(String[] args) {
        Shape s = new Square();
        Editor e = new Editor();
        ShapeFacade sf = new ShapeFacade(e);
        // with only one method, we handle all the operations the editor needs to perform on the shape object
        sf.drawShape(s);
    }

}

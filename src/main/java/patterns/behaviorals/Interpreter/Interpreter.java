package patterns.behaviorals.Interpreter;

import java.util.Arrays;
import java.util.List;

/**
 This is not a standard design pattern but rather a solution to a recurrent problem: Translate into Java Objects a sentence in a certain language.
 In this example is about creating shapes having a certain color and displaying them.
**/
interface Shape {
    void display();
}

interface ShapeProperty {
}

enum ShapeColor implements ShapeProperty{
    BLUE,
    RED,
    GREEN
}

enum ShapeType implements ShapeProperty {
    SQUARE,
    CIRCLE
}

abstract class ColoredShape implements Shape {
    private final ShapeColor shapeColor;

    ColoredShape(ShapeColor shapeColor) {
        this.shapeColor = shapeColor;
    }

    ShapeColor getShapeColor() {
        return this.shapeColor;
    }
}

class Square extends ColoredShape {

    Square(ShapeColor color) {
        super(color);
    }

    @Override
    public void display() {
        System.out.println("This is a " + this.getShapeColor() + " square");
    }
}

class Circle extends ColoredShape {

    Circle(ShapeColor color) {
        super(color);
    }

    @Override
    public void display() {
        System.out.println("This is a " + this.getShapeColor() + " circle");
    }
}

class ShapeFactory {

    ShapeColor shapeColor;
    ShapeType shapeType;

    ShapeFactory(List<ShapeProperty> shapeProperties) {
        //we can add as many properties to the shape as we want !
        this.shapeColor = (ShapeColor) shapeProperties.stream().filter(property->property instanceof ShapeColor).toList().getFirst();
        this.shapeType = (ShapeType) shapeProperties.stream().filter(property->property instanceof ShapeType).toList().getFirst();
    }

    Shape create() {
        return switch(shapeType) {
            case ShapeType.CIRCLE -> new Circle(shapeColor);
            case ShapeType.SQUARE -> new Square(shapeColor);
        };
    }
}

interface ExpressionHandler {
    ShapeProperty handleExpression(String s);
    ShapeProperty callNextHandler(String s);
}

abstract class AbstractExpressionHandler implements ExpressionHandler {
    ExpressionHandler eh;

    void setNextExpressionHandler(ExpressionHandler eh) {
        this.eh = eh;
    }

    public abstract ShapeProperty handleExpression(String s);

    public ShapeProperty callNextHandler(String s) {
        return eh.handleExpression(s);
    }
}

class ShapeExpression extends AbstractExpressionHandler {

    @Override
    public ShapeProperty handleExpression(String s) {
        return switch (s) {
            case "square" -> ShapeType.SQUARE;
            case "circle" -> ShapeType.CIRCLE;
            default -> callNextHandler(s);
        };
    }
}

class ColorExpression extends AbstractExpressionHandler {

    @Override
    public ShapeProperty handleExpression(String s) {
        return switch (s) {
            case "blue" -> ShapeColor.BLUE;
            case "green" -> ShapeColor.GREEN;
            case "red" -> ShapeColor.RED;
            default -> callNextHandler(s);
        };
    }
}

class ColoredShapeExpression {

    public Shape evaluate(String s) {
        String[] exp = s.split(" ");
        if(exp.length!=2) {
            throw new IllegalArgumentException("Invalid expression!");
        } else {
            ShapeExpression shapeExpression = new ShapeExpression();
            ColorExpression colorExpression = new ColorExpression();
            shapeExpression.setNextExpressionHandler(colorExpression);

            ShapeFactory sf = new ShapeFactory(Arrays.stream(exp).map(shapeExpression::handleExpression).toList());
            return sf.create();
        }
    }
}

public class Interpreter {
    public static void main(String[] args) {
        ColoredShapeExpression cse = new ColoredShapeExpression();
        // we can introduce the shape properties in any order
        List<String> commands = List.of("blue square", "square blue", "red circle", "circle green");
        commands.forEach(command-> {
            Shape s = cse.evaluate(command);
            s.display();
        });
    }
}

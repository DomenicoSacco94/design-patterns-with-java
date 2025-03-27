package patterns.behaviorals.TemplateMethod;

/**
  This design pattern is used when we want to pre-define an algorithm and its steps in the mother class (the template)
  and then make changes to it in the child classes.
**/
abstract class Shape {

    protected void printShapeInfo() {
        double area = calculateArea();
        double perimeter = calculatePerimeter();
        System.out.println("The area of this shape is " + area + " and its perimeter is " + perimeter);
    }
    abstract double calculateArea();
    abstract double calculatePerimeter();
}

class Square extends Shape {

    double side;

    Square(double side) {
        this.side = side;
    }

    @Override
    double calculateArea() {
        return Math.pow(side, 2);
    }

    @Override
    double calculatePerimeter() {
        return side*4;
    }
}

class Circle extends Shape {

    double radius;

    Circle(double radius) {
        this.radius = radius;
    }

    @Override
    double calculateArea() {
        return Math.PI * Math.pow(radius, 2);
    }

    @Override
    double calculatePerimeter() {
        return 2 * Math.PI * radius;
    }
}

public class TemplateMethod {
    public static void main(String[] args) {
        Shape circle = new Circle(10);
        Shape square = new Square(10);

        circle.printShapeInfo();
        square.printShapeInfo();
    }
}

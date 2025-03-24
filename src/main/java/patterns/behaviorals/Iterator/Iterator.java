package patterns.behaviorals.Iterator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 An iterator is implemented in most of the programming languages, it helps to decouple the internal representation of an object from the function
 listing its content. In this example Square and Circle have different collection classes but implement the Java Iterable interface
**/
interface Shape {
    void display();
}

class Square implements Shape {

    int side;

    Square(int side) {
        this.side = side;
    }

    @Override
    public void display() {
        System.out.println("Displaying a square having a side of " + side);
    }
}

class SquareList implements Iterable<Square> {

    List<Square> squares;

    SquareList() {
        squares = new ArrayList<>();
        squares.add(new Square(1));
        squares.add(new Square(2));
        squares.add(new Square(3));
        squares.add(new Square(4));
    }

    @Override
    public @NotNull java.util.Iterator<Square> iterator() {
        return squares.listIterator();
    }
}


class Circle implements Shape {

    int radius;

    Circle(int radius) {
        this.radius = radius;
    }

    @Override
    public void display() {
        System.out.println("Displaying a circle having a radius of " + radius);
    }
}

class CircleList implements Iterable<Circle> {

    Circle[] circles;

    CircleList() {
        Circle c1 = new Circle(1);
        Circle c2 = new Circle(2);
        Circle c3 = new Circle(3);
        Circle c4 = new Circle(4);

        circles = new Circle[]{c1, c2, c3, c4};
    }

    @Override
    public @NotNull java.util.Iterator<Circle> iterator() {
        return Arrays.stream(circles).iterator();
    }
}

public class Iterator {

    public static void printElements(java.util.Iterator<? extends Shape> iterator) {
        while(iterator.hasNext()) {
            Shape s = iterator.next();
            s.display();
        }
    }

    public static void main(String [] args) {
        CircleList cl = new CircleList();
        printElements(cl.iterator());
        SquareList sl = new SquareList();
        printElements(sl.iterator());
    }
}

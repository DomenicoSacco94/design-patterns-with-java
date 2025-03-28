package patterns.architectures;

/**
 The Exagonal architecture can be seen as an evolution of the layered model (controller -> service -> repository).
 It inverts the dependency of the service layer (business) with the repository.
 In order to achieve this outcome, the architecture will use ports (inbound and outbound) and adapters.
 Inbound ports will be IMPLEMENTED by the application, outbound port are those the application DEPENDS on.
 **/
interface Shape {
    void draw();
}

class Square implements Shape {

    @Override
    public void draw() {
        System.out.println("Drawing the square " + this);
    }
}

//Outbound port, This could be for instance a JPA adapter for CRUD operations using Hibernate
interface ShapeDBAdapter {
    void storeShapeInDb(Shape shape);
}

class ShapeDBRepository implements ShapeDBAdapter {

    @Override
    public void storeShapeInDb(Shape s) {
        System.out.println("Storing the shape " + s + " in the DB");
    }
}

interface ShapeService {
    void processShape(Shape shape);
}

class ShapeServiceImpl implements ShapeService {

    private final ShapeDBAdapter shapeDBAdapter;

    ShapeServiceImpl(ShapeDBRepository shapeDBAdapter) {
        this.shapeDBAdapter = shapeDBAdapter;
    }

    public void processShape(Shape shape) {
        shape.draw();
        shapeDBAdapter.storeShapeInDb(shape);
    }
}

// Inbound port, this could be a Spring Boot REST controller
class ShapeController {

    // Spring scans the classes implementing ShapeService and will autowireShapeServiceImpl
    private final ShapeService shapeService;

    ShapeController(ShapeService shapeService) {
        this.shapeService = shapeService;
    }

    public void postShape(Shape shape) {
        System.out.println("Accepting the REST requesto to store shape " + shape);
        shapeService.processShape(shape);
    }
}

public class ExagonalArchitecture {
    public static void main(String[] args) {
        ShapeDBRepository shapeDBRepository = new ShapeDBRepository();
        ShapeServiceImpl shapeServiceImpl = new ShapeServiceImpl(shapeDBRepository);
        ShapeController shapeController = new ShapeController(shapeServiceImpl);
        Shape square = new Square();
        shapeController.postShape(square);
    }
}

package patterns.behaviorals;

/**
 This pattern is used for decoupling the client (command requester) with the server (command executor).
 The decoupling is performed creating a Command object which gets exchanges between the two.
**/
interface ShapeCommand {
    void execute();
}

class HeightEditor {
    static int x = 0;

    public static void moveUp() {
        x+=1;
        displayStatus();
    }

    public static void moveDown() {
        x-=1;
        displayStatus();
    }

    public static void displayStatus() {
        System.out.println("x axis is at: " + x);
    }
}

class MoveUpCommand implements ShapeCommand {

    @Override
    public void execute() {
        HeightEditor.moveUp();
    }
}

class MoveDownCommand implements ShapeCommand {

    @Override
    public void execute() {
        HeightEditor.moveDown();
    }
}

class LengthEditor {
    static int y = 0;

    public static void moveLeft() {
        y-=1;
        displayStatus();
    }

    public static void moveRight() {
        y+=1;
        displayStatus();
    }

    public static void displayStatus() {
        System.out.println("y axis is at: " + y);
    }
}


class MoveLeftCommand implements ShapeCommand {

    @Override
    public void execute() {
        LengthEditor.moveLeft();
    }
}

class MoveRightCommand implements ShapeCommand {

    @Override
    public void execute() {
        LengthEditor.moveRight();
    }
}


class ShapeButtonCouple {

    private final ShapeCommand plusButtonCommand;
    private final ShapeCommand minusButtonCommand;

    ShapeButtonCouple(ShapeCommand plusButtonCommand, ShapeCommand minusButtonCommand) {
        this.plusButtonCommand = plusButtonCommand;
        this.minusButtonCommand = minusButtonCommand;
    }

    void clickPlus() {
        plusButtonCommand.execute();
    }

    void clickMinus() {
        minusButtonCommand.execute();
    }

}


public class Command {

    public static void main(String[] args) {
        ShapeButtonCouple upDownMovement = new ShapeButtonCouple(new MoveUpCommand(), new MoveDownCommand());
        ShapeButtonCouple leftRightMovement = new ShapeButtonCouple(new MoveLeftCommand(), new MoveRightCommand());
        upDownMovement.clickPlus();
        leftRightMovement.clickPlus();
        leftRightMovement.clickPlus();
        upDownMovement.clickMinus();
        upDownMovement.clickMinus();
        leftRightMovement.clickMinus();
    }
}

package patterns.creationals.Singleton;

/*
    The singleton is a design pattern used when we want only one object at a time with a single
    point of access
 */

class SerialGeneratorSingleton {
    private int serialNum;

    // it must be static otherwise a static method cannot manipulate it
    private static SerialGeneratorSingleton serialGeneratorSingleton;

    //enforcing thread-safety
    public synchronized int getIncreasedSerialNumber() {
        return ++serialNum;
    }

    private SerialGeneratorSingleton() {};

    public static SerialGeneratorSingleton getInstance() {
        if(serialGeneratorSingleton==null) {
            serialGeneratorSingleton = new SerialGeneratorSingleton();
        }
        return serialGeneratorSingleton;
    }

}

// can manage multiple singletons at once
// enum by definitions cannot be instantiated and are immutable
enum EnumMultiton {
    VAR1,VAR2;

    // these variables and methods will be duplicated across each member of the enum
    private int serialNum;

    public synchronized int getNextSerial() {
        return ++serialNum;
    }
}

public class Singleton {
    public static void main(String[] args) {
       SerialGeneratorSingleton serialGeneratorSingleton = SerialGeneratorSingleton.getInstance();
       System.out.println(serialGeneratorSingleton.getIncreasedSerialNumber());

       // even if we have a different reference (somewhere else in the program) we still access the same instance
       SerialGeneratorSingleton serialGeneratorSingleton2 = SerialGeneratorSingleton.getInstance();
       System.out.println(serialGeneratorSingleton2.getIncreasedSerialNumber());

       // the multiton will have many singletons inside
       System.out.println("first call var 1: " + EnumMultiton.VAR1.getNextSerial());
       System.out.println("first call var 2: " + EnumMultiton.VAR2.getNextSerial());
       System.out.println("second call var 1: " + EnumMultiton.VAR1.getNextSerial());
       System.out.println("second call var 2: " + EnumMultiton.VAR2.getNextSerial());

    }
}
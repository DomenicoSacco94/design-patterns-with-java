package generation;

public class UMLRelationship {
    private final String from;
    private final String to;
    private final RelationshipType type;

    public UMLRelationship(String from, String to, RelationshipType type) {
        this.from = from;
        this.to = to;
        this.type = type;
    }

    @Override
    public String toString() {
        return switch (type) {
            case INHERITANCE -> from + " --|> " + to;
            case IMPLEMENTATION -> from + " ..|> " + to;
            case COMPOSITION -> from + " --> " + to;
            case TAKES_ARGUMENT -> from + " --> " + to + " : takes";
            case RETURN_TYPE -> from + " --> " + to + " : returns";
        };
    }

    public enum RelationshipType {
        INHERITANCE,
        IMPLEMENTATION,
        COMPOSITION,
        TAKES_ARGUMENT,
        RETURN_TYPE
    }
}
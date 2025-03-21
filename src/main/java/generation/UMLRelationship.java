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

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public RelationshipType getType() {
        return type;
    }

    @Override
    public String toString() {
        switch (type) {
            case INHERITANCE:
                return from + " --|> " + to;
            case IMPLEMENTATION:
                return from + " ..|> " + to;
            case COMPOSITION:
                return from + " --> " + to;
            case TAKES_ARGUMENT:
                return from + " --> " + to + " : takes";
            case RETURN_TYPE:
                return from + " --> " + to + " : returns";
            default:
                throw new IllegalArgumentException("Unknown relationship type: " + type);
        }
    }

    public enum RelationshipType {
        INHERITANCE,
        IMPLEMENTATION,
        COMPOSITION,
        TAKES_ARGUMENT,
        RETURN_TYPE
    }
}
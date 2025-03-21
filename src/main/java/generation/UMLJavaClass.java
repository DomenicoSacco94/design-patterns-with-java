package generation;

import com.thoughtworks.qdox.model.JavaClass;

public class UMLJavaClass {
    private final JavaClass javaClass;

    public UMLJavaClass(JavaClass javaClass) {
        this.javaClass = javaClass;
    }

    public String getClassDefinition() {
        StringBuilder classDef = new StringBuilder();
        if (javaClass.isInterface()) {
            classDef.append("interface ").append(javaClass.getName());
        } else {
            classDef.append("class ").append(javaClass.getName());
        }
        return classDef.toString();
    }

    public String getFields() {
        StringBuilder fields = new StringBuilder();
        for (var field : javaClass.getFields()) {
            fields.append("  ").append(field.getType().getFullyQualifiedName()).append(" ")
                  .append(field.getName()).append("\n");
        }
        return fields.toString();
    }

    public String getMethods() {
        StringBuilder methods = new StringBuilder();
        for (var method : javaClass.getMethods()) {
            methods.append("  ").append(method.getReturnType().getFullyQualifiedName()).append(" ")
                   .append(method.getName()).append("()\n");
        }
        return methods.toString();
    }

    public String getUMLDescription() {
        return getClassDefinition() +
                " {\n" +
                getFields() +
                getMethods() +
                "}\n";
    }
}
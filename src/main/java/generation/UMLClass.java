package generation;

import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.JavaProjectBuilder;

public class UMLClass {
    private final JavaClass javaClass;

    public UMLClass(JavaClass javaClass) {
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

    public String drawInheritanceRelationships() {
        if (javaClass.getSuperJavaClass() != null && !isObjectClass(javaClass.getSuperJavaClass())) {
            return javaClass.getName() + " --|> " + javaClass.getSuperJavaClass().getName() + "\n";
        }
        return "";
    }

    public String drawImplementationRelationship() {
        StringBuilder relationships = new StringBuilder();
        for (JavaClass interf : javaClass.getInterfaces()) {
            relationships.append(javaClass.getName()).append(" ..|> ").append(interf.getName()).append("\n");
        }
        return relationships.toString();
    }

    public String drawCompositionRelationships(JavaProjectBuilder builder) {
        StringBuilder references = new StringBuilder();
        for (JavaField field : javaClass.getFields()) {
            String fieldTypeName = field.getType().getFullyQualifiedName();
            JavaClass fieldType = builder.getClassByName(fieldTypeName);
            if (!isPrimitiveOrJavaUtilsClass(fieldType) && !fieldType.getName().equals(javaClass.getName())) {
                references.append(javaClass.getName()).append(" --> ").append(fieldType.getName()).append("\n");
            }
        }
        return references.toString();
    }

    public String getUMLContent(JavaProjectBuilder builder) {
        StringBuilder umlContent = new StringBuilder();
        umlContent.append(getUMLDescription());
        umlContent.append(drawInheritanceRelationships());
        umlContent.append(drawImplementationRelationship());
        umlContent.append(drawCompositionRelationships(builder));
        return umlContent.toString();
    }

    public boolean isTrivialClass() {
        return containsMainMethod(javaClass) || isObjectClass(javaClass);
    }

    private boolean isObjectClass(JavaClass javaClass) {
        return javaClass.getName().equals("Object");
    }

    private boolean isPrimitiveOrJavaUtilsClass(JavaClass javaClass) {
        return javaClass.isPrimitive() || javaClass.getFullyQualifiedName().startsWith("java.lang.") || javaClass.getFullyQualifiedName().startsWith("java.util.");
    }

    private boolean containsMainMethod(JavaClass javaClass) {
        for (var method : javaClass.getMethods()) {
            if (method.getName().equals("main") && method.isStatic() && method.getParameters().size() == 1) {
                return true;
            }
        }
        return false;
    }
}
package generation;

import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.JavaProjectBuilder;

import java.util.ArrayList;
import java.util.List;

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

    public String generateUMLDescription() {
        return getClassDefinition() +
                " {\n" +
                getFields() +
                getMethods() +
                "}\n";
    }

    public List<UMLRelationship> drawInheritanceRelationships() {
        List<UMLRelationship> UMLRelationships = new ArrayList<>();
        JavaClass superClass = javaClass.getSuperJavaClass();
        if (superClass != null && !isObjectClass(superClass) && !isPrimitiveOrJavaUtilsClass(superClass)) {
            UMLRelationships.add(new UMLRelationship(javaClass.getName(), superClass.getName(), UMLRelationship.RelationshipType.INHERITANCE));
        }
        return UMLRelationships;
    }

    public List<UMLRelationship> drawImplementationRelationship() {
        List<UMLRelationship> UMLRelationships = new ArrayList<>();
        for (JavaClass interf : javaClass.getInterfaces()) {
            UMLRelationships.add(new UMLRelationship(javaClass.getName(), interf.getName(), UMLRelationship.RelationshipType.IMPLEMENTATION));
        }
        return UMLRelationships;
    }

    public List<UMLRelationship> drawCompositionRelationships(JavaProjectBuilder builder) {
        List<UMLRelationship> UMLRelationships = new ArrayList<>();
        if (javaClass.isInterface()) {
            for (var method : javaClass.getMethods()) {
                JavaClass methodReturnType = method.getReturns();
                if (!isPrimitiveOrJavaUtilsClass(methodReturnType)) {
                    String cleanedName = cleanClassName(methodReturnType.getGenericCanonicalName());
                    UMLRelationships.add(new UMLRelationship(javaClass.getName(), cleanedName, UMLRelationship.RelationshipType.RETURN_TYPE));
                }
                for (var parameter : method.getParameters()) {
                    JavaClass parameterType = parameter.getJavaClass();
                    if (!isPrimitiveOrJavaUtilsClass(parameterType)) {
                        String cleanedName = cleanClassName(parameterType.getGenericCanonicalName());
                        UMLRelationships.add(new UMLRelationship(javaClass.getName(), cleanedName, UMLRelationship.RelationshipType.TAKES_ARGUMENT));
                    }
                }
            }
        }
        for (JavaField field : javaClass.getFields()) {
            String fieldTypeName = field.getType().getFullyQualifiedName();
            JavaClass fieldType = builder.getClassByName(fieldTypeName);
            if (!isPrimitiveOrJavaUtilsClass(fieldType)) {
                String cleanedFieldTypeName = cleanClassName(field.getType().getGenericCanonicalName());
                UMLRelationships.add(new UMLRelationship(javaClass.getName(), cleanedFieldTypeName, UMLRelationship.RelationshipType.COMPOSITION));
            }
        }
        return UMLRelationships;
    }

    public String generateUMLContent(JavaProjectBuilder builder) {
        StringBuilder umlContent = new StringBuilder(generateUMLDescription());
        List<UMLRelationship> UMLRelationships = new ArrayList<>();
        UMLRelationships.addAll(drawInheritanceRelationships());
        UMLRelationships.addAll(drawImplementationRelationship());
        UMLRelationships.addAll(drawCompositionRelationships(builder));
        for (UMLRelationship UMLRelationship : UMLRelationships) {
            umlContent.append(UMLRelationship).append("\n");
        }
        return umlContent.toString();
    }

    public boolean isTrivialClass() {
        return containsMainMethod(javaClass) || isObjectClass(javaClass);
    }

    private boolean isObjectClass(JavaClass javaClass) {
        return javaClass.getName().equals("Object");
    }

    private boolean isPrimitiveOrJavaUtilsClass(JavaClass javaClass) {
        return javaClass.isPrimitive() || javaClass.isEnum() || javaClass.getFullyQualifiedName().startsWith("java.lang.");
    }

    private boolean containsMainMethod(JavaClass javaClass) {
        for (var method : javaClass.getMethods()) {
            if (method.getName().equals("main") && method.isStatic() && method.getParameters().size() == 1) {
                return true;
            }
        }
        return false;
    }

    private String cleanClassName(String className) {
        // Remove array brackets if present
        className = className.replace("[]", "");
        // Capture and clean generic type information if present
        if (className.contains("<")) {
            int startIndex = className.indexOf("<");
            int endIndex = className.lastIndexOf(">");
            String genericPart = className.substring(startIndex + 1, endIndex);
            genericPart = cleanClassName(genericPart); // Recursively clean the generic part
            className = genericPart; // Use the cleaned generic part as the class name
        }
        // Split by dot and take the last part
        String[] parts = className.split("\\.");
        return parts[parts.length - 1];
    }
}
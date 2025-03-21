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
                    UMLRelationships.add(new UMLRelationship(javaClass.getName(), methodReturnType.getName(), UMLRelationship.RelationshipType.RETURN_TYPE));
                }
                for (var parameter : method.getParameters()) {
                    JavaClass parameterType = parameter.getJavaClass();
                    if (!isPrimitiveOrJavaUtilsClass(parameterType)) {
                        UMLRelationships.add(new UMLRelationship(javaClass.getName(), parameterType.getName(), UMLRelationship.RelationshipType.TAKES_ARGUMENT));
                    }
                }
            }
        }
        for (JavaField field : javaClass.getFields()) {
            String fieldTypeName = field.getType().getFullyQualifiedName();
            JavaClass fieldType = builder.getClassByName(fieldTypeName);
            if (!isPrimitiveOrJavaUtilsClass(fieldType)) {
                UMLRelationships.add(new UMLRelationship(javaClass.getName(), fieldType.getName(), UMLRelationship.RelationshipType.COMPOSITION));
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
        return javaClass.isPrimitive() || javaClass.isEnum() || javaClass.getFullyQualifiedName().startsWith("java.lang.") || javaClass.getFullyQualifiedName().startsWith("java.util.");
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
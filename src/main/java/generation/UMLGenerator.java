package generation;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaSource;
import com.thoughtworks.qdox.model.JavaMethod;
import net.sourceforge.plantuml.SourceStringReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public class UMLGenerator {

    public void generateUML(String sourceDirPath, String outputDirPath) throws IOException {
        JavaProjectBuilder builder = new JavaProjectBuilder();
        builder.addSourceTree(new File(sourceDirPath));

        Collection<JavaSource> sources = builder.getSources();

        for (JavaSource source : sources) {
            StringBuilder umlContent = new StringBuilder("@startuml\n");

            StringBuilder javadocContent = new StringBuilder();

            // Collect all Javadoc comments
            for (JavaClass javaClass : source.getClasses()) {
                javadocContent.append(getJavadocComment(javaClass.getComment()));

                for (JavaField field : javaClass.getFields()) {
                    javadocContent.append(getJavadocComment(field.getComment()));
                }
                for (JavaMethod method : javaClass.getMethods()) {
                    javadocContent.append(getJavadocComment(method.getComment()));
                }
            }

            // Append Javadoc comments at the beginning of the UML content
            if (!javadocContent.isEmpty()) {
                umlContent.append("note top of diagram\n")
                          .append(javadocContent.toString())
                          .append("end note\n");
            }

            // Generate UML content
            for (JavaClass javaClass : source.getClasses()) {
                if (containsMainMethod(javaClass) || isObjectClass(javaClass)) {
                    continue;
                }

                umlContent.append(getClassDefinition(javaClass));
                umlContent.append(" {\n");
                umlContent.append(getFields(javaClass));
                umlContent.append(getMethods(javaClass));
                umlContent.append("}\n");

                if (javaClass.getSuperJavaClass() != null && !isObjectClass(javaClass.getSuperJavaClass())) {
                    umlContent.append(getInheritanceRelationships(javaClass.getName(), javaClass.getSuperJavaClass().getName()));
                }

                for (JavaClass interf : javaClass.getInterfaces()) {
                    umlContent.append(getImplementationRelationship(javaClass.getName(), interf.getName()));
                }

                umlContent.append(getFieldReferences(javaClass, builder));
            }

            umlContent.append("@enduml");

            File outputDir = new File(outputDirPath);
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            String fileName = new File(source.getURL().getFile()).getName().replace(".java", "");
            File umlFile = new File(outputDir, fileName + ".puml");
            try (FileWriter writer = new FileWriter(umlFile)) {
                writer.write(umlContent.toString());
            }

            SourceStringReader reader = new SourceStringReader(umlContent.toString());
            reader.generateImage(new File(outputDir, fileName + ".png"));
        }
    }

    private String getJavadocComment(String comment) {
        if (comment != null && !comment.isEmpty()) {
            return comment.replace("\n", "\n") + "\n";
        }
        return "";
    }

    private String getClassDefinition(JavaClass javaClass) {
        StringBuilder classDef = new StringBuilder();
        if (javaClass.isInterface()) {
            classDef.append("interface ").append(javaClass.getName());
        } else {
            classDef.append("class ").append(javaClass.getName());
        }
        return classDef.toString();
    }

    private String getFields(JavaClass javaClass) {
        StringBuilder fields = new StringBuilder();
        for (JavaField field : javaClass.getFields()) {
            fields.append("  ").append(field.getType().getFullyQualifiedName()).append(" ")
                  .append(field.getName()).append("\n");
        }
        return fields.toString();
    }

    private String getMethods(JavaClass javaClass) {
        StringBuilder methods = new StringBuilder();
        for (JavaMethod method : javaClass.getMethods()) {
            methods.append("  ").append(method.getReturnType().getFullyQualifiedName()).append(" ")
                   .append(method.getName()).append("()\n");
        }
        return methods.toString();
    }

    private String getInheritanceRelationships(String className, String superClassName) {
        return className + " --|> " + superClassName + "\n";
    }

    private String getImplementationRelationship(String className, String interfaceName) {
        return className + " ..|> " + interfaceName + "\n";
    }

    private String getFieldReferences(JavaClass javaClass, JavaProjectBuilder builder) {
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

    private boolean containsMainMethod(JavaClass javaClass) {
        for (JavaMethod method : javaClass.getMethods()) {
            if (method.getName().equals("main") && method.isStatic() && method.getParameters().size() == 1) {
                return true;
            }
        }
        return false;
    }

    private boolean isObjectClass(JavaClass javaClass) {
        return javaClass.getName().equals("Object");
    }

    private boolean isPrimitiveOrJavaUtilsClass(JavaClass javaClass) {
        return javaClass.isPrimitive() || javaClass.getFullyQualifiedName().startsWith("java.lang.") || javaClass.getFullyQualifiedName().startsWith("java.util.");
    }

    public static void main(String[] args) {
        UMLGenerator generator = new UMLGenerator();
        try {
            generator.generateUML("src/main/java/patterns", "build/uml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
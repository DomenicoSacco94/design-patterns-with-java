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


// TODO ADD JAVADOC TO THE FINAL UML DIAGRAM
// TODO REFACTOR

public class UMLGenerator {

    public void generateUML(String sourceDirPath, String outputDirPath) throws IOException {
        JavaProjectBuilder builder = new JavaProjectBuilder();
        builder.addSourceTree(new File(sourceDirPath));

        Collection<JavaSource> sources = builder.getSources();

        for (JavaSource source : sources) {
            StringBuilder umlContent = new StringBuilder("@startuml\n");

            for (JavaClass javaClass : source.getClasses()) {
                // Skip classes with a main method and classes that extend Object
                if (containsMainMethod(javaClass) || isObjectClass(javaClass)) {
                    continue;
                }

                appendClassDefinition(umlContent, javaClass);
                umlContent.append(" {\n");
                appendFields(umlContent, javaClass);
                appendMethods(umlContent, javaClass);
                umlContent.append("}\n");

                if (javaClass.getSuperJavaClass() != null && !isObjectClass(javaClass.getSuperJavaClass())) {
                    addInheritance(umlContent, javaClass.getName(), javaClass.getSuperJavaClass().getName());
                }

                for (JavaClass interf : javaClass.getInterfaces()) {
                    addImplementation(umlContent, javaClass.getName(), interf.getName());
                }

                appendFieldReferences(umlContent, javaClass, builder);
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

    private void appendClassDefinition(StringBuilder umlContent, JavaClass javaClass) {
        if (javaClass.isInterface()) {
            umlContent.append("interface ").append(javaClass.getName());
        } else {
            umlContent.append("class ").append(javaClass.getName());
        }
    }

    private void appendFields(StringBuilder umlContent, JavaClass javaClass) {
        for (JavaField field : javaClass.getFields()) {
            umlContent.append("  ").append(field.getType().getFullyQualifiedName()).append(" ")
                    .append(field.getName()).append("\n");
        }
    }

    private void appendMethods(StringBuilder umlContent, JavaClass javaClass) {
        for (JavaMethod method : javaClass.getMethods()) {
            umlContent.append("  ").append(method.getReturnType().getFullyQualifiedName()).append(" ")
                    .append(method.getName()).append("()\n");
        }
    }

    private void appendFieldReferences(StringBuilder umlContent, JavaClass javaClass, JavaProjectBuilder builder) {
        for (JavaField field : javaClass.getFields()) {
            String fieldTypeName = field.getType().getFullyQualifiedName();
            JavaClass fieldType = builder.getClassByName(fieldTypeName);
            if (!isPrimitiveOrJavaUtilsClass(fieldType) && !fieldType.getName().equals(javaClass.getName())) {
                umlContent.append(javaClass.getName()).append(" --> ").append(fieldType.getName()).append("\n");
            }
        }
    }

    private void addInheritance(StringBuilder umlContent, String className, String superClassName) {
        umlContent.append(className).append(" --|> ").append(superClassName).append("\n");
    }

    private void addImplementation(StringBuilder umlContent, String className, String interfaceName) {
        umlContent.append(className).append(" ..|> ").append(interfaceName).append("\n");
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
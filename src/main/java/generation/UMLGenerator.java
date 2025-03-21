package generation;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
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

            for (JavaClass javaClass : source.getClasses()) {
                // Skip classes with a main method and classes that extend Object
                if (containsMainMethod(javaClass) || isObjectClass(javaClass)) {
                    continue;
                }

                if (javaClass.isInterface()) {
                    umlContent.append("interface ").append(javaClass.getName()).append(" {\n");
                } else {
                    umlContent.append("class ").append(javaClass.getName()).append(" {\n");
                }

                appendFields(umlContent, javaClass);
                appendMethods(umlContent, javaClass);
                umlContent.append("}\n");

                if (javaClass.getSuperJavaClass() != null && !isObjectClass(javaClass.getSuperJavaClass())) {
                    umlContent.append(javaClass.getName()).append(" --|> ")
                            .append(javaClass.getSuperJavaClass().getName()).append("\n");
                }

                javaClass.getInterfaces().forEach(interf ->
                        umlContent.append(javaClass.getName()).append(" ..|> ").append(interf.getName()).append("\n"));
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

    private void appendFields(StringBuilder umlContent, JavaClass javaClass) {
        javaClass.getFields().forEach(field ->
                umlContent.append("  ").append(field.getType().getFullyQualifiedName()).append(" ")
                        .append(field.getName()).append("\n"));
    }

    private void appendMethods(StringBuilder umlContent, JavaClass javaClass) {
        javaClass.getMethods().forEach(method ->
                umlContent.append("  ").append(method.getReturnType().getFullyQualifiedName()).append(" ")
                        .append(method.getName()).append("()\n"));
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

    public static void main(String[] args) {
        UMLGenerator generator = new UMLGenerator();
        try {
            generator.generateUML("src/main/java/patterns", "build/uml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
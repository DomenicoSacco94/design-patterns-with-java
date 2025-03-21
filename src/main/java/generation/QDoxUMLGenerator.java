package generation;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaSource;
import net.sourceforge.plantuml.SourceStringReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

//TODO USE VERSION CONTROL
//TODO REMOVE OBJECTS
//TODO REMOVE CLAS WITH THE MAIN METHOD
//TODO DO NOT USE FULL PATH

public class QDoxUMLGenerator {

    public void generateUML(String sourceDirPath, String outputDirPath) throws IOException {
        JavaProjectBuilder builder = new JavaProjectBuilder();
        builder.addSourceTree(new File(sourceDirPath));

        Collection<JavaSource> sources = builder.getSources();

        for (JavaSource source : sources) {
            StringBuilder umlContent = new StringBuilder("@startuml\n");

            for (JavaClass javaClass : source.getClasses()) {
                if (javaClass.getName().equals("java.lang.Object")) {
                    continue;
                }

                if (javaClass.isInterface()) {
                    umlContent.append("interface ").append(javaClass.getName()).append(" {\n");
                } else {
                    umlContent.append("class ").append(javaClass.getName()).append(" {\n");
                }

                javaClass.getFields().forEach(field -> umlContent.append("  ").append(field.getType().getFullyQualifiedName()).append(" ").append(field.getName()).append("\n"));
                javaClass.getMethods().forEach(method -> umlContent.append("  ").append(method.getReturnType().getFullyQualifiedName()).append(" ").append(method.getName()).append("()\n"));
                umlContent.append("}\n");

                if (javaClass.getSuperJavaClass() != null && !javaClass.getSuperJavaClass().getName().equals("java.lang.Object")) {
                    umlContent.append(javaClass.getName()).append(" --|> ").append(javaClass.getSuperJavaClass().getName()).append("\n");
                }

                javaClass.getInterfaces().forEach(interf -> umlContent.append(javaClass.getName()).append(" ..|> ").append(interf.getName()).append("\n"));
            }

            umlContent.append("@enduml");

            File outputDir = new File(outputDirPath);
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            String fileName = source.getURL().getFile().replace(sourceDirPath, "").replace(File.separator, "_").replace(".java", "");
            File umlFile = new File(outputDir, fileName + ".puml");
            try (FileWriter writer = new FileWriter(umlFile)) {
                writer.write(umlContent.toString());
            }

            SourceStringReader reader = new SourceStringReader(umlContent.toString());
            reader.generateImage(new File(outputDir, fileName + ".png"));
        }
    }

    public static void main(String[] args) {
        QDoxUMLGenerator generator = new QDoxUMLGenerator();
        try {
            generator.generateUML("src/main/java/patterns", "build/uml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
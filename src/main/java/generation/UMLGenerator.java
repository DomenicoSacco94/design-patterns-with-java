package generation;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaSource;
import net.sourceforge.plantuml.SourceStringReader;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class UMLGenerator {

    public void generateUML(String sourceDirPath, String outputDirPath) throws IOException {
        JavaProjectBuilder builder = new JavaProjectBuilder();
        builder.addSourceTree(new File(sourceDirPath));

        Collection<JavaSource> sources = builder.getSources();

        for (JavaSource source : sources) {
            UMLSource umlSource = new UMLSource(source);
            StringBuilder umlContent = new StringBuilder("@startuml\n");

            // Collect all Javadoc comments
            String javadocContent = umlSource.collectJavadocComments();

            // Append Javadoc comments at the beginning of the UML content
            if (!javadocContent.isEmpty()) {
                umlContent.append(String.format("note top of %s\n", source.getClasses().getFirst().getName()))
                        .append(javadocContent)
                        .append("end note\n");
            }

            // Generate UML content
            for (JavaClass javaClass : source.getClasses()) {

                UMLClass umlClass = new UMLClass(javaClass);

                if (umlClass.isTrivialClass()) {
                    continue;
                }

                umlContent.append(umlClass.getUMLContent(builder));
            }

            umlContent.append("@enduml");

            File outputDir = new File(outputDirPath);
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            String fileName = new File(source.getURL().getFile()).getName().replace(".java", "");
            SourceStringReader reader = new SourceStringReader(umlContent.toString());
            reader.generateImage(new File(outputDir, fileName + ".png"));
        }
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
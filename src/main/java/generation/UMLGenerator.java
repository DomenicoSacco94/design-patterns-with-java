package generation;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaSource;
import net.sourceforge.plantuml.SourceStringReader;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class UMLGenerator {

    private static final boolean GENERATE_JAVADOC = false;

    public void generateUML(String sourceDirPath, String outputDirPath) throws IOException {
        JavaProjectBuilder builder = new JavaProjectBuilder();
        builder.addSourceTree(new File(sourceDirPath));

        Collection<JavaSource> sources = builder.getSources();

        for (JavaSource source : sources) {

            StringBuilder umlContent = new StringBuilder("@startuml\n");

            if(GENERATE_JAVADOC) {
                // Collect all Javadoc comments
                UMLSource umlSource = new UMLSource(source);
                String javadocContent = umlSource.collectJavadocComments();

                // Append Javadoc comments at the beginning of the UML content
                if (!javadocContent.isEmpty()) {
                    umlContent.append(String.format("note top of %s\n", source.getClasses().getFirst().getName()))
                            .append(javadocContent)
                            .append("end note\n");
                }
            }

            source.getClasses().stream()
                    .map(UMLClass::new)
                    .filter(umlClass -> !umlClass.isTrivialClass())
                    .forEach(umlClass -> umlContent.append(umlClass.generateUMLContent(builder)));

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
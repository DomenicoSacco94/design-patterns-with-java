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
            StringBuilder umlContent = new StringBuilder("@startuml\n");

            // Collect all Javadoc comments
            String javadocContent = collectJavadocComments(source);

            // Append Javadoc comments at the beginning of the UML content
            if (!javadocContent.isEmpty()) {
                umlContent.append(String.format("note top of %s\n", source.getClasses().getFirst().getName()))
                        .append(javadocContent)
                        .append("end note\n");
            }

            // Generate UML content
            for (JavaClass javaClass : source.getClasses()) {

                UMLJavaClass umlJavaClass = new UMLJavaClass(javaClass);

                if (umlJavaClass.isTrivialClass()) {
                    continue;
                }

                umlContent.append(umlJavaClass.getUMLDescription());
                umlContent.append(umlJavaClass.drawInheritanceRelationships());
                umlContent.append(umlJavaClass.drawImplementationRelationship());
                umlContent.append(umlJavaClass.drawCompositionRelationships(builder));
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

    private String collectJavadocComments(JavaSource source) {
        StringBuilder javadocContent = new StringBuilder();

        for (JavaClass javaClass : source.getClasses()) {
            javadocContent.append(getJavadocComment(javaClass.getComment()));

            for (var field : javaClass.getFields()) {
                javadocContent.append(getJavadocComment(field.getComment()));
            }
            for (var method : javaClass.getMethods()) {
                javadocContent.append(getJavadocComment(method.getComment()));
            }
        }

        return javadocContent.toString();
    }

    private String getJavadocComment(String comment) {
        if (comment != null && !comment.isEmpty()) {
            return comment.replace("\n", "\n") + "\n";
        }
        return "";
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
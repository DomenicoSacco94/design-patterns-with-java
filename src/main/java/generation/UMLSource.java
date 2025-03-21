package generation;

import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaSource;

public class UMLSource {
    private final JavaSource source;

    public UMLSource(JavaSource source) {
        this.source = source;
    }

    public String collectJavadocComments() {
        StringBuilder javadocContent = new StringBuilder();

        for (JavaClass javaClass : source.getClasses()) {
            javadocContent.append(getJavadocComment(javaClass.getComment()));
        }

        return javadocContent.toString();
    }

    private String getJavadocComment(String comment) {
        if (comment != null && !comment.isEmpty()) {
            return comment + "\n";
        }
        return "";
    }
}
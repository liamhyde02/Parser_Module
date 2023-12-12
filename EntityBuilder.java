package helloworld;

public interface EntityBuilder {
    JavaEntity build();
    void name(String s);
    void fullyQualifiedName(String s);
    void type(JavaEntityType type);
    void addDependency(String s);
    void addComposition(String s);
    void addRealization(String s);
    void parent(String s);
    void linesOfCode(int i);
    void addAssociation(String s);
}

package semantic;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import parsing.ast.Cardinality;
import parsing.ast.CompoundProperty;
import parsing.ast.Property;
import parsing.ast.PropertyList;

/**
 *
 * TODO What does this type/class do?
 */
public class PropertyTree {

    private PropertyTree parent;
    private String propertyName;
    private Cardinality cardinality;
    private Map<String, PropertyTree> children = new HashMap<>();

    public void addPropertyList(PropertyList list) {
        for (CompoundProperty cp : list) {
            addCompoundProperty(cp);
        }
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Collection<PropertyTree> getChildren() {
        return children.values();
    }

    private void addCompoundProperty(CompoundProperty cp) {
        PropertyTree head = this;
        for (Property p : cp) {
            PropertyTree child = head.children.get(p.getPropertyName());
            if (child == null) {
                child = new PropertyTree();
                child.propertyName = p.getPropertyName();
                child.parent = this;
                head.children.put(p.getPropertyName(), child);
            }
            head = child;
        }
        head.cardinality = cp.getCardinality();
    }

    public void print(OutputStream stream) throws IOException {
        for (PropertyTree child : children.values()) {
            print(stream, child);
        }
    }

    /**
     * @param propertyTree
     * @throws IOException
     */
    private static void print(OutputStream stream, PropertyTree propertyTree) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));
        print(writer, propertyTree, 0);
        writer.flush();
    }

    /**
     * @param propertyTree
     * @param i
     * @throws IOException
     */
    private static void print(BufferedWriter writer, PropertyTree propertyTree, int i) throws IOException {
        printSpaces(writer, i);

        String propertyName = propertyTree.getPropertyName();

        writer.write(propertyName);
        if (propertyTree.cardinality != null) {
            Cardinality cardinality = propertyTree.cardinality;
            writer.write("[" + cardinality.getLeft() + ", " + cardinality.getRight() + "]");
        }
        writer.write("\n");

        for (PropertyTree child : propertyTree.getChildren()) {
            print(writer, child, i + 2);
        }
    }

    /**
     * @param i
     * @throws IOException
     */
    private static void printSpaces(BufferedWriter writer, int num) throws IOException {
        for (int i = 0; i < num; i++) {
            writer.write(" ");
        }
    }
}

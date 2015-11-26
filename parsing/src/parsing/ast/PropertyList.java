package parsing.ast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * TODO What does this type/class do?
 */
public class PropertyList implements Iterable<CompoundProperty> {

    private List<CompoundProperty> properties = new ArrayList<>();

    public List<CompoundProperty> getProperties() {
        return properties;
    }

    /**
     * @param readProperty
     */
    public void add(CompoundProperty property) {
        properties.add(property);
    }

    @Override
    public Iterator<CompoundProperty> iterator() {
        return properties.iterator();
    }
}

package parsing.ast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * TODO What does this type/class do?
 */
public class CompoundProperty implements Iterable<Property> {

    private List<Property> properties;
    private Cardinality cardinality;

    public CompoundProperty(List<Property> properties) {
        super();
        this.properties = properties;
    }

    public CompoundProperty(CompoundProperty currCompound, CompoundProperty innerProperty) {
        properties = new ArrayList<>();
        properties.addAll(currCompound.properties);
        properties.addAll(innerProperty.properties);
        cardinality = innerProperty.cardinality;
    }

    public List<Property> getProperties() {
        return properties;
    }

    @Override
    public Iterator<Property> iterator() {
        return properties.iterator();
    }

    public Cardinality getCardinality() {
        return cardinality;
    }

    public void setCardinality(Cardinality cardinality) {
        this.cardinality = cardinality;
    }
}

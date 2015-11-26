package parsing.ast;

public class Property {

    private String propertyName;

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public String toString() {
        return "PropertyElement [propertyName=" + propertyName + "]";
    }

}

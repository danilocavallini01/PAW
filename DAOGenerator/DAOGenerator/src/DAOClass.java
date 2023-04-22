import java.util.ArrayList;
import java.util.List;

public class DAOClass {
    private List<Property> properties;
    private String name;
    
    public DAOClass(String name) {
        this.properties = new ArrayList<Property>();
        this.name = name;
    }

    public void addProperty(Property property) {
        this.properties.add(property);
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

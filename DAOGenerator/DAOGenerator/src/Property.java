public class Property {
    
    private String name;
    private PropertyType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PropertyType getType() {
        return type;
    }

    public void setType(PropertyType type) {
        this.type = type;
    }

    public Property(String name, PropertyType type) {
        this.name = name;
        this.type = type;
    }

    public String getJavaCode() {
        return this.type.type + " " + this.name + ";"; 
    }

    public String getDb2Code() {
        return " " + this.type.db2Sql + " ";
    }
}

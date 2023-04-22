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
        this.name = name.toLowerCase();
        this.type = type;
    }

    public static PropertyType getTypeOf(String type) {
        if ( type.equalsIgnoreCase("String") ) {
            return PropertyType.STRING;
        } else if ( type.equalsIgnoreCase("Int") ) {
            return PropertyType.INT;
        } else if ( type.equalsIgnoreCase("Float") ) {
            return PropertyType.FLOAT;
        } else if ( type.equalsIgnoreCase("Date") ) {
            return PropertyType.DATE;
        } else {
            return null;
        }
    }

    public String getJavaCode() {
        return this.type + " " + this.name + ";"; 
    }
}

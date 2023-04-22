public enum PropertyType {
    STRING("String"),
    INT("int"),
    FLOAT("Float"),
    DATE("Date"),
    BOOLEAN("boolean");
    
    private String type;

    private PropertyType(String type) {
        this.type = type;
    }

    public String toString() {
        return this.type;
    }
}

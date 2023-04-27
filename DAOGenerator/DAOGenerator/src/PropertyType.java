public enum PropertyType {
    STRING("String", "VARCHAR(50)"),
    INT("int", "INT"),
    FLOAT("Float", "DOUBLE"),
    DATE("Date","DATE"),
    BOOLEAN("boolean","SMALLINT");
    
    public String type;
    public String db2Sql;

    private PropertyType(String type, String db2Sql) {
        this.type = type;
        this.db2Sql = db2Sql;
    }

    public String toString() {
        return this.type + this.db2Sql;
    }
}

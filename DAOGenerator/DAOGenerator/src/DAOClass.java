import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DAOClass {
    private List<Property> properties;
    private String name;

    /**
     * -----------------------------
     * UTILITES
     * ------------------------------
     */

    private final String JAVA_EXT = ".java";

    private final String TAB = "\t";
    private final String NL = "\n";
    private final String NLNL = NL + NL;
    private final String SC = ";";
    private final String SCNL = SC + NL;

    private final String PACKAGE = "package it.unibo.paw.dao" + SCNL;

    private final String PUBLIC = "public ";
    private final String ABSTRACT = "abstract ";
    private final String BOOLEAN = "boolean ";
    private final String VOID = "void ";
    private final String INTERFACE_DEF = PUBLIC + "interface ";

    private final String INT_CODE = "int code";
    private final String METHOD_START = TAB + PUBLIC;
    private final String CREATE_TABLE = METHOD_START + BOOLEAN + "createTable();" + NL;
    private final String DROP_TABLE = METHOD_START + BOOLEAN + "dropTable();" + NL;

    private final String CURL_BRAK_OPEN = "{\n";
    private final String CURL_BRAK_CLOSE = "}\n";

    private final String SEPARATOR = TAB + "//-------------------------------------" + NL;

    public DAOClass(String name) {
        this.properties = new ArrayList<Property>();
        this.properties.add(new Property("id", PropertyType.INT));
        this.properties.add(new Property("loaded", PropertyType.BOOLEAN));

        this.name = capitalize(name);
    }

    public String capitalize(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
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

    /**
     * ----------------------------------------------------------------------------------
     * BUILD xxxDAO.JAVA FILE
     * ----------------------------------------------------------------------------------
     * 
     * @param outPutDir
     * @throws IOException
     */

    public void buildDaoInterface(String outPutDir) throws IOException {

        File f = new File(outPutDir + this.getDAOName() + JAVA_EXT);
        FileWriter fw = new FileWriter(f);

        // package it.unibo.paw.dao;
        fw.write(PACKAGE);
        fw.write(NL);

        // public interface StudentDAO {
        fw.write(INTERFACE_DEF + this.getDAOName() + " " + CURL_BRAK_OPEN);
        fw.write(NL);

        fw.write(SEPARATOR);
        fw.write(NL);

        // public void create(StudentDTO student);
        fw.write(METHOD_START + VOID + "create " + this.getDTOParameter());
        fw.write(NLNL);

        // public StudentDTO read(int code);
        fw.write(METHOD_START + this.getDTOName() + " read " + this.getParameter(INT_CODE));
        fw.write(NLNL);

        // public boolean update(StudentDTO student);
        fw.write(METHOD_START + BOOLEAN + "update " + this.getDTOParameter());
        fw.write(NLNL);

        // public boolean delete(StudentDTO student);
        fw.write(METHOD_START + BOOLEAN + "update " + this.getParameter(INT_CODE));
        fw.write(NLNL);

        fw.write(SEPARATOR);
        fw.write(NLNL);

        // public boolean createTable();
        // public boolean dropTable();
        fw.write(CREATE_TABLE + NL);
        fw.write(DROP_TABLE);

        fw.write(CURL_BRAK_CLOSE);

        fw.close();
    }

    public String getParameter(String parameterCode) {
        return "(" + parameterCode + ");";
    }

    public String getDTOParameter() {
        return this.getParameter(this.getDTOName() + " " + this.name);
    }

    public String getDTOName() {
        return this.name + "DTO";
    }

    public String getDAOName() {
        return this.name + "DAO";
    }

    /**
     * ----------------------------------------------------------------------------------
     * BUILD xxxDTO.JAVA FILE
     * ----------------------------------------------------------------------------------
     */

    private final String PRIVATE = "private ";
    private final String CLASS_DEF = PUBLIC + "class ";
    private final String STATIC = "static ";
    private final String FINAL = "final ";
    private final String THIS = "this.";

    private final String IMPORT_SERIALIZABLE = "import java.io.Serializable" + SCNL;
    private final String SERIALIZABLE = "implements Serializable " + CURL_BRAK_OPEN;
    private final String SERIAL_VERSION = TAB + PRIVATE + STATIC + FINAL + " long serialVersionUID = 1L" + SCNL;

    private final String END_CONTRUCTOR = TAB + CURL_BRAK_CLOSE;
    private final String ALREADY_LOADED_CONSTRUCT = TAB + TAB + THIS + "loaded = false" + SCNL;

    public void buildDTOClass(String outPutDir) throws IOException {
        File f = new File(outPutDir + this.getDTOName() + JAVA_EXT);
        FileWriter fw = new FileWriter(f);

        // package it.unibo.paw.dao;
        fw.write(PACKAGE);
        fw.write(NL);

        fw.write(IMPORT_SERIALIZABLE);
        fw.write(NL);

        // public class StudentDTO implements Serializable {
        fw.write(CLASS_DEF + this.getDTOName() + " " + SERIALIZABLE);
        fw.write(NLNL);

        fw.write(SERIAL_VERSION);
        fw.write(NL);

        fw.write(SEPARATOR);
        fw.write(NL);

        // PROPERTIES DEFINITION

        for (Property property : this.properties) {
            fw.write(TAB + PRIVATE + property.getJavaCode() + NL);
        }

        // CONTRUCTOR
        fw.write(NL);
        fw.write(SEPARATOR);
        fw.write(NLNL);

        fw.write(TAB + PUBLIC + this.getDTOName() + "() " + CURL_BRAK_OPEN);
        fw.write(ALREADY_LOADED_CONSTRUCT);
        fw.write(END_CONTRUCTOR);

        fw.write(CURL_BRAK_CLOSE);

        fw.close();
    }

    /**
     * Concrete DAO Factory
     */

    public String getterParameter(String parameter, boolean capitalize, boolean isBool) {
        return (isBool ? "is" : "get") + (capitalize ? capitalize(parameter) : parameter) + "()";
    }

    public String getter(String parameter) {
        return this.getterParameter(parameter, false, false);
    }

    public String getDAOConcreteFactoryCode() {
        return PUBLIC + ABSTRACT + this.getDAOName() + " " + this.getter(this.getDAOName()) + SCNL;
    }

    public String getXXXDAOConcreteFactoryCode() {
        return PUBLIC + this.getDAOName() + " " + this.getter(this.getDAOName()) + CURL_BRAK_OPEN;
    }

    /**
     * ----------------------------------------------------------------------------------
     * BUILD DB2xxxDAO.JAVA FILE
     * ----------------------------------------------------------------------------------
     */

    private final String STRING = "String ";
    private final String INSERT = "insert ";
    private final String U_INSERT = INSERT.toUpperCase();
    private final String INTO = "INTO ";
    private final String TABLE = "TABLE ";
    private final String WHERE = "WHERE ";

    private final String ID = "ID ";
    private final String CM = ", ";
    private final String QM = "? ";
    private final String EQ = "= ";
    private final String EQQM = " = ? ";
    private final String PLUS = "+ ";
    private final String QMCM = "?, ";

    private final String BRAK_OPEN = "( ";
    private final String BRAK_CLOSE = ") ";
    private final String VALUES = "VALUES ";

    private final String IMPLEMENTS = "implements ";
    private final String STATIC_FINAL_STR = TAB + STATIC + FINAL + STRING;

    public void buildDb2DAO(String outPutDir) throws IOException {
        File f = new File(outPutDir + "Db2" + getDTOName() + JAVA_EXT);
        FileWriter fw = new FileWriter(f);

        // IMPORT SECTION
        DAOGenerator.importFile(DAOGenerator.TEMPLATE_DIR + "Db2DAOClass_1", fw);

        // public class Db2CourseDAO implements CourseDAO {
        fw.write(NL + db2ClassDefinition());

        fw.write(SEPARATOR);
        fw.write(NL);

        // static final String TABLE = "courses";
        fw.write(tableDef() + NL);

        fw.write(SEPARATOR);
        fw.write(NL);

        // PROPERTY DEFINITIONS
        // static final String ID = "id";

        fw.write(propertiesDef());

        // static final String insert = "INSERT " + ...
        fw.write(insertDef(fw));

        // SELECT, DELETE AND PART OF UPDATE SECTIONS
        DAOGenerator.importFile(DAOGenerator.TEMPLATE_DIR + "Db2DAOClass_3", fw);

        fw.write(updateDef());
        fw.write(SEPARATOR);
        fw.write(NL);

        // @TODO: DA CREATE TABLE IN POI

        fw.close();
    }




    public String quote(String input) {
        return "\"" + input + "\" ";
    }

    public String tab(int num) {
        String result = "";

        for (int i = 0; i < num; i++) {
            result += TAB;
        }

        return result;
    }

    private String valuesDef() {
        String result = VALUES;
        result += BRAK_OPEN;

        int count = this.properties.size() - 1;

        while (count-- > 0) {
            if (count == 0) {
                result += QM.trim();
            } else {
                result += QMCM.trim();
            }
        }

        result += BRAK_CLOSE;
        return quote(result);
    }

    public String db2ClassDefinition() {
        return CLASS_DEF + "Db2" + this.getDAOName() + " " + IMPLEMENTS + " " + this.getDAOName() + " "
                + CURL_BRAK_OPEN + NL;
    }

    public String tableDef() {
        return STATIC_FINAL_STR + TABLE + EQ + quote(this.name) + SCNL;
    }

    public String propertyDef(Property prop) {

        return STATIC_FINAL_STR + prop.getName().toUpperCase() + " " + EQ + quote(prop.getName()) + SCNL;
    }

    public String propertiesDef() {
        String result = "";

        result += propertyDef(this.properties.get(0));
        for (Property prop : propertiesWithoutDefault()) {
            result += propertyDef(prop);
        }

        return result;
    }

    public String insertDef(FileWriter fw) throws IOException {

        DAOGenerator.importFile(DAOGenerator.TEMPLATE_DIR + "Db2DAOClass_2", fw);
        String result = ID + PLUS;

        for (Property prop : propertiesWithoutDefault()) {
            result += quote(CM) + PLUS + prop.getName().toUpperCase() + PLUS;
        }

        result += quote(BRAK_CLOSE) + PLUS + NL;
        result += tab(3) + valuesDef() + SCNL;

        return result;
    }

    public ArrayList<Property> propertiesWithoutDefault() {
        ArrayList<Property> filtered = new ArrayList<Property>(this.properties);

        filtered.removeIf(p -> (p.getName().equalsIgnoreCase("id") || p.getName().equalsIgnoreCase("loaded")));

        return filtered;
    }

    public String updateDef() throws IOException {
        String result = "";

        for (Property prop : propertiesWithoutDefault()) {
            result += prop.getName().toUpperCase() + " " + PLUS + quote(EQQM) + PLUS;
        }

        result += NL;

        // WHERE ID + "= ? " ;
        result += tab(3) + quote(WHERE) + PLUS + ID + PLUS + quote(EQQM) + SCNL + NL;

        return result;
    }
}

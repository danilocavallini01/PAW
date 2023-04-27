import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DAOClass extends Shortcut {
    private List<Property> properties;
    private String name;

    /**
     * -----------------------------
     * UTILITES
     * ------------------------------
     */

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
        fw.write(DAOCreate() + SC);
        fw.write(NLNL);

        // public StudentDTO read(int code);
        fw.write(DAORead() + SC);
        fw.write(NLNL);

        // public boolean update(StudentDTO student);
        fw.write(DAOUpdate() + SC);
        fw.write(NLNL);

        // public boolean delete(StudentDTO student);
        fw.write(DAOdelete() + SC);
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

    public String DAOCreate() {
        return METHOD_START + VOID + CREATE.toLowerCase() + this.getDTOParameter();
    }

    public String DAORead() {
        return METHOD_START + this.getDTOName() + " " + READ + this.getParameter(INT_CODE);
    }

    public String DAOUpdate() {
        return METHOD_START + BOOLEAN + UPDATE + this.getDTOParameter();
    }

    public String DAOdelete() {
        return METHOD_START + BOOLEAN + DELETE + this.getParameter(INT_CODE);
    }

    public String getDTOParameter() {
        return this.getParameter(this.getDTOName() + " " + this.name.toLowerCase());
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

    public String setterParameter(String parameter, boolean capitalize) {
        return "set" + (capitalize ? capitalize(parameter) : parameter) + "(";
    }

    public String setter(String parameter) {
        return this.setterParameter(parameter, true);
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

    public void buildDb2DAO(String outPutDir) throws IOException {
        File f = new File(outPutDir + "Db2" + getDAOName() + JAVA_EXT);
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

        // SELECT, DELETE AND PART OF UPDATE TABLE SECTIONS
        DAOGenerator.importFile(DAOGenerator.TEMPLATE_DIR + "Db2DAOClass_3", fw);

        fw.write(updateDef());
        fw.write(SEPARATOR);
        fw.write(NL);

        fw.write(createDef());
        fw.write(NL);

        // DROP TABLE SECTION
        DAOGenerator.importFile(DAOGenerator.TEMPLATE_DIR + "Db2DAOClass_4", fw);

        // INSERT SECTION
        fw.write(DAOCreateDef());
        DAOGenerator.importFile(DAOGenerator.TEMPLATE_DIR + "Db2DAOClass_5", fw);

        // READ SECTION
        fw.write(DAOReadDef());
        DAOGenerator.importFile(DAOGenerator.TEMPLATE_DIR + "Db2DAOClass_6", fw);

        // UPDATE SECTION
        fw.write(DAOUpdateDef());
        DAOGenerator.importFile(DAOGenerator.TEMPLATE_DIR + "Db2DAOClass_7", fw);

        DAOGenerator.importFile(DAOGenerator.TEMPLATE_DIR + "Db2DAOClass_8", fw);

        fw.close();
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
        return CLASS_DEF + "Db2" + this.getDAOName() + " " + IMPLEMENTS  + this.getDAOName() + " "
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

     /**
     * ----------------------------------------------------------------
     * STATIC INSERT
     * ----------------------------------------------------------------
     */

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

    /**
     * ----------------------------------------------------------------
     * STATIC UPDATE
     * ----------------------------------------------------------------
     */
    public String updateDef() {
        String result = "";

        for (Property prop : propertiesWithoutDefault()) {
            result += prop.getName().toUpperCase() + " " + PLUS + quote(EQQM) + PLUS;
        }

        result += NL;

        // WHERE ID + "= ? " ;
        result += tab(3) + quote(WHERE) + PLUS + ID + PLUS + quote(EQQM) + SCNL + NL;

        return result;
    }

    /**
     * ----------------------------------------------------------------
     * STATIC CREATE
     * ----------------------------------------------------------------
     */

    private String createDef() {
        String result = "";

        // static String create = "CREATE " +
        result += STATIC_FINAL_STR + CREATE.toLowerCase() + EQ + quote(CREATE) + PLUS + NL;

        // "TABLE " + TABLE + " ( " +
        result += tab(3) + quote(TABLE) + PLUS + TABLE + PLUS + quote(" " + BRAK_OPEN) + PLUS + NL;

        // PropertyDefinition
        // ID + " INT NOT NULL PRIMARY KEY, " +
        result += tab(3) + ID + PLUS + quote(" INT NOT NULL PRIMARY KEY, ") + PLUS + NL;

        // ES: FIRSTNAME + " VARCHAR(50), " +
        int size = propertiesWithoutDefault().size();
        for (Property prop : propertiesWithoutDefault()) {
            if (size-- > 0) {
                result += tab(3) + prop.getName().toUpperCase() + " " + PLUS + quote(prop.getDb2Code() + CM) + PLUS
                        + NL;
            } else {
                result += tab(3) + prop.getName().toUpperCase() + " " + PLUS + quote(prop.getDb2Code()) + PLUS + NL;
            }
        }

        // ") ";
        result += tab(3) + quote(BRAK_OPEN) + SCNL;
        return result;
    }

    /**
     * ----------------------------------------------------------------
     * CREATE/INSERT FUNCTION 
     * ----------------------------------------------------------------
     */


    public String DAOCreateDef() {
        String result = "";

        result += NL + DAOCreate() + CURL_BRAK_OPEN;

        result += tab(2) + IF + BRAK_OPEN + this.name.toLowerCase() + " " + EQEQ + NULL + BRAK_CLOSE + CURL_BRAK_OPEN;
        result += ErrorNullEntry("create", 3);
        result += tab(2) + CURL_BRAK_CLOSE + NL;
        result += Db2connectionOpen(2);

        result += try_stmt(2);
        result += Db2PreparedStatement("insert", 3);

        result += Db2PrepStmtSet(3);
        result += NLNL;

        result += prepStmtExecUpdate(3);
        result += prepStmtExecClose(3);

        return result;
    }

     /**
     * ----------------------------------------------------------------
     * READ FUNCTION 
     * ----------------------------------------------------------------
     */

    
     public String DAOReadDef() {
        String result = "";

        result += NL + DAORead() + CURL_BRAK_OPEN;

        result += tab(2) + getDTOName() + " result " + EQ + NULL + SCNL;


        result += tab(2) + IF + BRAK_OPEN + "id < 0" + BRAK_CLOSE + CURL_BRAK_OPEN;
        result += ErrorInvalidID("read", 3);
        result += tab(2) + CURL_BRAK_CLOSE + NL;


        result += Db2connectionOpen(2);

        result += try_stmt(2);
        result += Db2PreparedStatement("read_by_id", 3);

        result += tab(3) + PREP_STMT_NAME + ".setInt(1, id)" + SCNL;


        result += prepStmtExecQuery(3);
       
        result += readStmtIfExist(3);

        return result;
    }

   /**
     * ----------------------------------------------------------------
     * UPDATE FUNCTION 
     * ----------------------------------------------------------------
     */


    public String DAOUpdateDef() {
        String result = "";

        result += NL + DAOUpdate() + CURL_BRAK_OPEN;

        result += tab(2) + BOOLEAN + "result " + EQ + "false" + SCNL;
        result += tab(2) + IF + BRAK_OPEN + this.name.toLowerCase() + " " + EQEQ + NULL + BRAK_CLOSE + CURL_BRAK_OPEN;
        result += ErrorNullEntry("create", 3, true);
        result += tab(2) + CURL_BRAK_CLOSE + NL;
        result += Db2connectionOpen(2);

        result += try_stmt(2);
        result += Db2PreparedStatement("insert", 3);

        result += Db2PrepStmtSet(3);
        result += NLNL;

        result += prepStmtExecUpdate(3);

        result += tab(3) + "result " + EQ + "true" + SCNL;
        result += prepStmtExecClose(3);

        return result;
    }


    private String readStmtIfExist(int tabNum) {
        String result = "";

        result += tab(tabNum) + IF + BRAK_OPEN + RESULT_SET_NAME + ".next()" + BRAK_CLOSE + CURL_BRAK_OPEN;
        tabNum++;

        result += tab(tabNum) + getDTOName() + " entry " + EQ + "new " + getDTOName() + "()" + SCNL;
        result += getReadEntry(tabNum);
        result += tab(tabNum) + "result " + EQ + "entry" + SCNL;
        
        tabNum--;
        result += tab(tabNum) + CURL_BRAK_CLOSE;

        return result;
    }

    public String getReadEntry(int tabNum) {

        String result = "";

        result += readProperty(properties.get(0), tabNum);
        for ( Property prop : propertiesWithoutDefault() ) {
            result += readProperty(prop, tabNum);
        }

        return result;
    }

    public String readProperty(Property prop, int tabNum) {

        String resultSetProperty = "";
        String result = "";

        if ( !prop.getType().equals(PropertyType.DATE) ) {
            resultSetProperty = RESULT_SET_NAME + "." + getterParameter(prop.getType().type,true,false);
            resultSetProperty = resultSetProperty.substring(0, resultSetProperty.length() - 1);
            resultSetProperty += prop.getName().toUpperCase() + BRAK_CLOSE;
        } else {
            result += tab(tabNum) + "long secs = " + RESULT_SET_NAME + ".getDate(" + prop.getName().toUpperCase() + ").getTime()" + SCNL;
            resultSetProperty = "new java.util.Date(secs" + BRAK_CLOSE; 
        }

        resultSetProperty.trim();
       

        result +=  tab(tabNum) + "entry." + setter(prop.getName()) + resultSetProperty + BRAK_CLOSE + SCNL;
    
        return result;
    }


    public String Db2connectionOpen(int num) {

        return tab(num) + "Connection " + CONNECTION_NAME + EQ + DAOGenerator.DB2_DAO_FACTORY_NAME
                + ".createConnection()" + SCNL + NL;
    }

    public String Db2PreparedStatement(String operation, int tabNum) {
        return tab(tabNum) + "PreparedStatement " + PREP_STMT_NAME + EQ + CONNECTION_NAME + "." + "prepareStatement("
                + operation + ")" + SCNL
                +  tab(tabNum) + PREP_STMT_NAME + ".clearParameters()" + SCNL;
    }

    public String Db2PrepStmtSet(int tabNum) {
        String result = "";

        int propIndex = 1;

        result += prepStmtSetProperty(this.properties.get(0), tabNum, propIndex);
        for (Property prop : propertiesWithoutDefault()) {
            propIndex++;
            result += prepStmtSetProperty(prop, tabNum, propIndex);
        }

        return result;
    }

    public String prepStmtSetProperty(Property prop, int tabNum, int propIndex) {
        String result =  tab(tabNum) + PREP_STMT_NAME + "." + setter(prop.getType().type) + (propIndex++) + CM;
        if ( prop.getType().equals(PropertyType.DATE)) {
            result += "new java.sql.Date(";
        }
        result += this.name.toLowerCase() + "." + this.getterParameter(prop.getName(),true,false);
        
        if ( prop.getType().equals(PropertyType.DATE)) {
            result += ".getTime()" + BRAK_CLOSE;
        }

        result +=  BRAK_CLOSE + SCNL;
        return result;
    }

    public String prepStmtExecUpdate(int tabNum) {
        return tab(tabNum) + PREP_STMT_NAME + ".executeUpdate()" + SCNL;
    }

    public String prepStmtExecQuery(int tabNum) {
        return tab(tabNum) + "ResultSet " + RESULT_SET_NAME + EQ + PREP_STMT_NAME + ".executeQuery()" + SCNL;
    }

    public String prepStmtExecClose(int tabNum) {
        return tab(tabNum) + PREP_STMT_NAME + ".close()" + SCNL;
    }


}

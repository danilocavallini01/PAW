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
     * BUILD XXXDAO.JAVA FILE
     * ----------------------------------------------------------------------------------
     * 
     * @param outPutDir
     * @throws IOException
     */

    public void buildDaoInterface(String outPutDir) throws IOException {

        File f = new File(outPutDir + this.getDAOName() + JAVA_EXT);
        FileWriter fw = new FileWriter(f);


        //package it.unibo.paw.dao;
        fw.write(PACKAGE);
        fw.write(NL);

        //public interface StudentDAO {
        fw.write(INTERFACE_DEF + this.getDAOName() + " " + CURL_BRAK_OPEN);
        fw.write(NL);

        fw.write(SEPARATOR);
        fw.write(NL);

        //public void create(StudentDTO student);
        fw.write(METHOD_START + VOID + "create " + this.getDTOParameter());
        fw.write(NLNL);

        //public StudentDTO read(int code);
        fw.write(METHOD_START + this.getDTOName() + " read " + this.getParameter(INT_CODE));
        fw.write(NLNL);

        //public boolean update(StudentDTO student);
        fw.write(METHOD_START + BOOLEAN + "update " + this.getDTOParameter());
        fw.write(NLNL);

        //public boolean delete(StudentDTO student);
        fw.write(METHOD_START + BOOLEAN + "update " + this.getParameter(INT_CODE));
        fw.write(NLNL);

        fw.write(SEPARATOR);
        fw.write(NLNL);

        //public boolean createTable();
        //public boolean dropTable();
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
     *  ----------------------------------------------------------------------------------
     * BUILD XXXDTO.JAVA FILE
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

    public void buildDTOClass(String outPutDir) throws IOException{
        File f = new File(outPutDir + this.getDTOName() + JAVA_EXT);
        FileWriter fw = new FileWriter(f);

        //package it.unibo.paw.dao;
        fw.write(IMPORT_SERIALIZABLE);
        fw.write(NL);
        fw.write(PACKAGE);
        fw.write(NL);

        //public class StudentDTO implements Serializable {
        fw.write(CLASS_DEF + this.getDTOName() + " " + SERIALIZABLE);
        fw.write(NLNL);

        fw.write(SERIAL_VERSION);
        fw.write(NL);

        fw.write(SEPARATOR);
        fw.write(NL);


        // PROPERTIES DEFINITION

        for ( Property property : this.properties ) {
            fw.write(TAB + PRIVATE + property.getJavaCode() + NL);
        } 

        // CONTRUCTOR
        fw.write(NL);
        fw.write(SEPARATOR);
        fw.write(NLNL);
        
        fw.write( TAB + PUBLIC + this.getDTOName() + "() " + CURL_BRAK_OPEN);
        fw.write(ALREADY_LOADED_CONSTRUCT);
        fw.write(END_CONTRUCTOR);

        fw.write(CURL_BRAK_CLOSE);

        fw.close();
    }

    /**
     * Concrete DAO Factory
     */

    public String getterParameter(String parameter, boolean capitalize, boolean isBool) {
        return ( isBool ? "is" : "get" ) + (capitalize ? capitalize(parameter) : parameter) + "()";
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
}


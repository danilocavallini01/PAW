public abstract class Shortcut {

    final String JAVA_EXT = ".java";

    final String TAB = "\t";
    final String NL = "\n";
    final String NLNL = NL + NL;
    final String SC = ";";
    final String SCNL = SC + NL;

    final String PACKAGE = "package it.unibo.paw.dao" + SCNL;

    final String PUBLIC = "public ";
    final String ABSTRACT = "abstract ";
    final String BOOLEAN = "boolean ";
    final String VOID = "void ";
    final String INTERFACE_DEF = PUBLIC + "interface ";
    final String IF = "if ";
    final String PRIVATE = "private ";
    final String CLASS_DEF = PUBLIC + "class ";
    final String STATIC = "static ";
    final String FINAL = "final ";
    final String THIS = "this.";
    final String STRING = "String ";
    final String NULL = "null ";
    final String RETURN_STMT = "return " + SCNL;

    final String INT_CODE = "int id";
    final String METHOD_START = TAB + PUBLIC;
    final String CREATE_TABLE = METHOD_START + BOOLEAN + "createTable();" + NL;
    final String DROP_TABLE = METHOD_START + BOOLEAN + "dropTable();" + NL;
    final String IMPLEMENTS = "implements ";
    final String STATIC_FINAL_STR = TAB + STATIC + FINAL + STRING;

    final String CURL_BRAK_OPEN = "{\n";
    final String CURL_BRAK_CLOSE = "}\n";

    final String SEPARATOR = TAB + "//-------------------------------------" + NL;

  
    final String INSERT = "insert ";
    final String U_INSERT = INSERT.toUpperCase();
    final String INTO = "INTO ";
    final String TABLE = "TABLE ";
    final String WHERE = "WHERE ";
    final String CREATE = "CREATE ";
    final String UPDATE = "update ";
    final String READ = "read ";
    final String DELETE = "delete ";
    final String VALUES = "VALUES ";


    final String ID = "ID ";
    final String CM = ", ";
    final String QM = "? ";
    final String EQ = "= ";
    final String EQQM = " = ? ";
    final String PLUS = "+ ";
    final String QMCM = "?, ";
    final String EQEQ = "== ";
    final String BRAK_OPEN = "( ";
    final String BRAK_CLOSE = ") ";

    final String IMPORT_SERIALIZABLE = "import java.io.Serializable" + SCNL;
    final String SERIALIZABLE = "implements Serializable " + CURL_BRAK_OPEN;
    final String SERIAL_VERSION = TAB + PRIVATE + STATIC + FINAL + " long serialVersionUID = 1L" + SCNL;
    final String SYSTEM_OUT = "System.err.println" + BRAK_OPEN;

    final String END_CONTRUCTOR = TAB + CURL_BRAK_CLOSE;
    final String ALREADY_LOADED_CONSTRUCT = TAB + TAB + THIS + "loaded = false" + SCNL;

    final String CONNECTION_NAME = "conn";
    final String PREP_STMT_NAME = "prep_stmt";
    final String RESULT_SET_NAME = "rs";

  

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

    public String getParameter(String parameterCode) {
        return "(" + parameterCode + ")";
    }

    public String ErrorNullEntry(String operation, int tabNum) {
        return ErrorNullEntry(operation, tabNum, false);
    }

    public String ErrorNullEntry(String operation, int tabNum, boolean withResult) {

        String result = "";

        result += tab(tabNum) + SYSTEM_OUT + quote(operation + "(): failed to " + operation + " a null entry") + BRAK_CLOSE + SCNL;
        result += tab(tabNum) + (withResult ? "return result" + SCNL : RETURN_STMT);

        return result;
    }

    public String ErrorInvalidID(String operation, int tabNum) {

        String result = "";

        result += tab(tabNum) + SYSTEM_OUT + quote(operation + "(): cannot " + operation + " an entry with invalid id") + BRAK_CLOSE + SCNL;
        result += tab(tabNum) + "return result" + SCNL;

        return result;
    }


    public String try_stmt(int tabNum ) {
        return tab(tabNum) + "try " + CURL_BRAK_OPEN;
    }
}

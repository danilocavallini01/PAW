import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DAOGenerator {

	private List<DAOClass> classes;

	private static final String spr = File.separator;

	public static final String TEMPLATE_DIR = ".." + spr + "files" + spr;
	private final String JAVA_EXT = ".java";
	private final String OUTPUT_DIR = "." + spr + "it" + spr + "unibo" + spr + "paw" + spr + "dao" + spr;

	private final String DB2 = "Db2";
	private final String HSQLDB = "Hsqldb";
	private final String MYSQL = "MySql";

	private final String DB2_DIR = OUTPUT_DIR + "db2" + spr;
	private final String HSQLDB_DIR = OUTPUT_DIR + "hsqldb" + spr;
	private final String MYSQL_DIR = OUTPUT_DIR + "mysql" + spr;

	private static final int BUFFER_LENGTH = 1;

	/**
	 * ---------------------------------------
	 * FACTORIES
	 * ---------------------------------------
	 */
	
	private final String DAO_FACTORY_NAME = "DAOFactory";
	private final String DAO_FACTORY_TOTAL = OUTPUT_DIR + DAO_FACTORY_NAME + JAVA_EXT;

	private final String DB2_DAO_FACTORY_NAME = DB2 + DAO_FACTORY_NAME;
	private final String DB2_DAO_FACTORY_TOTAL = DB2_DIR + DB2_DAO_FACTORY_NAME + JAVA_EXT;

	private final String HSQLDB_DAO_FACTORY_NAME = HSQLDB +  DAO_FACTORY_NAME;
	private final String HSQLDB_DAO_FACTORY_TOTAL = HSQLDB_DIR + HSQLDB_DAO_FACTORY_NAME + JAVA_EXT;

	private final String MYSQL_DAO_FACTORY_NAME = MYSQL + DAO_FACTORY_NAME;
	private final String MYSQL_DAO_FACTORY_TOTAL = MYSQL_DIR + MYSQL_DAO_FACTORY_NAME + JAVA_EXT;

	// ---------------------------------------

	public static void main(String[] args) {
		try {

			DAOGenerator generator = new DAOGenerator();
			generator.createEmptyDaoFactories();

			Scanner sc = new Scanner(System.in);

			String className;
			String property;
			String type;
			PropertyType pType;

			DAOClass DAOClass;

			System.out.print("Inserisci nome della classe/tabella (q per finire): ");
			while (sc.hasNext()) {

				className = sc.nextLine();
				if (className.equalsIgnoreCase("q")) {
					break;
				}

				DAOClass = new DAOClass(className);

				System.out.print("\tNome proprietà (q per finire): ");

				while (sc.hasNext()) {

					property = sc.nextLine();

					if (property.equalsIgnoreCase("q")) {
						break;
					}

					System.out.print("\tTipo proprietà: ");
					type = sc.nextLine();

					try {
						pType = PropertyType.valueOf(type.toUpperCase());
					} catch (IllegalArgumentException e) {
						System.out.println("\tTipo di proprietà inesistente, riparto");
						System.out.print("\n\tNome proprietà (q per finire): ");
						continue;
					}

					DAOClass.addProperty(new Property(property, pType));

					System.out.print("\n\tNome proprietà (q per finire): ");
				}

				generator.addDAOClass(DAOClass);

				System.out.print("Inserisci nome della classe/tabella (q per finire): ");
			}

			sc.close();

			generator.generateDaoInterfaces();
			generator.generateDTOClasses();
			generator.updateDAOFactories();
			generator.generateDb2Classes();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public DAOGenerator() {
		this.classes = new ArrayList<DAOClass>();
	}

	public void addDAOClass(DAOClass dao) {
		this.classes.add(dao);
	}

	/**
	 * ------------------------------------------------------------
	 * BUILD xxxDAO.java and xxxDTO.java and DB2xxxDAO.java FILES
	 * ----------------------------------------------------------
	 */
	public void generateDaoInterfaces() throws IOException {

		for (DAOClass dao : this.classes) {
			dao.buildDaoInterface(OUTPUT_DIR);
		}
	}

	public void generateDTOClasses() throws IOException {

		for (DAOClass dao : this.classes) {
			dao.buildDTOClass(OUTPUT_DIR);
		}
	}

	public void generateDb2Classes() throws IOException {
		for (DAOClass dao : this.classes) {
			dao.buildDb2DAO(DB2_DIR);
		}
	}


	/**
	 * ------------------------------------------------------------
	 * UPDATE FACTORY FILES
	 * ------------------------------------------------------------
	 */
	private final String TAB = "\t";

	public void updateDAOFactory() throws IOException {

		File daoFactory = new File(DAO_FACTORY_TOTAL);
		FileWriter fw = new FileWriter(daoFactory, true);


		for (DAOClass dao : this.classes) {
			String factory = dao.getDAOConcreteFactoryCode();

			fw.write("\n");
			fw.write(TAB + "//Method to obtain a DATA ACCESS OBJECT for the datatype " + dao.getName() + "\n");
			fw.write(TAB +  factory + "\n");
			
		}

		fw.write("}\n");

		fw.close();
	}

	public void updateXXXDaoFactory(String factoryType) throws IOException{
		File daoFactory = new File(OUTPUT_DIR + factoryType.toLowerCase() + spr + factoryType + DAO_FACTORY_NAME + JAVA_EXT);
		FileWriter fw = new FileWriter(daoFactory, true);


		for (DAOClass dao : this.classes) { 
			String factory = dao.getXXXDAOConcreteFactoryCode();

			fw.write(TAB + factory + "\n");	
			fw.write(TAB + TAB + "return new " + factoryType + dao.getDAOName() + "();\n");
			fw.write(TAB + "}\n");		
		}

		fw.write("}\n");

		fw.close();
	}

	public void updateDAOFactories() throws IOException {

		this.updateDAOFactory();

		this.updateXXXDaoFactory(DB2);
		this.updateXXXDaoFactory(HSQLDB);
		this.updateXXXDaoFactory(MYSQL);
	}

	/**
	 * ------------------------------------------------------------
     * CREATE EMPTY FACTORY FILES
	 * ------------------------------------------------------------
	 */

	public void createEmptyDaoFactories() throws Exception {

		this.createOutDirs();

		this.createEmptyDAOFactory(DAO_FACTORY_TOTAL, DAO_FACTORY_NAME);
		this.createEmptyDAOFactory(DB2_DAO_FACTORY_TOTAL, DB2_DAO_FACTORY_NAME);
		this.createEmptyDAOFactory(HSQLDB_DAO_FACTORY_TOTAL, HSQLDB_DAO_FACTORY_NAME);
		this.createEmptyDAOFactory(MYSQL_DAO_FACTORY_TOTAL, MYSQL_DAO_FACTORY_NAME);

	}

	public void createEmptyDAOFactory(String factoryFileName, String templateFile) throws Exception {
		File f = new File(factoryFileName);
		FileWriter fw = new FileWriter(f);

		DAOGenerator.importFile(TEMPLATE_DIR + templateFile, fw);

		fw.close();
	}

	public void createOutDirs() throws Exception {

		File outputDir = new File(OUTPUT_DIR);
		if (!outputDir.exists()) {
			if (!outputDir.mkdirs()) {
				throw new Exception("outputDir not created", null);
			}
		}

		outputDir = new File(DB2_DIR);
		if (!outputDir.exists()) {
			if (!outputDir.mkdirs()) {
				throw new Exception("outputDir not created", null);
			}
		}

		outputDir = new File(HSQLDB_DIR);
		if (!outputDir.exists()) {
			if (!outputDir.mkdirs()) {
				throw new Exception("outputDir not created", null);
			}
		}

		outputDir = new File(MYSQL_DIR);
		if (!outputDir.exists()) {
			if (!outputDir.mkdirs()) {
				throw new Exception("outputDir not created", null);
			}
		}
	}

	
	/*
	 * ------------------------------------------------------------
     * UTILITIES
	 * ------------------------------------------------------------
	 */

	public static void importFile(String file, FileWriter fw) throws IOException {
		FileReader fr = new FileReader(file);
		char[] buffer = new char[BUFFER_LENGTH];

		while (fr.read(buffer, 0, BUFFER_LENGTH) != -1) {
			fw.write(buffer, 0, buffer.length);
		}

		fr.close();
	}
}

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DAOGenerator {

	final String spr = File.separator;

	final String TEMPLATE_DIR = ".." + spr + "files" + spr;
	final String JAVA_EXT = ".java";
	final String OUTPUT_DIR = "." + spr + "output" + spr;

	final String DB2_DIR = OUTPUT_DIR + "db2" + spr;
	final String HSQLDB_DIR = OUTPUT_DIR + "hsqldb" + spr;
	final String MYSQL_DIR = OUTPUT_DIR + "mysql" + spr;

	final int BUFFER_LENGTH = 1;

	/** ---------------------------------------
	 *    FACTORIES
	 *  ---------------------------------------
	 */
	final String DAO_FACTORY_NAME = "DAOFactory";
	final String DAO_FACTORY_TOTAL = OUTPUT_DIR  + DAO_FACTORY_NAME + JAVA_EXT;

	final String DB2_DAO_FACTORY_NAME = "Db2DAOFactory";
	final String DB2_DAO_FACTORY_TOTAL = DB2_DIR + DB2_DAO_FACTORY_NAME + JAVA_EXT;
	
	final String HSQLDB_DAO_FACTORY_NAME = "HsqldbDAOFactory";
	final String HSQLDB_DAO_FACTORY_TOTAL = HSQLDB_DIR + HSQLDB_DAO_FACTORY_NAME + JAVA_EXT;

	final String MYSQL_DAO_FACTORY_NAME = "MySqlDAOFactory";
	final String MYSQL_DAO_FACTORY_TOTAL = MYSQL_DIR + MYSQL_DAO_FACTORY_NAME + JAVA_EXT;

	private List<DAOClass> classes;

	/**
	 * ---------------------------------------
	 */

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
			while ( sc.hasNext() ) {

				className = sc.nextLine();
				if ( className.equalsIgnoreCase("q") ) {
					break;
				}

				DAOClass = new DAOClass(className);

				System.out.print("Nome proprietà (q per finire): ");

				while ( sc.hasNext() ) {

					property = sc.nextLine();

					if ( property.equalsIgnoreCase("q") ) {
						break;
					}

					System.out.print("Tipo proprietà: ");
					type = sc.nextLine();

					if ( ( pType = Property.getTypeOf(type) ) == null ){
						System.out.println("Tipo di proprietà inesistente, riparto");
						System.out.print("Nome proprietà (q per finire): ");
						continue;
					}

					DAOClass.addProperty(new Property(property, pType));

					System.out.println("Proprietà " + property + " aggiunta\n");
					System.out.print("Nome proprietà (q per finire): ");
				}

				generator.addDAOClass(DAOClass);

				System.out.print("Inserisci nome della classe/tabella (q per finire): ");
			}

			sc.close();

			

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

		FileReader fr = new FileReader(TEMPLATE_DIR + templateFile);
		char[] buffer = new char[BUFFER_LENGTH];

		while (fr.read(buffer, 0, BUFFER_LENGTH) != -1) {
			fw.write(buffer, 0, buffer.length);
		}

		fr.close();
		fw.close();
	}

	public void createOutDirs() throws Exception {
		
		File outputDir = new File(OUTPUT_DIR);
		if ( !outputDir.exists() ) {
			if (!outputDir.mkdir()) {
				throw new Exception("outputDir not created", null);
			}
		}

		outputDir = new File(DB2_DIR);
		if ( !outputDir.exists() ) {
			if (!outputDir.mkdir()) {
				throw new Exception("outputDir not created", null);
			}
		}

		outputDir = new File(HSQLDB_DIR);
		if ( !outputDir.exists() ) {
			if (!outputDir.mkdir()) {
				throw new Exception("outputDir not created", null);
			}
		}
		

		outputDir = new File(MYSQL_DIR);
		if ( !outputDir.exists() ) {
			if (!outputDir.mkdir()) {
				throw new Exception("outputDir not created", null);
			}
		}
	}

}

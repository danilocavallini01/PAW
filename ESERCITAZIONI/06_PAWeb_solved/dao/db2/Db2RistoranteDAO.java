package it.unibo.paw.dao.db2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.unibo.paw.dao.RistoranteDAO;
import it.unibo.paw.dao.RistoranteDTO;

public class Db2RistoranteDAO implements RistoranteDAO {

	private static final String TABLE = "ristoranti";

	private static final String ID = "id";
	private static final String NOMERISTORANTE = "nome";
	private static final String INDIRIZZO = "indirizzo";
	private static final String RATING = "rating";

	// == STATEMENT SQL ====================================================================

	// INSERT INTO table ( id, name, description, ...) VALUES ( ?,?, ... );
	private static final String insert = "INSERT " +
			"INTO " + TABLE + " ( " +
			ID + ", " + NOMERISTORANTE + ", " + INDIRIZZO + ", " + RATING + " " +
			") " +
			"VALUES (?,?,?,?) ";

	// SELECT * FROM table WHERE idcolumn = ?;
	private static final String read_by_id = "SELECT * " +
			"FROM " + TABLE + " " +
			"WHERE " + ID + " = ? ";

	// SELECT * FROM table WHERE idcolumn = ?;
	private static final String read_by_name = "SELECT * " +
			"FROM " + TABLE + " " +
			"WHERE " + NOMERISTORANTE + " = ? ";

	// SELECT * FROM table WHERE stringcolumn = ?;
	private static final String read_all = "SELECT * " +
			"FROM " + TABLE + " ";

	// SELECT * FROM table WHERE stringcolumn = ?;
	private static final String find_resturant_by_city = "SELECT * " +
			"FROM " + TABLE + " "
			+ "WHERE " + INDIRIZZO + " LIKE ?";

	private static final String find_resturant_over_rate = read_all +
			"WHERE " + RATING + " >= ? ";

	// DELETE FROM table WHERE idcolumn = ?;
	private static final String delete = "DELETE " +
			"FROM " + TABLE + " " +
			"WHERE " + ID + " = ? ";

	// UPDATE table SET xxxcolumn = ?, ... WHERE idcolumn = ?;
	private static final String update = "UPDATE " + TABLE + " " +
			"SET " +
			NOMERISTORANTE + " = ?, " +
			INDIRIZZO + " = ?, " +
			RATING + " = ? " +
			"WHERE " + ID + " = ? ";

	// -------------------------------------------------------------------------------------

	// CREATE entrytable ( code INT NOT NULL PRIMARY KEY, ... );
	private static final String create = "CREATE " +
			"TABLE " + TABLE + " ( " +
			ID + " INT NOT NULL PRIMARY KEY, " +
			NOMERISTORANTE + " VARCHAR(50) NOT NULL UNIQUE, " +
			INDIRIZZO + " VARCHAR(50), " +
			RATING + " INT " +
			") ";

	private static final String drop = "DROP " +
			"TABLE " + TABLE + " ";

	@Override
	public void create(RistoranteDTO ristorante) {
		// --- 1. Dichiarazione della variabile per il risultato ---
		//Long result = new Long(-1);
		// --- 2. Controlli preliminari sui dati in ingresso ---
		if (ristorante == null) {
			System.out.println("create(): failed to insert a null entry");
			return;
		}
		// --- 3. Apertura della connessione ---
		Connection conn = Db2DAOFactory.createConnection();
		// --- 4. Tentativo di accesso al db e impostazione del risultato ---
		try {
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement
			PreparedStatement prep_stmt = conn.prepareStatement(Db2RistoranteDAO.insert);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, ristorante.getId());
			prep_stmt.setString(2, ristorante.getNomeRistorante());
			prep_stmt.setString(3, ristorante.getIndirizzo());
			prep_stmt.setInt(4, ristorante.getRating());
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)
			prep_stmt.executeUpdate();
			// --- d. Cicla sul risultato (se presente) pe accedere ai valori di ogni sua tupla
			// n.d.
			// --- e. Rilascia la struttura dati del risultato
			// n.d.
			// --- f. Rilascia la struttura dati dello statement
			prep_stmt.close();
		}
		// --- 5. Gestione di eventuali eccezioni ---
		catch (Exception e) {
			System.out.println("create(): failed to insert entry: " + e.getMessage());
			e.printStackTrace();
			//result = new Long(-2);
		} finally {
			Db2DAOFactory.closeConnection(conn);
		}

	}

	@Override
	public RistoranteDTO read(int id) {
		// --- 1. Dichiarazione della variabile per il risultato ---
		RistoranteDTO result = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---
		if (id < 0) {
			System.out.println("read(): cannot read an entry with a negative id");
			return result;
		}
		// --- 3. Apertura della connessione ---
		Connection conn = Db2DAOFactory.createConnection();
		// --- 4. Tentativo di accesso al db e impostazione del risultato ---
		try {
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement
			PreparedStatement prep_stmt = conn.prepareStatement(read_by_id);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, id);
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)
			ResultSet rs = prep_stmt.executeQuery();
			// --- d. Cicla sul risultato (se presente) pe accedere ai valori di ogni sua tupla
			if (rs.next()) {
				RistoranteDTO entry = new Db2RistoranteDTOProxy();
				entry.setId(rs.getInt(ID));
				entry.setIndirizzo(rs.getString(INDIRIZZO));
				entry.setRating(rs.getInt(RATING));
				entry.setNomeRistorante(rs.getString(NOMERISTORANTE));
				result = entry;
			}
			// --- e. Rilascia la struttura dati del risultato
			rs.close();
			// --- f. Rilascia la struttura dati dello statement
			prep_stmt.close();
		}
		// --- 5. Gestione di eventuali eccezioni ---
		catch (Exception e) {
			System.out.println("read(): failed to read entry: " + e.getMessage());
			e.printStackTrace();
		}
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il controllo al chiamante
		finally {
			Db2DAOFactory.closeConnection(conn);
		}
		// --- 7. Restituzione del risultato (eventualmente di fallimento)
		return result;
	}

	@Override
	public boolean update(RistoranteDTO r) {
		// --- 1. Dichiarazione della variabile per il risultato ---
		boolean result = false;
		// --- 2. Controlli preliminari sui dati in ingresso ---
		if (r == null) {
			System.out.println("update(): failed to update a null entry");
			return result;
		}
		// --- 3. Apertura della connessione ---
		Connection conn = Db2DAOFactory.createConnection();
		// --- 4. Tentativo di accesso al db e impostazione del risultato ---
		try {
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement
			PreparedStatement prep_stmt = conn.prepareStatement(update);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, r.getId());
			prep_stmt.setString(2, r.getNomeRistorante());
			prep_stmt.setString(3, r.getIndirizzo());
			prep_stmt.setInt(4, r.getRating());
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)
			prep_stmt.executeUpdate();
			// --- d. Cicla sul risultato (se presente) pe accedere ai valori di ogni sua tupla
			// n.d. qui devo solo dire al chiamante che e' andato tutto liscio
			result = true;
			// --- e. Rilascia la struttura dati del risultato
			// n.d.
			// --- f. Rilascia la struttura dati dello statement
			prep_stmt.close();
		}
		// --- 5. Gestione di eventuali eccezioni ---
		catch (Exception e) {
			System.out.println("update(): failed to update entry: " + e.getMessage());
			e.printStackTrace();
		}
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il controllo al chiamante
		finally {
			Db2DAOFactory.closeConnection(conn);
		}
		// --- 7. Restituzione del risultato (eventualmente di fallimento)
		return result;
	}

	@Override
	public boolean delete(int id) {
		// --- 1. Dichiarazione della variabile per il risultato ---
		boolean result = false;
		// --- 2. Controlli preliminari sui dati in ingresso ---
		//if ( code == null || code < 0 )  {
		if (id < 0) {
			System.out.println("delete(): cannot delete an entry with a negative id");
			return result;
		}
		// --- 3. Apertura della connessione ---
		Connection conn = Db2DAOFactory.createConnection();
		// --- 4. Tentativo di accesso al db e impostazione del risultato ---
		try {
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement
			PreparedStatement prep_stmt = conn.prepareStatement(delete);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, id);
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)
			prep_stmt.executeUpdate();
			// --- d. Cicla sul risultato (se presente) pe accedere ai valori di ogni sua tupla
			// n.d. Qui devo solo dire al chiamante che e' andato tutto liscio
			result = true;
			// --- e. Rilascia la struttura dati del risultato
			// n.d.
			// --- f. Rilascia la struttura dati dello statement
			prep_stmt.close();
		}
		// --- 5. Gestione di eventuali eccezioni ---
		catch (Exception e) {
			System.out.println("delete(): failed to delete entry: " + e.getMessage());
			e.printStackTrace();
		}
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il controllo al chiamante
		finally {
			Db2DAOFactory.closeConnection(conn);
		}
		// --- 7. Restituzione del risultato (eventualmente di fallimento)
		return result;
	}

	@Override
	public boolean createTable() {
		// --- 1. Dichiarazione della variabile per il risultato ---
		boolean result = false;
		// --- 2. Controlli preliminari sui dati in ingresso ---
		// n.d.
		// --- 3. Apertura della connessione ---
		Connection conn = Db2DAOFactory.createConnection();
		// --- 4. Tentativo di accesso al db e impostazione del risultato ---
		try {
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement
			Statement stmt = conn.createStatement();
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			// n.d.
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)
			stmt.execute(create);
			// --- d. Cicla sul risultato (se presente) pe accedere ai valori di ogni sua tupla
			// n.d. Qui devo solo dire al chiamante che è andato tutto liscio
			result = true;
			// --- e. Rilascia la struttura dati del risultato
			// n.d.
			// --- f. Rilascia la struttura dati dello statement
			stmt.close();
		}
		// --- 5. Gestione di eventuali eccezioni ---
		catch (Exception e) {
			e.printStackTrace();
		}
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il controllo al chiamante
		finally {
			Db2DAOFactory.closeConnection(conn);
		}
		// --- 7. Restituzione del risultato (eventualmente di fallimento)
		return result;
	}

	@Override
	public boolean dropTable() {
		// --- 1. Dichiarazione della variabile per il risultato ---
		boolean result = false;
		// --- 2. Controlli preliminari sui dati in ingresso ---
		// n.d.
		// --- 3. Apertura della connessione ---
		Connection conn = Db2DAOFactory.createConnection();
		// --- 4. Tentativo di accesso al db e impostazione del risultato ---
		try {
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement
			Statement stmt = conn.createStatement();
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			// n.d.
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)
			stmt.execute(drop);
			// --- d. Cicla sul risultato (se presente) pe accedere ai valori di ogni sua tupla
			// n.d. Qui devo solo dire al chiamante che è andato tutto a posto.
			result = true;
			// --- e. Rilascia la struttura dati del risultato
			// n.d.
			// --- f. Rilascia la struttura dati dello statement
			stmt.close();
		}
		// --- 5. Gestione di eventuali eccezioni ---
		catch (Exception e) {
			System.out.println("dropTable(): failed to drop table '" + TABLE + "': " + e.getMessage());
		}
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il controllo al chiamante
		finally {
			Db2DAOFactory.closeConnection(conn);
		}
		// --- 7. Restituzione del risultato (eventualmente di fallimento)
		return result;
	}

	@Override
	public List<RistoranteDTO> getResturantByCity(String citta) {
		List<RistoranteDTO> result = null;
		if (citta == null || citta.isEmpty()) {
			System.out.println("getResturantByCity(): cannot read an entry with an invalid city");
			return result;
		}
		Connection conn = Db2DAOFactory.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(find_resturant_by_city);
			prep_stmt.setString(1, "%"+citta+"%");
			ResultSet rs = prep_stmt.executeQuery();

			result = new ArrayList<RistoranteDTO>();
			while (rs.next()) {
				RistoranteDTO entry = new Db2RistoranteDTOProxy();
				entry.setId(rs.getInt(ID));
				entry.setIndirizzo(rs.getString(INDIRIZZO));
				entry.setRating(rs.getInt(RATING));
				entry.setNomeRistorante(rs.getString(NOMERISTORANTE));
				result.add(entry);
			}
			rs.close();
			prep_stmt.close();
		} catch (Exception e) {
			System.out.println("getResturantByCity(): failed to read entry: " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactory.closeConnection(conn);
		}
		return result;
	}

	@Override
	public List<RistoranteDTO> getRatedResturant(int stars) {
		List<RistoranteDTO> result = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---
		if (stars < 1 || stars > 5) {
			System.out.println("getResturantByCity(): cannot read an entry with an invalid input parameter");
			return result;
		}
		// --- 3. Apertura della connessione ---
		Connection conn = Db2DAOFactory.createConnection();
		// --- 4. Tentativo di accesso al db e impostazione del risultato ---
		try {
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement
			PreparedStatement prep_stmt = conn.prepareStatement(find_resturant_over_rate);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, stars);
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)
			ResultSet rs = prep_stmt.executeQuery();
			result = new ArrayList<RistoranteDTO>();
			// --- d. Cicla sul risultato (se presente) pe accedere ai valori di ogni sua tupla
			while (rs.next()) {
				RistoranteDTO entry = new Db2RistoranteDTOProxy();
				entry.setId(rs.getInt(ID));
				entry.setIndirizzo(rs.getString(INDIRIZZO));
				entry.setRating(rs.getInt(RATING));
				entry.setNomeRistorante(rs.getString(NOMERISTORANTE));
				result.add(entry);
			}
			// --- e. Rilascia la struttura dati del risultato
			rs.close();
			// --- f. Rilascia la struttura dati dello statement
			prep_stmt.close();
		}
		// --- 5. Gestione di eventuali eccezioni ---
		catch (Exception e) {
			System.out.println("getRatedResturant(): failed to read entry: " + e.getMessage());
			e.printStackTrace();
		}
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il controllo al chiamante
		finally {
			Db2DAOFactory.closeConnection(conn);
		}
		return result; // --- 7. Restituzione del risultato (eventualmente di fallimento)
	}

}

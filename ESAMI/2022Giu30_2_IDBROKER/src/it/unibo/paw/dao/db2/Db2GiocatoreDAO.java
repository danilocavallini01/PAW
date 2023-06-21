package it.unibo.paw.dao.db2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import it.unibo.paw.dao.GiocatoreDAO;
import it.unibo.paw.dao.GiocatoreDTO;
import it.unibo.paw.dao.IdBroker;

public class Db2GiocatoreDAO implements GiocatoreDAO {

	// -------------------------------------

	static final String TABLE = "Giocatore";

	// -------------------------------------

	static final String ID = "id";
	static final String CODICEFISCALE = "codiceFiscale";
	static final String COGNOME = "cognome";
	static final String NOME = "nome";
	static final String ETA = "eta";

	// == STATEMENT SQL
	// ====================================================================

	// INSERT INTO table ( name,description, ...) VALUES ( ?,?, ... );
	static final String insert = "INSERT " + "INTO " + TABLE + " ( " + ID + ", " + CODICEFISCALE + ", " + COGNOME + ", "
			+ NOME + ", " + ETA + ") " + "VALUES (?,?,?,?,?) ";

	// SELECT * FROM table WHERE idcolumn = ?;
	static final String read_by_id = "SELECT * " + "FROM " + TABLE + " " + "WHERE " + ID + " = ? ";

	// DELETE FROM table WHERE idcolumn = ?;
	static final String delete = "DELETE " + "FROM " + TABLE + " " + "WHERE " + ID + " = ? ";

	static final String read_all = "SELECT * " + "FROM " + TABLE + " ";

	// UPDATE table SET xxxcolumn = ?, ... WHERE idcolumn = ?;
	static final String update = "UPDATE " + TABLE + " " + "SET " + CODICEFISCALE + " = ? " + COGNOME + " = ? " + NOME
			+ " = ? " + ETA + " = ? " + "WHERE " + ID + " = ? ";

	// -------------------------------------

	static final String create = "CREATE " + "TABLE " + TABLE + " ( " + ID + " INT NOT NULL PRIMARY KEY, "
			+ CODICEFISCALE + " VARCHAR(50) NOT NULL UNIQUE, " + COGNOME + " VARCHAR(50) , " + NOME + " VARCHAR(50) , "
			+ ETA + " INT" + ")";

	static final String drop = "DROP " + "TABLE " + TABLE + " ";

// ------------------- METODI DAO -----------------------
	public void create(GiocatoreDTO giocatore) {
		if (giocatore == null) {
			System.err.println("create(): failed to create a null entry");
			return;
		}

		Connection conn = Db2DAOFactory.createConnection();
		IdBroker idBroker = Db2DAOFactory.getIdBroker();

		try {
			PreparedStatement prep_stmt = conn.prepareStatement(insert);

			prep_stmt.clearParameters();

			int id = idBroker.newId();

			prep_stmt.setInt(1, id);
			prep_stmt.setString(2, giocatore.getCodiceFiscale());
			prep_stmt.setString(3, giocatore.getCognome());
			prep_stmt.setString(4, giocatore.getNome());
			prep_stmt.setInt(5, giocatore.getEta());

			prep_stmt.executeUpdate();
			giocatore.setId(id);

			prep_stmt.close();
		} catch (Exception e) {
			System.err.println("create(): failed to insert entry: " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactory.closeConnection(conn);
		}

	}

// -------------------------------------------------------------------------------------

	public GiocatoreDTO read(int id) {
		GiocatoreDTO result = null;
		if (id < 0) {
			System.err.println("read(): cannot read an entry with invalid id");
			return result;
		}

		Connection conn = Db2DAOFactory.createConnection();

		try {
			PreparedStatement prep_stmt = conn.prepareStatement(read_by_id);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, id);
			ResultSet rs = prep_stmt.executeQuery();
			if (rs.next()) {
				GiocatoreDTO entry = new GiocatoreDTO();
				entry.setId(rs.getInt(ID));
				entry.setCodiceFiscale(rs.getString(CODICEFISCALE));
				entry.setCognome(rs.getString(COGNOME));
				entry.setNome(rs.getString(NOME));
				entry.setEta(rs.getInt(ETA));

				Db2GiocatoreSquadraMappingDAO map = new Db2GiocatoreSquadraMappingDAO();
				entry.setSquadre(map.getSquadreByGiocatore(rs.getInt(ID)));
				result = entry;
			}

			rs.close();

			prep_stmt.close();
		} catch (Exception e) {
			System.err.println("read(): failed to retrieve entry with id = " + id + ": " + e.getMessage());
			e.printStackTrace();
		}

		finally {
			Db2DAOFactory.closeConnection(conn);
		}

		return result;
	}

	// -------------------------------------------------------------------------------------

	public boolean update(GiocatoreDTO giocatore) {
		boolean result = false;
		if (giocatore == null) {
			System.err.println("create(): failed to create a null entry");
			return result;
		}

		Connection conn = Db2DAOFactory.createConnection();

		try {
			PreparedStatement prep_stmt = conn.prepareStatement(insert);
			prep_stmt.clearParameters();

			prep_stmt.setString(1, giocatore.getCodiceFiscale());
			prep_stmt.setString(2, giocatore.getCognome());
			prep_stmt.setString(3, giocatore.getNome());
			prep_stmt.setInt(4, giocatore.getEta());
			prep_stmt.setInt(5, giocatore.getId());

			prep_stmt.executeUpdate();
			result = true;
			prep_stmt.close();
		} catch (Exception e) {
			System.err.println("insert(): failed to update entry: " + e.getMessage());
			e.printStackTrace();
		}
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il
		// controllo al chiamante
		finally {
			Db2DAOFactory.closeConnection(conn);
		}
		// --- 7. Restituzione del risultato (eventualmente di fallimento)
		return result;
	}

	// -------------------------------------------------------------------------------------

	/**
	 * D
	 */
	public boolean delete(int id) {
		// --- 1. Dichiarazione della variabile per il risultato ---
		boolean result = false;
		// --- 2. Controlli preliminari sui dati in ingresso ---
		if (id < 0) {
			System.err.println("delete(): cannot delete an entry with an invalid id ");
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
			// --- d. Cicla sul risultato (se presente) pe accedere ai valori di ogni sua
			// tupla
			// n.d. Qui devo solo dire al chiamante che e' andato tutto liscio
			result = true;
			// --- e. Rilascia la struttura dati del risultato
			// n.d.
			// --- f. Rilascia la struttura dati dello statement
			prep_stmt.close();
		}
		// --- 5. Gestione di eventuali eccezioni ---
		catch (Exception e) {
			System.err.println("delete(): failed to delete entry with id = " + id + ": " + e.getMessage());
			e.printStackTrace();
		}
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il
		// controllo al chiamante
		finally {
			Db2DAOFactory.closeConnection(conn);
		}
		// --- 7. Restituzione del risultato (eventualmente di fallimento)
		return result;
	}

	// -------------------------------------------------------------------------------------

	// - CUSTOM FUNCTIONS -

	// -------------------------------------------------------------------------------------

	/**
	 * Creazione della table
	 */
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
			// --- d. Cicla sul risultato (se presente) pe accedere ai valori di ogni sua
			// tupla
			// n.d. Qui devo solo dire al chiamante che Ãš andato tutto liscio
			result = true;
			// --- e. Rilascia la struttura dati del risultato
			// n.d.
			// --- f. Rilascia la struttura dati dello statement
			stmt.close();
		}
		// --- 5. Gestione di eventuali eccezioni ---
		catch (Exception e) {
			System.err.println("createTable(): failed to create table '" + TABLE + "': " + e.getMessage());
		}
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il
		// controllo al chiamante
		finally {
			Db2DAOFactory.closeConnection(conn);
		}
		// --- 7. Restituzione del risultato (eventualmente di fallimento)
		return result;
	}

	// -------------------------------------------------------------------------------------

	/**
	 * Rimozione della table
	 */
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
			// --- d. Cicla sul risultato (se presente) pe accedere ai valori di ogni sua
			// tupla
			// n.d. Qui devo solo dire al chiamante che Ãš andato tutto a posto.
			result = true;
			// --- e. Rilascia la struttura dati del risultato
			// n.d.
			// --- f. Rilascia la struttura dati dello statement
			stmt.close();
		}
		// --- 5. Gestione di eventuali eccezioni ---
		catch (Exception e) {
			System.err.println("dropTable(): failed to drop table '" + TABLE + "': " + e.getMessage());
		}
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il
		// controllo al chiamante
		finally {
			Db2DAOFactory.closeConnection(conn);
		}
		// --- 7. Restituzione del risultato (eventualmente di fallimento)
		return result;
	}

}
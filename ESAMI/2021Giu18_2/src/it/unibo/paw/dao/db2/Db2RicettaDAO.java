package it.unibo.paw.dao.db2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import it.unibo.paw.dao.RicettaDAO;
import it.unibo.paw.dao.RicettaDTO;

public class Db2RicettaDAO implements RicettaDAO {

	// -------------------------------------

	static final String TABLE = "Ricetta";

	// -------------------------------------

	static final String ID = "id";
	static final String NOME = "nome";
	static final String TEMPOPREPARAZIONE = "tempoPreparazione";
	static final String LIVELLODIFFICOLTA = "livelloDifficolta";
	static final String CALORIE = "calorie";

	// == STATEMENT SQL
	// ====================================================================

	// INSERT INTO table ( name,description, ...) VALUES ( ?,?, ... );
	static final String insert = "INSERT " + "INTO " + TABLE + " ( " + ID + ", " + NOME + ", " + TEMPOPREPARAZIONE
			+ ", " + LIVELLODIFFICOLTA + ", " + CALORIE + ") " + "VALUES ( ?,?,?,?,?) ";

	// SELECT * FROM table WHERE idcolumn = ?;
	static final String read_by_id = "SELECT * " + "FROM " + TABLE + " " + "WHERE " + ID + " = ? ";

	// DELETE FROM table WHERE idcolumn = ?;
	static final String delete = "DELETE " + "FROM " + TABLE + " " + "WHERE " + ID + " = ? ";

	static final String read_all = "SELECT * " + "FROM " + TABLE + " ";

	// UPDATE table SET xxxcolumn = ?, ... WHERE idcolumn = ?;
	static final String update = "UPDATE " + TABLE + " " + "SET " + NOME + " = ? " + TEMPOPREPARAZIONE + " = ? "
			+ LIVELLODIFFICOLTA + " = ? " + CALORIE + " = ? " + "WHERE " + ID + " = ? ";

	// -------------------------------------

	static final String create = "CREATE " + "TABLE " + TABLE  + " ( " 
			+ ID + " INT NOT NULL PRIMARY KEY, " 
			+ NOME + " VARCHAR(50) NOT NULL UNIQUE, " 
			+ TEMPOPREPARAZIONE + " INT NOT NULL, " 
			+ LIVELLODIFFICOLTA + " VARCHAR(50) NOT NULL, " 
			+ CALORIE + " INT NOT NULL ) ";

	static final String drop = "DROP " + "TABLE " + TABLE + " ";

// ------------------- METODI DAO -----------------------
	public void create(RicettaDTO ricetta) {
		if (ricetta == null) {
			System.err.println("create(): failed to create a null entry");
			return;
		}

		Connection conn = Db2DAOFactory.createConnection();

		try {
			PreparedStatement prep_stmt = conn.prepareStatement(insert);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, ricetta.getId());
			prep_stmt.setString(2, ricetta.getNome());
			prep_stmt.setInt(3, ricetta.getTempoPreparazione());
			prep_stmt.setString(4, ricetta.getLivelloDifficolta());
			prep_stmt.setInt(5, ricetta.getCalorie());

			prep_stmt.executeUpdate();
			prep_stmt.close();

			// --- 5. Gestione di eventuali eccezioni ---
		} catch (Exception e) {
			System.err.println("create(): failed to insert entry: " + e.getMessage());
			e.printStackTrace();
			// result = new Long(-2);
		} finally {
			Db2DAOFactory.closeConnection(conn);
		}

	}

// -------------------------------------------------------------------------------------

	public RicettaDTO read(int id) {
		RicettaDTO result = null;
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
				RicettaDTO entry = new RicettaDTO();
				entry.setId(rs.getInt(ID));
				entry.setNome(rs.getString(NOME));
				entry.setTempoPreparazione(rs.getInt(TEMPOPREPARAZIONE));
				entry.setLivelloDifficolta(rs.getString(LIVELLODIFFICOLTA));
				entry.setCalorie(rs.getInt(CALORIE));
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

	public boolean update(RicettaDTO ricetta) {
		boolean result = false;
		if (ricetta == null) {
			System.err.println("create(): failed to create a null entry");
			return result;
		}

		Connection conn = Db2DAOFactory.createConnection();

		try {
			PreparedStatement prep_stmt = conn.prepareStatement(insert);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, ricetta.getId());
			prep_stmt.setString(2, ricetta.getNome());
			prep_stmt.setInt(3, ricetta.getTempoPreparazione());
			prep_stmt.setString(4, ricetta.getLivelloDifficolta());
			prep_stmt.setInt(5, ricetta.getCalorie());

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
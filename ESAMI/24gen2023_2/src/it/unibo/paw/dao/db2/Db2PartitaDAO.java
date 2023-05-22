package it.unibo.paw.dao.db2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.unibo.paw.dao.PartitaDAO;
import it.unibo.paw.dao.PartitaDTO;
import it.unibo.paw.dao.StadioDTO;

public class Db2PartitaDAO implements PartitaDAO {

	// -------------------------------------

	static final String TABLE = "Partita";

	// -------------------------------------

	static final String ID = "id";
	static final String CODICEPARTITA = "CodicePartita";
	static final String CATEGORIA = "Categoria";
	static final String GIRONE = "Girone";
	static final String NOMESQUADRACASA = "NomeSquadraCasa";
	static final String NOMESQUADRAOSPITE = "NomeSquadraOspite";
	static final String DATA = "Data";
	static final String STADIO = "Stadio";

	// == STATEMENT SQL
	// ====================================================================

	// INSERT INTO table ( name,description, ...) VALUES ( ?,?, ... );
	static final String insert = "INSERT " + "INTO " + TABLE + " ( " + ID + ", " + CODICEPARTITA + ", " + CATEGORIA
			+ ", " + GIRONE + ", " + NOMESQUADRACASA + ", " + NOMESQUADRAOSPITE + ", " + DATA + "," + STADIO + ") "
			+ "VALUES ( ?,?,?,?,?,?,?,?) ";

	// SELECT * FROM table WHERE idcolumn = ?;
	static final String read_by_id = "SELECT * " + "FROM " + TABLE + " " + "WHERE " + ID + " = ? ";

	// DELETE FROM table WHERE idcolumn = ?;
	static final String delete = "DELETE " + "FROM " + TABLE + " " + "WHERE " + ID + " = ? ";

	static final String read_all = "SELECT * " + "FROM " + TABLE + " ";

	// UPDATE table SET xxxcolumn = ?, ... WHERE idcolumn = ?;
	static final String update = "UPDATE " + TABLE + " " + "SET " + CODICEPARTITA + " = ? " + CATEGORIA + " = ? "
			+ GIRONE + " = ? " + NOMESQUADRACASA + " = ? " + NOMESQUADRAOSPITE + " = ? " + DATA + " = ? " + "WHERE "
			+ ID + " = ? ";

	// -------------------------------------

	static final String create = "CREATE " + "TABLE " + TABLE + " ( " + ID + " INT NOT NULL PRIMARY KEY, "
			+ CODICEPARTITA + " INT , " + CATEGORIA + " VARCHAR(50) , " + GIRONE + " VARCHAR(50) , " + NOMESQUADRACASA
			+ " VARCHAR(50) , " + NOMESQUADRAOSPITE + " VARCHAR(50) , " + DATA + " DATE , " + STADIO + " INT NOT NULL,"
			+ "FOREIGN KEY (" + STADIO + ") REFERENCES Stadio(id)" + "( ";

	static final String drop = "DROP " + "TABLE " + TABLE + " ";

// ------------------- METODI DAO -----------------------
	public void create(PartitaDTO partita) {
		if (partita == null) {
			System.err.println("create(): failed to create a null entry");
			return;
		}

		Connection conn = Db2DAOFactory.createConnection();

		try {
			PreparedStatement prep_stmt = conn.prepareStatement(insert);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, partita.getId());
			prep_stmt.setInt(2, partita.getCodicePartita());
			prep_stmt.setString(3, partita.getCategoria());
			prep_stmt.setString(4, partita.getGirone());
			prep_stmt.setString(5, partita.getNomeSquadraCasa());
			prep_stmt.setString(6, partita.getNomeSquadraOspite());
			prep_stmt.setDate(7, new java.sql.Date(partita.getData().getTime()));

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

		// Nel caso della creazione di una nuova tupla eseguo un secondo accesso per
		// sapere che code le e' stato assegnato
		// Chiaramente e' inutile farlo se gia'  il primo accesso ha prodotto errori
		// Devo inoltre preoccuparmi di rimuovere la chiusura dalla connessione dal
		// blocco finally definito precedentemente in quanto riutilizzata
		// --- 6./7. Chiusura della connessione in caso di errori e restituizione
		// prematura di un risultato di fallimento
		/*
		 * if ( result == -2 ) { Db2DAOFactory.closeConnection(conn); return result; }
		 * 
		 * // --- 1. Dichiarazione della variabile per il risultato --- // riutilizziamo
		 * quella di prima
		 * 
		 * // --- 2. Controlli preliminari sui dati in ingresso --- // gia'  fatti
		 * 
		 * // --- 3. Apertura della connessione --- // ce n'e' una gia'  aperta, se
		 * siamo qui
		 * 
		 * // --- 4. Tentativo di accesso al db e impostazione del risultato --- try {
		 * Statement stmt = conn.createStatement(); ResultSet rs =
		 * stmt.executeQuery(Db2StudentDAO.lastInsert); if ( rs.next() ) { result =
		 * rs.getLong(1); } rs.close(); stmt.close(); } // --- 5. Gestione di eventuali
		 * eccezioni --- catch (Exception e) {
		 * logger.warning("create(): failed to retrieve id of last inserted entry: "+e.
		 * getMessage()); e.printStackTrace(); } // --- 6. Rilascio, SEMPRE E COMUNQUE,
		 * la connessione prima di restituire il controllo al chiamante finally {
		 * Db2DAOFactory.closeConnection(conn); } // --- 7. Restituzione del risultato
		 * (eventualmente di fallimento) return result;
		 */
	}

// -------------------------------------------------------------------------------------

	public PartitaDTO read(int id) {
		PartitaDTO result = null;
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
				PartitaDTO entry = new PartitaDTO();
				entry.setId(rs.getInt(ID));
				entry.setCodicePartita(rs.getInt(CODICEPARTITA));
				entry.setCategoria(rs.getString(CATEGORIA));
				entry.setGirone(rs.getString(GIRONE));
				entry.setNomeSquadraCasa(rs.getString(NOMESQUADRACASA));
				entry.setNomeSquadraOspite(rs.getString(NOMESQUADRAOSPITE));
				long secs = rs.getDate(DATA).getTime();

				entry.setData(new java.util.Date(secs));
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

	public List<PartitaDTO> findPartiteByStadio(int id) {
		List<PartitaDTO> result = null;
		if (id < 0) {
			System.err.println("read(): cannot read an entry with a negative id");
			return result;
		}
		Connection conn = Db2DAOFactory.createConnection();
		result = new ArrayList<PartitaDTO>();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement("SELCET * FROM " + TABLE + " WHERE " + STADIO + " = ?");
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, id);
			ResultSet rs = prep_stmt.executeQuery();

			while (rs.next()) {
				PartitaDTO entry = new PartitaDTO();
				entry.setId(rs.getInt(ID));
				entry.setCodicePartita(rs.getInt(CODICEPARTITA));
				entry.setCategoria(rs.getString(CATEGORIA));
				entry.setGirone(rs.getString(GIRONE));
				entry.setNomeSquadraCasa(rs.getString(NOMESQUADRACASA));
				entry.setNomeSquadraOspite(rs.getString(NOMESQUADRAOSPITE));
				long secs = rs.getDate(DATA).getTime();
				entry.setData(new java.util.Date(secs));

				result.add(entry);
			}
			rs.close();
			prep_stmt.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			Db2DAOFactory.closeConnection(conn);
		}
		return result;
	}

	// -------------------------------------------------------------------------------------

	public boolean update(PartitaDTO partita) {
		boolean result = false;
		if (partita == null) {
			System.err.println("create(): failed to create a null entry");
			return result;
		}

		Connection conn = Db2DAOFactory.createConnection();

		try {
			PreparedStatement prep_stmt = conn.prepareStatement(insert);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, partita.getId());
			prep_stmt.setInt(2, partita.getCodicePartita());
			prep_stmt.setString(3, partita.getCategoria());
			prep_stmt.setString(4, partita.getGirone());
			prep_stmt.setString(5, partita.getNomeSquadraCasa());
			prep_stmt.setString(6, partita.getNomeSquadraOspite());
			prep_stmt.setDate(7, new java.sql.Date(partita.getData().getTime()));

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
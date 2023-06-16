package it.unibo.paw.dao.db2;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unibo.paw.dao.PartitaDTO;
import it.unibo.paw.dao.StadioDAO;
import it.unibo.paw.dao.StadioDTO;

public class Db2StadioDAO implements StadioDAO {

	// -------------------------------------

	static final String TABLE = "Stadio";

	// -------------------------------------

	static final String ID = "id";
	static final String CODICE = "Codice";
	static final String NOME = "Nome";
	static final String CITTA = "Citta";

	// == STATEMENT SQL
	// ====================================================================

	// INSERT INTO table ( name,description, ...) VALUES ( ?,?, ... );
	static final String insert = "INSERT " + "INTO " + TABLE + " ( " + ID + ", " + CODICE + ", " + NOME + ", " + CITTA
			+ ") " + "VALUES ( ?,?,?,?) ";

	// SELECT * FROM table WHERE idcolumn = ?;
	static final String read_by_id = "SELECT * " + "FROM " + TABLE + " " + "WHERE " + ID + " = ? ";

	// DELETE FROM table WHERE idcolumn = ?;
	static final String delete = "DELETE " + "FROM " + TABLE + " " + "WHERE " + ID + " = ? ";

	static final String read_all = "SELECT * " + "FROM " + TABLE + " ";

	// UPDATE table SET xxxcolumn = ?, ... WHERE idcolumn = ?;
	static final String update = "UPDATE " + TABLE + " " + "SET " + CODICE + " = ? " + NOME + " = ? " + CITTA + " = ? "
			+ "WHERE " + ID + " = ? ";

	// -------------------------------------
	static final String total_games_grouped = "SELECT " + Db2PartitaDAO.CATEGORIA + ",count(*) FROM " + Db2PartitaDAO.TABLE + " WHERE " + Db2PartitaDAO.STADIO + " = ? "
			+ "GROUP BY " + Db2PartitaDAO.CATEGORIA;
	
	static final String get_partite_by_stadio = "SELECT * FROM " + Db2PartitaDAO.TABLE + " WHERE " + Db2PartitaDAO.STADIO + " = ? ";
	// -------------------------------------

	static final String create = "CREATE " + "TABLE " + TABLE + " ( " + ID + " INT NOT NULL PRIMARY KEY, " + CODICE
			+ " INT , " + NOME + " VARCHAR(50) , " + CITTA + " VARCHAR(50)" + ") ";

	static final String drop = "DROP " + "TABLE " + TABLE + " ";

// ------------------- METODI DAO -----------------------
	public void create(StadioDTO stadio) {
		if (stadio == null) {
			System.err.println("create(): failed to create a null entry");
			return;
		}

		Connection conn = Db2DAOFactory.createConnection();

		try {
			PreparedStatement prep_stmt = conn.prepareStatement(insert);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, stadio.getId());
			prep_stmt.setInt(2, stadio.getCodice());
			prep_stmt.setString(3, stadio.getNome());
			prep_stmt.setString(4, stadio.getCitta());

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

	public StadioDTO read(int id) {
		StadioDTO result = null;
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
				StadioDTO entry = new StadioDTO();
				entry.setId(rs.getInt(ID));
				entry.setCodice(rs.getInt(CODICE));
				entry.setNome(rs.getString(NOME));
				entry.setCitta(rs.getString(CITTA));

				
				result = entry;
			}
			
			prep_stmt = conn.prepareStatement(get_partite_by_stadio);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, id);
			
			rs = prep_stmt.executeQuery();

			List<PartitaDTO> listaPartite= new ArrayList<PartitaDTO>();
			while ( rs.next() ) {
				PartitaDTO entry = new PartitaDTO();
				entry.setId(rs.getInt(Db2PartitaDAO.ID));
				entry.setCodicePartita(rs.getInt(Db2PartitaDAO.CODICEPARTITA));
				entry.setCategoria(rs.getString(Db2PartitaDAO.CATEGORIA));
				entry.setGirone(rs.getString(Db2PartitaDAO.GIRONE));
				entry.setNomeSquadraCasa(rs.getString(Db2PartitaDAO.NOMESQUADRACASA));
				entry.setNomeSquadraOspite(rs.getString(Db2PartitaDAO.NOMESQUADRAOSPITE));
				long secs = rs.getDate(Db2PartitaDAO.DATA).getTime();
				entry.setData(new java.util.Date(secs));
				
				listaPartite.add(entry);
			}
			
			result.setPartite(listaPartite);
			
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

	public Map<String, Integer> totalGamesGroupedByCategory(int id) {
	
		Map<String, Integer> result = new HashMap<String, Integer>();
		
		if (id < 0) {
			System.err.println("read(): cannot read an entry with invalid id");
			return result;
		}

		Connection conn = Db2DAOFactory.createConnection();

		try {
			PreparedStatement prep_stmt = conn.prepareStatement(total_games_grouped);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, id);
			ResultSet rs = prep_stmt.executeQuery();
			while(rs.next()) {
				result.put(rs.getString(1), rs.getInt(2));
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
	
	public boolean update(StadioDTO stadio) {
		boolean result = false;
		if (stadio == null) {
			System.err.println("create(): failed to create a null entry");
			return result;
		}

		Connection conn = Db2DAOFactory.createConnection();

		try {
			PreparedStatement prep_stmt = conn.prepareStatement(insert);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, stadio.getId());
			prep_stmt.setInt(2, stadio.getCodice());
			prep_stmt.setString(3, stadio.getNome());
			prep_stmt.setString(4, stadio.getCitta());

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
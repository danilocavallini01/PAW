package it.unibo.paw.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.unibo.paw.dao.RistoranteDAO;
import it.unibo.paw.dao.RistoranteDTO;

public class MySqlRistoranteDAO implements RistoranteDAO {

	static final String TABLE = "ristoranti";

	static final String ID = "id";
	static final String NOMERISTORANTE = "nome";
	static final String INDIRIZZO = "indirizzo";
	static final String RATING = "rating";

	// INSERT INTO table ( id, nome, indirizzo, ...) VALUES ( ?,?, ... );
	static final String insert = "INSERT " +
			"INTO " + TABLE + " ( " +
			ID + ", " + NOMERISTORANTE + ", " + INDIRIZZO + ", " + RATING + " " +
			") " +
			"VALUES (?,?,?,?) ";

	// SELECT * FROM table WHERE idcolumn = ?;
	static String read_by_name = "SELECT * " +
			"FROM " + TABLE + " " +
			"WHERE " + NOMERISTORANTE + " = ? ";

	// SELECT * FROM table WHERE idcolumn = ?;
	static String read_by_id = "SELECT * " +
			"FROM " + TABLE + " " +
			"WHERE " + ID + " = ? ";

	// UPDATE table SET xxxcolumn = ?, ... WHERE idcolumn = ?;
	static String update = "UPDATE " + TABLE + " " +
			"SET " +
			NOMERISTORANTE + " = ?, " +
			INDIRIZZO + " = ?, " +
			RATING + " = ? " +
			"WHERE " + ID + " = ? ";

	// DELETE FROM table WHERE idcolumn = ?;
	static String delete = "DELETE " +
			"FROM " + TABLE + " " +
			"WHERE " + ID + " = ? ";

	// SELECT * FROM table;
	static String query = "SELECT * " +
			"FROM " + TABLE + " ";

	// SELECT * FROM table ;
	static String read_all = "SELECT * " +
			"FROM " + TABLE + " ";

	static String find_resturant_over_rate = read_all +
			"WHERE " + RATING + " > ? ";

	// CREATE entrytable ( code INT NOT NULL PRIMARY KEY, ... );
	static String create = "CREATE " +
			"TABLE " + TABLE + " ( " +
			ID + " INT NOT NULL PRIMARY KEY, " +
			NOMERISTORANTE + " VARCHAR(50) NOT NULL UNIQUE, " +
			INDIRIZZO + " VARCHAR(50), " +
			RATING + " INT " +
			") ";

	static String drop = "DROP " +
			"TABLE " + TABLE + " ";

	@Override
	public void create(RistoranteDTO ristorante) {
		// TODO Auto-generated method stub
		// --- 1. Dichiarazione della variabile per il risultato ---
		//Long result = new Long(-1);
		// --- 2. Controlli preliminari sui dati in ingresso ---
		/*if ( t == null )  {
			logger.warning("create(): failed to insert a null entry");
			return result;
		}*/
		// --- 3. Apertura della connessione ---
		Connection conn = MySqlDAOFactory.createConnection();
		// --- 4. Tentativo di accesso al db e impostazione del risultato ---
		try {
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement
			PreparedStatement prep_stmt = conn.prepareStatement(insert);
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

			e.printStackTrace();
			//result = new Long(-2);
		}

		// Nel caso della creazione di una nuova tupla eseguo un secondo accesso per sapere che code le e' stato assegnato
		// Chiaramente e' inutile farlo se gia'� il primo accesso ha prodotto errori
		// --- 6./7. Chiusura della connessione in caso di errori e restituizione prematura di un risultato di fallimento
		/*if ( result == -2 ) {
			Db2DAOFactory.closeConnection(conn);
			return result;*/
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
		Connection conn = MySqlDAOFactory.createConnection();
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
				RistoranteDTO entry = new MySqlRistoranteDTOProxy();
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
			MySqlDAOFactory.closeConnection(conn);
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

			return result;
		}
		// --- 3. Apertura della connessione ---
		Connection conn = MySqlDAOFactory.createConnection();
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

			e.printStackTrace();
		}
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il controllo al chiamante
		finally {
			MySqlDAOFactory.closeConnection(conn);
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
		Connection conn = MySqlDAOFactory.createConnection();
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
			MySqlDAOFactory.closeConnection(conn);
		}
		// --- 7. Restituzione del risultato (eventualmente di fallimento)
		return result;
	}

	@Override
	public List<RistoranteDTO> getResturantByCity(String citta) {
		// TODO Auto-generated method stub
		List<RistoranteDTO> result = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---
		if (citta.isEmpty() || citta == null) {

			return result;
		}
		// --- 3. Apertura della connessione ---
		Connection conn = MySqlDAOFactory.createConnection();
		// --- 4. Tentativo di accesso al db e impostazione del risultato ---
		try {
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement
			PreparedStatement prep_stmt = conn.prepareStatement(query);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)

			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)
			ResultSet rs = prep_stmt.executeQuery();

			result = new ArrayList<RistoranteDTO>();
			// --- d. Cicla sul risultato (se presente) pe accedere ai valori di ogni sua tupla
			String address;
			while (rs.next()) {
				address = rs.getString("indirizzo").toLowerCase();
				if (address.contains("bologna")) {
					RistoranteDTO entry = new MySqlRistoranteDTOProxy();
					entry.setId(rs.getInt(ID));
					entry.setIndirizzo(address);
					entry.setRating(rs.getInt(RATING));
					entry.setNomeRistorante(rs.getString(NOMERISTORANTE));
					result.add(entry);
				}

			}

			// --- e. Rilascia la struttura dati del risultato
			rs.close();
			// --- f. Rilascia la struttura dati dello statement
			prep_stmt.close();
		}
		// --- 5. Gestione di eventuali eccezioni ---
		catch (Exception e) {

			e.printStackTrace();
		}
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il controllo al chiamante
		finally {
			MySqlDAOFactory.closeConnection(conn);
		}
		// --- 7. Restituzione del risultato (eventualmente di fallimento)
		return result;
	}

	@Override
	public List<RistoranteDTO> getRatedResturant(int stars) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		List<RistoranteDTO> result = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---
		if (stars < 1 || stars > 5) {

			return result;
		}
		// --- 3. Apertura della connessione ---
		Connection conn = MySqlDAOFactory.createConnection();
		// --- 4. Tentativo di accesso al db e impostazione del risultato ---
		try {
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement
			PreparedStatement prep_stmt = conn.prepareStatement(find_resturant_over_rate);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, stars - 1);
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)
			ResultSet rs = prep_stmt.executeQuery();

			result = new ArrayList<RistoranteDTO>();
			// --- d. Cicla sul risultato (se presente) pe accedere ai valori di ogni sua tupla
			while (rs.next()) {

				RistoranteDTO entry = new MySqlRistoranteDTOProxy();
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

			e.printStackTrace();
		}
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il controllo al chiamante
		finally {
			MySqlDAOFactory.closeConnection(conn);
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
		Connection conn = MySqlDAOFactory.createConnection();
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
			MySqlDAOFactory.closeConnection(conn);
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
		Connection conn = MySqlDAOFactory.createConnection();
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
			e.printStackTrace();
			System.out.println("La tabella tw-stud." + TABLE + " non è presente nel DB");
		}
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il controllo al chiamante
		finally {
			MySqlDAOFactory.closeConnection(conn);
		}
		// --- 7. Restituzione del risultato (eventualmente di fallimento)
		return result;
	}

}

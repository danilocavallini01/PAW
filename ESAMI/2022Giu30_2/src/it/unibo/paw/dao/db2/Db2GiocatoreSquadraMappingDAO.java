package it.unibo.paw.dao.db2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.unibo.paw.dao.GiocatoreDTO;
import it.unibo.paw.dao.GiocatoreSquadraMappingDAO;
import it.unibo.paw.dao.SquadrapallacanestroDTO;

public class Db2GiocatoreSquadraMappingDAO implements GiocatoreSquadraMappingDAO {

	// === Costanti letterali per non sbagliarsi a scrivere !!!
	// ============================

	static final String TABLE = "giocatore_squadra";

	// -------------------------------------------------------------------------------------

	static final String ID_G = "idGiocatore";
	static final String ID_S = "idSquadra";

	// == STATEMENT SQL
	// ====================================================================

	// INSERT INTO table ( idCourse, idStudent ) VALUES ( ?,? );
	static final String insert = "INSERT " + "INTO " + TABLE + " ( " + ID_G + ", " + ID_S + " " + ") "
			+ "VALUES (?,?) ";

	// SELECT * FROM table WHERE idcolumns = ?;
	static String read_by_ids = "SELECT * " + "FROM " + TABLE + " " + "WHERE " + ID_G + " = ? " + "AND " + ID_S
			+ " = ? ";

	// SELECT * FROM table WHERE idcolumns = ?;
	static String read_by_id_giocatore = "SELECT * " + "FROM " + TABLE + " " + "WHERE " + ID_G + " = ? ";

	// SELECT * FROM table WHERE idcolumns = ?;
	static String read_by_id_squadra = "SELECT * " + "FROM " + TABLE + " " + "WHERE " + ID_S + " = ? ";

	// SELECT * FROM table WHERE stringcolumn = ?;
	static String read_all = "SELECT * " + "FROM " + TABLE + " ";

	// DELETE FROM table WHERE idcolumn = ?;
	static String delete = "DELETE " + "FROM " + TABLE + " " + "WHERE " + ID_G + " = ? " + "AND " + ID_S + " = ? ";

	// -------------------------------------------------------------------------------------

	// CREATE entrytable ( code INT NOT NULL PRIMARY KEY, ... );
	static String create = "CREATE " + "TABLE " + TABLE + " ( " 
			+ ID_G + " INT NOT NULL, " 
			+ ID_S + " INT NOT NULL, "
			+ "PRIMARY KEY (" + ID_G + "," + ID_S + " ), " 
			+ "FOREIGN KEY (" + ID_S + ") REFERENCES " + Db2SquadrapallacanestroDAO.TABLE + "(id), "
			+ "FOREIGN KEY (" + ID_G + ") REFERENCES " + Db2GiocatoreDAO.TABLE + "(id) " + ") ";
	
	static String drop = "DROP " + "TABLE " + TABLE + " ";

	static String getSquadreByGiocatore = "SELECT * " + "FROM " + TABLE + " t, " + Db2SquadrapallacanestroDAO.TABLE
			+ " s " + "WHERE t." + ID_S + " = s.id AND t." + ID_G + " = ? ";

	static String getGiocatoriBySquadra = "SELECT * " + "FROM " + TABLE + " t, " + Db2GiocatoreDAO.TABLE + " g "
			+ "WHERE t." + ID_G + " = g.id AND t." + ID_S + " = ? ";

	// === METODI DAO
	// =========================================================================

	@Override
	public void create(int idGiocatore, int idSquadra) {
		Connection conn = Db2DAOFactory.createConnection();
		if (idGiocatore < 0 || idSquadra < 0) {
			System.err.println("create(): cannot insert an entry with an invalid id");
			return;
		}
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(insert);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, idGiocatore);
			prep_stmt.setInt(2, idSquadra);
			prep_stmt.executeUpdate();
			prep_stmt.close();
		} catch (Exception e) {
			System.err.println("create(): failed to insert entry: " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactory.closeConnection(conn);
		}
	}

	@Override
	public boolean delete(int idGiocatore, int idSquadra) {
		boolean result = false;
		if (idGiocatore < 0 || idSquadra < 0) {
			System.err.println("delete(): cannot delete an entry with an invalid id ");
			return result;
		}
		Connection conn = Db2DAOFactory.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(delete);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, idGiocatore);
			prep_stmt.setInt(2, idSquadra);
			prep_stmt.executeUpdate();
			result = true;
			prep_stmt.close();
		} catch (Exception e) {
			System.err.println("delete(): failed to delete entry with idCourse = " + idGiocatore + " and idStudent = "
					+ idSquadra + ": " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactory.closeConnection(conn);
		}
		return result;
	}

	@Override
	public boolean createTable() {
		boolean result = false;
		Connection conn = Db2DAOFactory.createConnection();
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(create);
			result = true;
			stmt.close();
		} catch (Exception e) {
			System.err.println("createTable(): failed to create table '" + TABLE + "': " + e.getMessage());
		} finally {
			Db2DAOFactory.closeConnection(conn);
		}
		return result;
	}

	@Override
	public boolean dropTable() {
		boolean result = false;
		Connection conn = Db2DAOFactory.createConnection();
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(drop);
			result = true;
			stmt.close();
		} catch (Exception e) {
			System.err.println("dropTable(): failed to drop table '" + TABLE + "': " + e.getMessage());
		} finally {
			Db2DAOFactory.closeConnection(conn);
		}
		return result;
	}

	@Override
	public List<GiocatoreDTO> getGiocatoriBySquadra(int id) {
		List<GiocatoreDTO> result = null;
		if (id < 0) {
			System.err.println("read(): cannot read an entry with a negative id");
			return result;
		}
		Connection conn = Db2DAOFactory.createConnection();
		result = new ArrayList<GiocatoreDTO>();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(getGiocatoriBySquadra);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, id);
			ResultSet rs = prep_stmt.executeQuery();
			while (rs.next()) {
				GiocatoreDTO entry = new GiocatoreDTO();
				entry.setId(rs.getInt(Db2GiocatoreDAO.ID));
				entry.setCodiceFiscale(rs.getString(Db2GiocatoreDAO.CODICEFISCALE));
				entry.setCognome(rs.getString(Db2GiocatoreDAO.COGNOME));
				entry.setNome(rs.getString(Db2GiocatoreDAO.NOME));
				entry.setEta(rs.getInt(Db2GiocatoreDAO.ETA));
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

	@Override
	public List<SquadrapallacanestroDTO> getSquadreByGiocatore(int id) {
		List<SquadrapallacanestroDTO> result = null;
		if (id < 0) {
			System.err.println("read(): cannot read an entry with a negative id");
			return result;
		}
		Connection conn = Db2DAOFactory.createConnection();
		result = new ArrayList<SquadrapallacanestroDTO>();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(getSquadreByGiocatore);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, id);
			ResultSet rs = prep_stmt.executeQuery();
			while (rs.next()) {
				SquadrapallacanestroDTO entry = new SquadrapallacanestroDTO();
				entry.setId(rs.getInt(Db2SquadrapallacanestroDAO.ID));
				entry.setNome(rs.getString(Db2SquadrapallacanestroDAO.NOME));
				entry.setTorneo(rs.getString(Db2SquadrapallacanestroDAO.TORNEO));
				entry.setAllenatore(rs.getString(Db2SquadrapallacanestroDAO.ALLENATORE));
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

}
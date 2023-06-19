package it.unibo.paw.dao.db2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.unibo.paw.dao.RicettaDTO;
import it.unibo.paw.dao.RicettaIngredienteMappingDAO;

public class Db2RicettaIngredienteMappingDAO implements RicettaIngredienteMappingDAO {
	// === Costanti letterali per non sbagliarsi a scrivere !!!
	// ============================
	private static final String TABLE = "ricetta_ingredienti";
	// -------------------------------------------------------------------------------------
	private static final String ID_R = "idRicetta";
	private static final String ID_I = "idIngrediente";
	// == STATEMENT SQL
	// ====================================================================
	private static final String insert = "INSERT " + "INTO " + TABLE + " ( " + ID_R + ", " + ID_I + " " + ") "
			+ "VALUES (?,?) ";

	// SELECT * FROM table WHERE idcolumns = ?;
	private static final String read_by_ids = "SELECT * " + "FROM " + TABLE + " " + "WHERE " + ID_R + " = ? " + "AND "
			+ ID_I + " = ? ";

	// SELECT * FROM table WHERE idcolumns = ?;
	private static final String read_by_ricettaId = "SELECT * " + "FROM " + TABLE + " " + "WHERE " + ID_R + " = ? ";

	// SELECT * FROM table WHERE idcolumns = ?;
	private static final String read_by_ingredienteId = "SELECT * " + "FROM " + TABLE + "  " + "WHERE " + ID_I
			+ " = ? ";

	private static final String read_ricette_by_ingredienteId = "SELECT r.* FROM " + TABLE + " m, " + Db2RicettaDAO.TABLE
			+ " r WHERE m." + ID_I + " = ? AND m." + ID_R + " = r." + Db2RicettaDAO.ID;
	
	private static final String read_ricette_include_aglio = "SELECT r.* FROM " + Db2RicettaDAO.TABLE + " r, " + TABLE + " map, "
			+ Db2IngredienteDAO.TABLE + " i WHERE i." + Db2IngredienteDAO.NOME + " = 'aglio' AND " 
			+ "i." + Db2IngredienteDAO.ID + " = map." + ID_I + " AND map." + ID_R + " = r." + Db2RicettaDAO.ID; 

	// SELECT * FROM table WHERE stringcolumn = ?;
	private static final String read_all = "SELECT * " + "FROM " + TABLE + " ";

	// DELETE FROM table WHERE idcolumn = ?;
	private static final String delete = "DELETE " + "FROM " + TABLE + " " + "WHERE " + ID_R + " = ? " + "AND " + ID_I
			+ " = ? ";

	// -------------------------------------------------------------------------------------
	// CREATE entrytable ( id INT NOT NULL PRIMARY KEY, ... );
	private static final String create = "CREATE " + "TABLE " + TABLE + " ( " 
			+ ID_R + " INT NOT NULL, " 
			+ ID_I + " INT NOT NULL, " 
			+ "PRIMARY KEY (" + ID_R + "," + ID_I + " ), " 
			+ "FOREIGN KEY (" + ID_R + ") REFERENCES " + Db2RicettaDAO.TABLE + "(id), " 
			+ "FOREIGN KEY (" + ID_I + ") REFERENCES " + Db2IngredienteDAO.TABLE + "(id) " + ")";

	private static final String drop = "DROP " + "TABLE " + TABLE + " ";

	@Override
	public void create(int idRicetta, int idIngrediente) {
		Connection conn = Db2DAOFactory.createConnection();
		if (idRicetta < 0 || idIngrediente < 0) {
			System.out.println("create(): cannot insert an entry with an invalid id ");
			return;
		}
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(insert);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, idRicetta);
			prep_stmt.setInt(2, idIngrediente);
			prep_stmt.executeUpdate();
			prep_stmt.close();
		} catch (Exception e) {
			System.out.println("create(): failed to insert entry: " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactory.closeConnection(conn);
		}

	}

	@Override
	public boolean delete(int idRicetta, int idIngrediente) {
		boolean result = false;
		if (idRicetta < 0 || idIngrediente < 0) {
			System.out.println("delete(): cannot delete an entry with an invalid id ");
			return result;
		}
		Connection conn = Db2DAOFactory.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(delete);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, idRicetta);
			prep_stmt.setInt(2, idIngrediente);
			prep_stmt.executeUpdate();
			result = true;
			prep_stmt.close();
		} catch (Exception e) {
			System.out.println("delete(): failed to delete entry with idRicetta = " + idRicetta
					+ " and idIngrediente = " + idIngrediente + ": " + e.getMessage());
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
			System.out.println("createTable(): failed to create table '" + TABLE + "': " + e.getMessage());
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
			System.out.println("dropTable(): failed to drop table '" + TABLE + "': " + e.getMessage());
		} finally {
			Db2DAOFactory.closeConnection(conn);
		}
		return result;
	}

	@Override
	public List<RicettaDTO> getRicetteFromIngrediente(int id) {
		List<RicettaDTO> result = null;
		if (id < 0) {
			System.out.println("read(): cannot read an entry with a negative id");
			return result;
		}
		Connection conn = Db2DAOFactory.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(read_ricette_by_ingredienteId);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, id);
			ResultSet rs = prep_stmt.executeQuery();

			result = new ArrayList<RicettaDTO>();
			while (rs.next()) {
				RicettaDTO entry = new RicettaDTO();
				entry.setId(rs.getInt(Db2RicettaDAO.ID));
				entry.setNome(rs.getString(Db2RicettaDAO.NOME));
				entry.setTempoPreparazione(rs.getInt(Db2RicettaDAO.TEMPOPREPARAZIONE));
				entry.setLivelloDifficolta(rs.getString(Db2RicettaDAO.LIVELLODIFFICOLTA));
				entry.setCalorie(rs.getInt(Db2RicettaDAO.CALORIE));
				result.add(entry);
			}
			rs.close();
			prep_stmt.close();
		} catch (Exception e) {
			System.out.println(
					"getRicetteFromIngrediente(): failed to read entry with " + ID_R + " = " + id + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactory.closeConnection(conn);
		}
		return result;
	}
	
	public List<String> getRicetteContenentiAglio() {
		List<String> result = null;

		Connection conn = Db2DAOFactory.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(read_ricette_include_aglio);
			prep_stmt.clearParameters();

			ResultSet rs = prep_stmt.executeQuery();

			result = new ArrayList<String>();
			while (rs.next()) {
				result.add(rs.getString(Db2RicettaDAO.NOME));
			}
			rs.close();
			prep_stmt.close();
		} catch (Exception e) {
			System.out.println(
					"getPiattiFromResturant(): failed to read entries " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactory.closeConnection(conn);
		}
		return result;
	}
}

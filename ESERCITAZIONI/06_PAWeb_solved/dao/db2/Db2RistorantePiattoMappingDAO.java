package it.unibo.paw.dao.db2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.unibo.paw.dao.PiattoDTO;
import it.unibo.paw.dao.RistorantePiattoMappingDAO;

public class Db2RistorantePiattoMappingDAO implements
		RistorantePiattoMappingDAO {
	// === Costanti letterali per non sbagliarsi a scrivere !!! ============================
	private static final String TABLE = "ristoranti_piatti";
	// -------------------------------------------------------------------------------------
	private static final String ID_R = "idRistorante";
	private static final String ID_P = "idPiatto";
	// == STATEMENT SQL ====================================================================
	private static final String insert = "INSERT " +
			"INTO " + TABLE + " ( " +
			ID_R + ", " + ID_P + " " +
			") " +
			"VALUES (?,?) ";

	// SELECT * FROM table WHERE idcolumns = ?;
	private static final String read_by_ids = "SELECT * " +
			"FROM " + TABLE + " " +
			"WHERE " + ID_R + " = ? " +
			"AND " + ID_P + " = ? ";

	// SELECT * FROM table WHERE idcolumns = ?;
	private static final String read_by_ristoranteID = "SELECT * " +
			"FROM " + TABLE + " " +
			"WHERE " + ID_R + " = ? ";

	// SELECT * FROM table WHERE idcolumns = ?;
	private static final String read_by_piattoID = "SELECT * " +
			"FROM " + TABLE + "  " +
			"WHERE " + ID_P + " = ? ";

	// SELECT * FROM table WHERE idcolumns = ?;
	private static final String dish_query = "SELECT * " +
			"FROM " + TABLE + " RP, piatti P " +
			"WHERE RP.idPiatto = P.id AND " + ID_R + " = ? ";

	// SELECT * FROM table WHERE stringcolumn = ?;
	private static final String read_all = "SELECT * " +
			"FROM " + TABLE + " ";

	// DELETE FROM table WHERE idcolumn = ?;
	private static final String delete = "DELETE " +
			"FROM " + TABLE + " " +
			"WHERE " + ID_R + " = ? " +
			"AND " + ID_P + " = ? ";

	// -------------------------------------------------------------------------------------
	// CREATE entrytable ( id INT NOT NULL PRIMARY KEY, ... );
	private static final String create = "CREATE " +
			"TABLE " + TABLE + " ( " +
			ID_R + " INT NOT NULL, " +
			ID_P + " INT NOT NULL, " +
			"PRIMARY KEY (" + ID_R + "," + ID_P + " ), " +
			"FOREIGN KEY (" + ID_R + ") REFERENCES ristoranti(id), " +
			"FOREIGN KEY (" + ID_P + ") REFERENCES piatti(id) " +
			") ";

	private static final String drop = "DROP " +
			"TABLE " + TABLE + " ";

	@Override
	public void create(int idr, int idp) {
		Connection conn = Db2DAOFactory.createConnection();
		if (idr < 0 || idp < 0) {
			System.out.println("create(): cannot insert an entry with an invalid id ");
			return;
		}
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(insert);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, idr);
			prep_stmt.setInt(2, idp);
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
	public boolean delete(int idRistorante, int idPiatto) {
		boolean result = false;
		if (idRistorante < 0 || idPiatto < 0) {
			System.out.println("delete(): cannot delete an entry with an invalid id ");
			return result;
		}
		Connection conn = Db2DAOFactory.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(delete);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, idRistorante);
			prep_stmt.setInt(2, idPiatto);
			prep_stmt.executeUpdate();
			result = true;
			prep_stmt.close();
		} catch (Exception e) {
			System.out.println(
					"delete(): failed to delete entry with idRistorante = " + idRistorante + " and idPiatto = " + idPiatto + ": " + e.getMessage());
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
	public List<PiattoDTO> getPiattiFromResturant(int id) {
		List<PiattoDTO> result = null;
		if (id < 0) {
			System.out.println("read(): cannot read an entry with a negative id");
			return result;
		}
		Connection conn = Db2DAOFactory.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(dish_query);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, id);
			ResultSet rs = prep_stmt.executeQuery();

			result = new ArrayList<PiattoDTO>();
			while (rs.next()) {
				PiattoDTO entry = new PiattoDTO();
				entry.setId((rs.getInt("id")));
				entry.setNomePiatto(rs.getString("nome"));
				entry.setTipo(rs.getString("tipo"));
				result.add(entry);
			}
			rs.close();
			prep_stmt.close();
		} catch (Exception e) {
			System.out.println(
					"getPiattiFromResturant(): failed to read entry with " + ID_R + " = " + id + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactory.closeConnection(conn);
		}
		return result;
	}

}

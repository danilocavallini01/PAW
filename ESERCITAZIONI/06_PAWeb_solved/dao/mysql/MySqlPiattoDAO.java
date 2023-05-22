package it.unibo.paw.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import it.unibo.paw.dao.PiattoDAO;
import it.unibo.paw.dao.PiattoDTO;

public class MySqlPiattoDAO implements PiattoDAO {

	// === Costanti letterali per non sbagliarsi a scrivere !!! ============================

	static final String TABLE = "piatti";

	// -------------------------------------------------------------------------------------

	static final String ID = "id";
	static final String TIPO = "tipo";
	static final String NOME = "nome";

	// == STATEMENT SQL ====================================================================

	// INSERT INTO table ( name,description, ...) VALUES ( ?,?, ... );
	static final String insert = "INSERT "+
			"INTO " + TABLE + " ( " +
			ID + ", " + NOME + ", " + TIPO + " " +
			") " +
			"VALUES (?,?,?) ";

	// SELECT * FROM table WHERE idcolumn = ?;
	static String read_by_name = "SELECT * " +
			"FROM " + TABLE + " " +
			"WHERE " + NOME + " = ? ";

	// SELECT * FROM table WHERE idcolumn = ?;
	static String read_by_id = "SELECT * " +
			"FROM " + TABLE + " " +
			"WHERE " + ID + " = ? ";

	// UPDATE table SET xxxcolumn = ?, ... WHERE idcolumn = ?;
	static String update = "UPDATE " + TABLE + " " +
			"SET " +
			TIPO + " = ? " +
			"WHERE " + NOME + " = ? ";

	// DELETE FROM table WHERE idcolumn = ?;
	static String delete = "DELETE " +
			"FROM " + TABLE + " " +
			"WHERE " + ID + " = ? ";

	// CREATE entrytable ( code INT NOT NULL PRIMARY KEY, ... );
	static String create = "CREATE " +
			"TABLE " + TABLE + " ( " +
			ID + " INT NOT NULL PRIMARY KEY, " +
			NOME + " VARCHAR(50) NOT NULL UNIQUE," +
			TIPO + " VARCHAR(50) NOT NULL" +
			") ";

	static String drop = "DROP " +
			"TABLE " + TABLE + " ";

	// === METODI DAO =========================================================================
	@Override
	public void create(PiattoDTO piatto) {
		// TODO Auto-generated method stub
		Connection conn = MySqlDAOFactory.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(insert);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, piatto.getId());
			prep_stmt.setString(2, piatto.getNomePiatto());
			prep_stmt.setString(3, piatto.getTipo());
			prep_stmt.executeUpdate();
			prep_stmt.close();
		} catch (Exception e) {
			System.out.println("create(): failed to insert entry: " + e.getMessage());
			e.printStackTrace();
			//result = new Long(-2);
		}
	}

	@Override
	public PiattoDTO read(int id) {
		PiattoDTO result = null;
		if (id < 0) {
			System.out.println("read(): cannot read an entry with a negative id");
			return result;
		}
		Connection conn = MySqlDAOFactory.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(read_by_name);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, id);
			ResultSet rs = prep_stmt.executeQuery();
			if (rs.next()) {
				PiattoDTO entry = new PiattoDTO();
				entry.setId(rs.getInt(ID));
				entry.setNomePiatto(rs.getString(NOME));
				entry.setTipo(rs.getString(TIPO));
				result = entry;
			}
			rs.close();
			prep_stmt.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			MySqlDAOFactory.closeConnection(conn);
		}
		return result;
	}

	@Override
	public PiattoDTO findByName(String nome) {
		PiattoDTO result = null;
		if (nome.isEmpty() || nome == null) {
			System.out.println("read(): cannot read an entry with an invalid name");
			return result;
		}
		Connection conn = MySqlDAOFactory.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(read_by_name);
			prep_stmt.clearParameters();
			prep_stmt.setString(1, nome);
			ResultSet rs = prep_stmt.executeQuery();
			if (rs.next()) {
				PiattoDTO entry = new PiattoDTO();
				entry.setId(rs.getInt(ID));
				entry.setNomePiatto(rs.getString(NOME));
				entry.setTipo(rs.getString(TIPO));
				result = entry;
			}
			rs.close();
			prep_stmt.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			MySqlDAOFactory.closeConnection(conn);
		}
		return result;
	}

	@Override
	public boolean update(PiattoDTO piatto) {
		boolean result = false;
		if (piatto == null) {
			System.out.println("update(): failed to update a null entry");
			return result;
		}
		Connection conn = MySqlDAOFactory.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(update);
			prep_stmt.clearParameters();
			prep_stmt.setString(1, piatto.getTipo());
			prep_stmt.setString(2, piatto.getNomePiatto());
			prep_stmt.executeUpdate();
			result = true;
			prep_stmt.close();
		} catch (Exception e) {
			System.out.println("insert(): failed to update entry: " + e.getMessage());
			e.printStackTrace();
		} finally {
			MySqlDAOFactory.closeConnection(conn);
		}
		return result;
	}

	@Override
	public boolean delete(int id) {
		boolean result = false;
		if (id < 0) {
			System.out.println("delete(): cannot delete an entry with an invalid id ");
			return result;
		}
		Connection conn = MySqlDAOFactory.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(delete);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, id);
			prep_stmt.executeUpdate();
			result = true;
			prep_stmt.close();
		} catch (Exception e) {
			//System.out.println("delete(): failed to delete entry with id = " + id+": "+e.getMessage());
			e.printStackTrace();
		} finally {
			MySqlDAOFactory.closeConnection(conn);
		}
		return result;
	}

	@Override
	public boolean createTable() {
		boolean result = false;
		Connection conn = MySqlDAOFactory.createConnection();
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(create);
			result = true;
			stmt.close();
		} catch (Exception e) {
			System.out.println("createTable(): failed to create table '" + TABLE + "': " + e.getMessage());
		} finally {
			MySqlDAOFactory.closeConnection(conn);
		}
		return result;
	}

	@Override
	public boolean dropTable() {
		boolean result = false;
		Connection conn = MySqlDAOFactory.createConnection();
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(drop);
			result = true;
			stmt.close();
		} catch (Exception e) {
			System.out.println("dropTable(): failed to drop table '" + TABLE + "': " + e.getMessage());
		} finally {
			MySqlDAOFactory.closeConnection(conn);
		}
		return result;
	}
}

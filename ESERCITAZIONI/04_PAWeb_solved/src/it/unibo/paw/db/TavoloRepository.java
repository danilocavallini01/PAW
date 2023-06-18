package it.unibo.paw.db;

import java.sql.*;

import it.unibo.paw.model.Tavolo;

public class TavoloRepository {
	private DataSource dataSource;

	// === Costanti letterali per non sbagliarsi a scrivere !!! ============================

	private static final String TABLE = "tavolo";

	// -------------------------------------------------------------------------------------

	private static final String ID = "id";
	private static final String NUMERO = "numero";
	private static final String CAPIENZA = "capienza";

	// == STATEMENT SQL ====================================================================

	// create table
	private static String create = "CREATE TABLE " + TABLE + " ( " +
			ID + " INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1), " +
			NUMERO + " VARCHAR(10) NOT NULL UNIQUE, " +
			CAPIENZA + " INT, "
			+ "PRIMARY KEY (" + ID + ")" +
			") ";

	// drop table
	private static String drop = "DROP TABLE " + TABLE + " ";

	// -------------------------------------------------------------------------------------

	// INSERT INTO table ( email, description, ...) VALUES ( ?,?, ... );
	private static final String insert = "INSERT INTO " + TABLE + " ( " +
			NUMERO + ", " + CAPIENZA + " " +
			") " +
			"VALUES (?,?) ";

	// DELETE FROM table WHERE idcolumn = ?;
	private static String delete = "DELETE " +
			"FROM " + TABLE + " " +
			"WHERE " + ID + " = ? ";

	// UPDATE table SET xxxcolumn = ?, ... WHERE idcolumn = ?;
	private static String update = "UPDATE " + TABLE + " SET " +
			NUMERO + " = ?, " +
			CAPIENZA + " = ? " +
			"WHERE " + ID + " = ? ";

	private static String read_available_table = "SELECT " + NUMERO +
			" FROM " + TABLE + " " +
			"WHERE " + "capienza" + " >= ? AND " + ID + " NOT IN ( SELECT idTavolo FROM prenotazione WHERE data = ?)";

	private static String read_id_per_code = "SELECT " + ID +
			" FROM " + TABLE + " " +
			"WHERE " + NUMERO + " = ? ";
	
	private static String read_code_per_id = "SELECT " + NUMERO +
			" FROM " + TABLE + " " +
			"WHERE " + ID + " = ? ";

	// =====================================================================================

	public TavoloRepository(int databaseType) {
		dataSource = new DataSource(databaseType);
	}
	
	public void dropTable() throws PersistenceException {
		Connection conn = this.dataSource.getConnection();
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(drop);
		} catch (SQLException e) {
			// the table does not exist
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
	}

	public void createTable() throws PersistenceException {
		Connection connection = this.dataSource.getConnection();

		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(create);
		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
	}

	public void persist(Tavolo t) throws PersistenceException {
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = this.dataSource.getConnection();
			statement = connection.prepareStatement(insert);
			statement.setString(1, t.getNumeroTavolo());
			statement.setDouble(2, t.getCapienzaTavolo());

			statement.executeUpdate();
		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (connection != null) {
					connection.close();
					connection = null;
				}
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}

	}

	public int getIdFromNumber(String numeroTav) throws PersistenceException {
		int result = -1;
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = this.dataSource.getConnection();
			statement = connection.prepareStatement(read_id_per_code);
			statement.setString(1, numeroTav);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				result = rs.getInt(ID);
			} else
				result = -1;
			return result;
		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (connection != null) {
					connection.close();
					connection = null;
				}
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
	}
	
	public String getNumberFromId(int id) throws PersistenceException {
		String result = null;
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = this.dataSource.getConnection();
			statement = connection.prepareStatement(read_code_per_id);
			statement.setInt(1, id);
			ResultSet rs = statement.executeQuery();
			
			if (rs.next())
				result = rs.getString(NUMERO);
			
			return result;
		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (connection != null) {
					connection.close();
					connection = null;
				}
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
	}

	public String availableTable(Date data, int persone) throws PersistenceException {
		String result = null;
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = this.dataSource.getConnection();
			statement = connection.prepareStatement(read_available_table);
			statement.setInt(1, persone);
			statement.setDate(2, data);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				result = rs.getString(NUMERO);
			} else
				result = null;
			return result;
		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (connection != null) {
					connection.close();
					connection = null;
				}
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
	}

	public void update(Tavolo t) throws PersistenceException {
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = this.dataSource.getConnection();
			statement = connection.prepareStatement(update);

			statement.setString(1, t.getNumeroTavolo());
			statement.setInt(2, t.getCapienzaTavolo());
			statement.setInt(3, t.getIdTavolo());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (connection != null) {
					connection.close();
					connection = null;
				}
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
	}

	public void delete(int id) throws PersistenceException {
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = this.dataSource.getConnection();
			statement = connection.prepareStatement(delete);

			statement.setInt(1, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (connection != null) {
					connection.close();
					connection = null;
				}
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
	}

}

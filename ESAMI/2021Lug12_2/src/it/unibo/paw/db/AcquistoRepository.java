package it.unibo.paw.db;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.unibo.paw.model.Acquisto;

public class AcquistoRepository {
	private DataSource dataSource;

	// === Costanti letterali per non sbagliarsi a scrivere !!!
	// ============================

	private static final String TABLE = "acquisto";

	// -------------------------------------------------------------------------------------

	private static final String ID = "id";
	private static final String CODICEACQUISTO = "codiceAcquisto";
	private static final String IMPORTO = "importo";
	private static final String NOMEACQUIRENTE = "nomeAcquirente";
	private static final String COGNOMEACQUIRENTE = "cognomeAcquirente";

	// == STATEMENT SQL
	// ====================================================================

	// create table
	private static String create = "CREATE TABLE " + TABLE + " ( " + ID + " INT NOT NULL PRIMARY KEY," + CODICEACQUISTO
			+ " INT NOT NULL UNIQUE, " + IMPORTO + " FLOAT NOT NULL, " + NOMEACQUIRENTE + " VARCHAR(50) NOT NULL, "
			+ COGNOMEACQUIRENTE + " VARCHAR(50) NOT NULL )";

	// drop table
	private static String drop = "DROP TABLE " + TABLE + " ";

	// -------------------------------------------------------------------------------------

	// INSERT INTO table ( email, description, ...) VALUES ( ?,?, ... );
	private static final String insert = "INSERT INTO " + TABLE + " ( " + ID + ", " + CODICEACQUISTO + ", " + IMPORTO
			+ ", " + NOMEACQUIRENTE + ", " + COGNOMEACQUIRENTE + ") " + "VALUES (?,?,?,?,?) ";

	// DELETE FROM table WHERE idcolumn = ?;
	private static String delete = "DELETE " + "FROM " + TABLE + " " + "WHERE " + ID + " = ? ";

	// UPDATE table SET xxxcolumn = ?, ... WHERE idcolumn = ?;
	private static String update = "UPDATE " + TABLE + " SET " + CODICEACQUISTO + " = ?, " + IMPORTO + " = ?, "
			+ NOMEACQUIRENTE + " = ?, " + COGNOMEACQUIRENTE + " = ? " + " WHERE " + ID + " = ? ";

	private static String read = "SELECT * " + " FROM " + TABLE + " WHERE " + ID + " = ?";

	private static String read_acquisti_where_importo_gt_soglia = "SELECT " + CODICEACQUISTO + " FROM " + TABLE
			+ " WHERE " + IMPORTO + " > ? ";
	// =====================================================================================

	public AcquistoRepository(int databaseType) {
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

	public void persist(Acquisto a) throws PersistenceException {
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = this.dataSource.getConnection();
			statement = connection.prepareStatement(insert);

			statement.setInt(1, a.getId());
			statement.setInt(2, a.getCodiceAcquisto());
			statement.setFloat(3, a.getImporto());
			statement.setString(4, a.getNomeAcquirente());
			statement.setString(5, a.getCognomeAcquirente());

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

	public Acquisto read(int id) throws PersistenceException {
		Connection connection = null;
		PreparedStatement pstmt = null;

		Acquisto result = null;

		try {
			connection = this.dataSource.getConnection();
			pstmt = connection.prepareStatement(read);

			pstmt.setInt(1, id);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				result = new Acquisto();

				result.setId(rs.getInt(ID));
				result.setCodiceAcquisto(rs.getInt(CODICEACQUISTO));
				result.setImporto(rs.getFloat(IMPORTO));
				result.setNomeAcquirente(rs.getString(NOMEACQUIRENTE));
				result.setCognomeAcquirente(rs.getString(COGNOMEACQUIRENTE));

			}
		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (connection != null) {
					connection.close();
					connection = null;
				}
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
		return result;
	}

	public void update(Acquisto a) throws PersistenceException {
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = this.dataSource.getConnection();

			statement = connection.prepareStatement(update);

			statement.setInt(1, a.getCodiceAcquisto());
			statement.setFloat(2, a.getImporto());
			statement.setString(3, a.getNomeAcquirente());
			statement.setString(4, a.getCognomeAcquirente());
			statement.setInt(5, a.getId());

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

	public List<Integer> codiciAcquistoConImportoMaggioreDi(int soglia) throws PersistenceException {

		Connection connection = null;
		PreparedStatement pstmt = null;

		List<Integer> result = new ArrayList<Integer>();

		try {
			connection = this.dataSource.getConnection();
			pstmt = connection.prepareStatement(read_acquisti_where_importo_gt_soglia);

			pstmt.setInt(1, soglia);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				result.add(rs.getInt(CODICEACQUISTO));
			}

		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (connection != null) {
					connection.close();
					connection = null;
				}
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
		return result;
	}

}

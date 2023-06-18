package it.unibo.paw.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.unibo.paw.model.ProdottoOfferto;
import it.unibo.paw.model.Supermercato;

public class ProdottoRepository {
	private DataSource dataSource;

	// === Costanti letterali per non sbagliarsi a scrivere !!!
	// ============================

	private static final String TABLE = "prodotto";

	// -------------------------------------------------------------------------------------

	private static final String CODICEPRODOTTO = "codiceProdotto";
	private static final String DESCRIZIONE = "descrizione";
	private static final String MARCA = "marca";
	private static final String PREZZO = "prezzo";
	private static final String ID_S = "idSupermercato";

	// == STATEMENT SQL
	// ====================================================================

	// create table
	private static String create = "CREATE TABLE " + TABLE + " ( " + CODICEPRODOTTO + " INT NOT NULL PRIMARY KEY, "
			+ DESCRIZIONE + " VARCHAR(50) NOT NULL, " + MARCA + " VARCHAR(50) NOT NULL, " + PREZZO + " INT NOT NULL, " + ID_S
			+ " INT NOT NULL," + " UNIQUE(DESCRIZIONE,MARCA) " + ") ";

	// drop table
	private static String drop = "DROP TABLE " + TABLE + " ";

	// -------------------------------------------------------------------------------------

	// INSERT INTO table ( email, description, ...) VALUES ( ?,?, ... );
	private static final String insert = "INSERT INTO " + TABLE + " ( " + CODICEPRODOTTO + ", " + DESCRIZIONE + ", "
			+ MARCA + ", " + PREZZO + ", " + ID_S + ") " + "VALUES (?,?,?,?,?) ";

	// DELETE FROM table WHERE idcolumn = ?;
	private static String delete = "DELETE " + "FROM " + TABLE + " " + "WHERE " + CODICEPRODOTTO + " = ? ";

	// UPDATE table SET xxxcolumn = ?, ... WHERE idcolumn = ?;
	private static String update = "UPDATE " + TABLE + " SET " + CODICEPRODOTTO + " = ?, " + DESCRIZIONE + " = ? "
			+ MARCA + " = ? " + PREZZO + " = ? " + ID_S + " = ? " + "WHERE " + CODICEPRODOTTO + " = ? ";

	static String read = "SELECT * " + " FROM " + TABLE + " WHERE " + CODICEPRODOTTO + " = ?";
	static String read_by_supermercato = "SELECT * " + " FROM " + TABLE + " WHERE " + ID_S + " = ?";

	// =====================================================================================

	public ProdottoRepository(int databaseType) {
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

	public void persist(ProdottoOfferto p, int idSupermercato) throws PersistenceException {
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = this.dataSource.getConnection();
			statement = connection.prepareStatement(insert);

			statement.setInt(1, p.getCodiceProdotto());
			statement.setString(2, p.getDescrizione());
			statement.setString(3, p.getMarca());
			statement.setInt(4, p.getPrezzo());
			statement.setInt(5, idSupermercato);

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

	public ProdottoOfferto read(int id) throws PersistenceException {
		Connection connection = null;
		PreparedStatement pstmt = null;

		ProdottoOfferto result = null;

		try {
			connection = this.dataSource.getConnection();
			pstmt = connection.prepareStatement(read);

			pstmt.setInt(1, id);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				result = new ProdottoOfferto();

				result.setCodiceProdotto(rs.getInt(CODICEPRODOTTO));
				result.setDescrizione(rs.getString(DESCRIZIONE));
				result.setMarca(rs.getString(MARCA));
				result.setPrezzo(rs.getInt(PREZZO));

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

	public List<ProdottoOfferto> retrieveProdottiBySupermercato(int idSupermercato) throws PersistenceException {
		Connection connection = null;
		PreparedStatement pstmt = null;

		List<ProdottoOfferto> result = new ArrayList<ProdottoOfferto>();

		try {
			connection = this.dataSource.getConnection();
			pstmt = connection.prepareStatement(read);

			pstmt.setInt(1, idSupermercato);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				ProdottoOfferto prodotto = new ProdottoOfferto();

				prodotto.setCodiceProdotto(rs.getInt(CODICEPRODOTTO));
				prodotto.setDescrizione(rs.getString(DESCRIZIONE));
				prodotto.setMarca(rs.getString(MARCA));
				prodotto.setPrezzo(rs.getInt(PREZZO));

				result.add(prodotto);

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

	public void update(ProdottoOfferto p, int idSupermercato) throws PersistenceException {
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = this.dataSource.getConnection();
			statement = connection.prepareStatement(update);

			statement.setInt(1, p.getCodiceProdotto());
			statement.setString(2, p.getDescrizione());
			statement.setString(3, p.getMarca());
			statement.setInt(4, p.getPrezzo());
			statement.setInt(5, idSupermercato);

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

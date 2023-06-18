package it.unibo.paw.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import it.unibo.paw.model.ProdottoOfferto;
import it.unibo.paw.model.Supermercato;

public class SupermercatoRepository {
	private DataSource dataSource;

	// === Costanti letterali per non sbagliarsi a scrivere !!!
	// ============================

	private static final String TABLE = "supermercato";

	// -------------------------------------------------------------------------------------

	private static final String CODICESUPER = "codiceSuper";
	private static final String NOME = "nome";
	private static final String RATINGGRADIMENTO = "ratingGradimento";

	// == STATEMENT SQL
	// ====================================================================

	// create table
	private static String create = "CREATE TABLE " + TABLE + " ( " + CODICESUPER + " INT NOT NULL PRIMARY KEY, " + NOME
			+ " VARCHAR(50) NOT NULL, " + RATINGGRADIMENTO + " INT NOT NULL" + ") ";

	// drop table
	private static String drop = "DROP TABLE " + TABLE;

	// -------------------------------------------------------------------------------------

	private static final String insert = "INSERT INTO " + TABLE + " ( " + CODICESUPER + ", " + NOME + ", "
			+ RATINGGRADIMENTO + " ) " + "VALUES (?,?,?) ";

	// DELETE FROM table WHERE idcolumn = ?;
	static String delete = "DELETE " + "FROM " + TABLE + " " + "WHERE " + CODICESUPER + " = ? ";

	// UPDATE table SET xxxcolumn = ?, ... WHERE idcolumn = ?;
	static String update = "UPDATE " + TABLE + " SET " + CODICESUPER + " = ?, " + NOME + " = ?, " + RATINGGRADIMENTO
			+ " = ?, " + "WHERE " + CODICESUPER + " = ? ";

	static String read = "SELECT * " + " FROM " + TABLE + " WHERE " + CODICESUPER + " = ?";

	static String read_by_nome = "SELECT * " + " FROM " + TABLE + " s, prodotto p WHERE " + NOME
			+ " = ? AND s.codiceSuper = p.idSupermercato" + " AND p.descrizione = ? AND p.marca = ?";
	// =====================================================================================

	public SupermercatoRepository(int databaseType) {
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

	public void persist(Supermercato s) throws PersistenceException {
		Connection connection = null;
		PreparedStatement stmt = null;

		try {
			connection = this.dataSource.getConnection();
			stmt = connection.prepareStatement(insert);

			stmt.setInt(1, s.getCodiceSuper());
			stmt.setString(2, s.getNome());
			stmt.setInt(3, s.getRatingGradimento());

			stmt.executeUpdate();

		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (connection != null) {
					connection.close();
					connection = null;
				}
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
	}

	public Supermercato read(int id) throws PersistenceException {
		Connection connection = null;
		PreparedStatement pstmt = null;

		Supermercato result = null;

		try {
			connection = this.dataSource.getConnection();
			pstmt = connection.prepareStatement(read);

			pstmt.setInt(1, id);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				result = new Supermercato();

				result.setCodiceSuper(rs.getInt(CODICESUPER));
				result.setNome(rs.getString(NOME));
				result.setRatingGradimento(rs.getInt(RATINGGRADIMENTO));

				ProdottoRepository pr = new ProdottoRepository(DataSource.DB2);

				result.setProdotti(pr.retrieveProdottiBySupermercato(result.getCodiceSuper()));
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

	public void update(Supermercato s) throws PersistenceException {
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = this.dataSource.getConnection();
			statement = connection.prepareStatement(update);

			statement.setInt(1, s.getCodiceSuper());
			statement.setString(2, s.getNome());
			statement.setInt(3, s.getRatingGradimento());

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

	// -----------------------------------------------------------

	public boolean prodottoOfferto(String nomeSupermercato, String descrizione, String marca) throws PersistenceException {
		Connection connection = null;
		PreparedStatement pstmt = null;

		boolean result = false;

		try {
			connection = this.dataSource.getConnection();
			pstmt = connection.prepareStatement(read_by_nome);

			pstmt.setString(1, nomeSupermercato);
			pstmt.setString(2, descrizione);
			pstmt.setString(3, marca);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				result = true;
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

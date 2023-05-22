package it.unibo.paw.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.unibo.paw.dao.PiattoDTO;
import it.unibo.paw.dao.RistorantePiattoMappingDAO;



public class MySqlRistorantePiattoMappingDAO implements
		RistorantePiattoMappingDAO {

	static final String TABLE = "ristoranti_piatti";

	// -------------------------------------------------------------------------------------

	static final String ID_R = "idRistorante";
	static final String ID_P = "idPiatto";
	
	// INSERT INTO table ( idRistorante, idPiatto) VALUES ( ?,? );
			static final String insert = 
				"INSERT " +
					"INTO " + TABLE + " ( " + 
						ID_R +", "+ID_P + " " +
					") " +
					"VALUES (?,?) "
				;	
			
			// SELECT * FROM table WHERE idcolumns = ?;
			static String read_by_ids = 
				"SELECT * " +
					"FROM " + TABLE + " " +
					"WHERE " + ID_R + " = ? " +
					"AND " + ID_P + " = ? "
				;
			
			// DELETE FROM table WHERE idcolumn = ?;
			static String delete = 
				"DELETE " +
					"FROM " + TABLE + " " +
					"WHERE " + ID_R + " = ? " +
					"AND " + ID_P + " = ? "
				;
			
			// CREATE entrytable ( code INT NOT NULL PRIMARY KEY, ... );
			static String create = 
				"CREATE " +
					"TABLE " + TABLE +" ( " +
						ID_R + " INT NOT NULL, " +
						ID_P + " INT NOT NULL, " +
						"PRIMARY KEY (" + ID_R +","+ ID_P + " ), " +
						"FOREIGN KEY ("+ID_R+") REFERENCES ristoranti(id), "+
						"FOREIGN KEY ("+ID_P+") REFERENCES piatti(id) "+
					") "
				;
			
			static String drop = 
					"DROP " +
						"TABLE " + TABLE + " "
					;
			
			// SELECT * FROM table WHERE idcolumns = ?;
			static String dish_query = 
				"SELECT * " +
					"FROM " + TABLE + " RP, piatti P " +
					"WHERE RP.idPiatto = P.id AND " + ID_R + " = ? "
				;
	
	@Override
	public void create(int idr, int idp) {
		Connection conn = MySqlDAOFactory.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(insert);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, idr);
			prep_stmt.setInt(2, idp);
			prep_stmt.executeUpdate();
			prep_stmt.close();
		}
		catch (Exception e) {
			System.out.println("create(): failed to insert entry: " + e.getMessage());
			e.printStackTrace();
		}

	}

//	

	@Override
	public boolean delete(int idRistorante, int idPiatto) {
		boolean result = false;
		if ( idRistorante < 0 || idPiatto < 0 )  {
			System.out.println("delete(): cannot delete an entry with an invalid id ");
			return result;
		}
		Connection conn = MySqlDAOFactory.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(delete);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, idRistorante);
			prep_stmt.setInt(2, idPiatto);
			prep_stmt.executeUpdate();
			result = true;
			prep_stmt.close();
		}
		catch (Exception e) {
			System.out.println("delete(): failed to delete entry with idCourse = " + idRistorante +" and idStudent = " + idPiatto + ": "+e.getMessage());
			e.printStackTrace();
		}
		finally {
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
		}
		catch (Exception e) {
			System.out.println("createTable(): failed to create table '"+TABLE+"': "+e.getMessage());
		}
		finally {
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
		}
		catch (Exception e) {
			System.out.println("dropTable(): failed to drop table '"+TABLE+"': "+e.getMessage());
		}
		finally {
			MySqlDAOFactory.closeConnection(conn);
		}
		return result;
	}

	@Override
	public List<PiattoDTO> getPiattiFromResturant(int id) {
		List<PiattoDTO> result = null;
		if ( id< 0 )  {
			System.out.println("read(): cannot read an entry with a negative id");
			return result;
		}
		Connection conn = MySqlDAOFactory.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(dish_query);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, id);
			ResultSet rs = prep_stmt.executeQuery();
			
			result = new ArrayList<PiattoDTO>();
			while ( rs.next() ) {
				PiattoDTO entry = new PiattoDTO();
				entry.setId((rs.getInt("id")));
				entry.setNomePiatto(rs.getString("nome"));
				entry.setTipo(rs.getString("tipo"));
				result.add(entry);
			}
			rs.close();
			prep_stmt.close();
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		finally {
			MySqlDAOFactory.closeConnection(conn);
		}
		return result;
	}

}

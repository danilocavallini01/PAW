package it.unibo.paw.dao.db2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import it.unibo.paw.dao.IdBroker;

public class Db2IdBroker implements IdBroker {

	static String sequence_query = "VALUES NEXT VALUE FOR sequenza";

	@Override
	public int newId() {
		int result = -1;
		Connection conn = Db2DAOFactory.createConnection();

		try {
			PreparedStatement prep_stmt = conn.prepareStatement(sequence_query);

			prep_stmt.clearParameters();
			ResultSet rs = prep_stmt.executeQuery();

			if (!rs.next()) {
				throw new Exception();
			}

			result = rs.getInt(1);

			rs.close();
			prep_stmt.close();
		} catch (Exception e) {
			System.err.println("read(): failed to retrieve sequence id " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactory.closeConnection(conn);
		}

		return result;
	}

}

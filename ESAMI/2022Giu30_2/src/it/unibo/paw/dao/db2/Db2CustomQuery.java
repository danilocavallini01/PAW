package it.unibo.paw.dao.db2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import it.unibo.paw.dao.GiocatoreDTO;
import it.unibo.paw.dao.SquadrapallacanestroDTO;

public class Db2CustomQuery {

	public static Map<GiocatoreDTO, String> getAllenatoryByGiocatory() {
		Map<GiocatoreDTO, String> result = new HashMap<GiocatoreDTO, String>();

		Connection conn = Db2DAOFactory.createConnection();

		try {
			PreparedStatement prep_stmt = conn.prepareStatement(Db2GiocatoreDAO.read_all);
			prep_stmt.clearParameters();

			ResultSet rs = prep_stmt.executeQuery();
			while (rs.next()) {
				GiocatoreDTO entry = new GiocatoreDTO();
				entry.setId(rs.getInt(Db2GiocatoreDAO.ID));
				entry.setCodiceFiscale(rs.getString(Db2GiocatoreDAO.CODICEFISCALE));
				entry.setCognome(rs.getString(Db2GiocatoreDAO.COGNOME));
				entry.setNome(rs.getString(Db2GiocatoreDAO.NOME));
				entry.setEta(rs.getInt(Db2GiocatoreDAO.ETA));

				Db2GiocatoreSquadraMappingDAO map = new Db2GiocatoreSquadraMappingDAO();
				entry.setSquadre(map.getSquadreByGiocatore(rs.getInt(Db2GiocatoreDAO.ID)));

				for (SquadrapallacanestroDTO s : entry.getSquadre()) {
					result.put(entry, s.getAllenatore());
				}
			}

			prep_stmt.close();
			rs.close();

		} catch (Exception e) {
			System.err.println("create(): failed to insert entry: " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactory.closeConnection(conn);
		}

		return result;
	}
}

package it.unibo.paw.model;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.ibm.db2.jcc.am.SqlException;

import it.unibo.paw.db.DataSource;
import it.unibo.paw.db.PersistenceException;

public class PrenotazioniRepository {
	
	private DataSource dataSource;
	
    private static final String TABLE_NAME = "PRENOTAZIONE";

	private static final String ID = "id";
    private static final String COGNOME = "cognome";
    private static final String DATA = "data";
    private static final String NUM_PERSONE = "numero_persone";
    private static final String CELLULARE = "cellulare";
    private static final String ID_TAVOLO = "idTavolo";
    
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME 
    + " ("
        + ID + " INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1), "
        + COGNOME + " VARCHAR(20) NOT NULL, "
        + DATA + " DATE NOT NULL, "
        + NUM_PERSONE + " INT NOT NULL, "
        + CELLULARE + " VARCHAR(20) NOT NULL, "
        + ID_TAVOLO + " INT NOT NULL REFERENCES Tavolo, "
        + "PRIMARY KEY(" + ID + "),"
        + "CONSTRAINT PrenotazioneId UNIQUE(" + COGNOME + ", " + DATA + "),"
        + "CONSTRAINT DataTavolo UNIQUE(" + DATA + ", " + ID_TAVOLO + ")"
    + ")";

    private static final String DROP_TABLE = "DROP TABLE " + TABLE_NAME;

    private static final String INSERT = "INSERT INTO " + TABLE_NAME
    + " ("
        + COGNOME + ", "
        + DATA + ", "
        + NUM_PERSONE + ", "
        + CELLULARE + ", "
        + ID_TAVOLO + ", "
    + ") VALUES (?,?,?,?,?)";
    
    public PrenotazioniRepository(int type) {
    	this.dataSource = new DataSource(type);
    }
    
    public void dropAndCreateTables() throws PersistenceException {
    	Connection conn = this.dataSource.getConnection();
    	Statement stmt = null;
    	
    	try {
    		stmt = conn.createStatement();
    		
    		try {
    			stmt.executeUpdate(DROP_TABLE);
    		} catch (SQLException e ) {}
    		
    		stmt.executeUpdate(CREATE_TABLE);
    	} catch (SQLException e) {
    		throw new PersistenceException(e.getMessage());
    	} finally {
    		if ( stmt != null ) {
    			try {
					stmt.close();
				} catch (SQLException e) {
					throw new PersistenceException(e.getMessage());
				}
    		}
    	}
    }
    
    
    public boolean richiestaPrenotazione(String cognome, Date data, int numeroPersone, String cellulare, 
    		TavoloRepository repo) throws PersistenceException {
    	
    	String tavoloDisponibile = repo.disponibilitaTavolo(data, numeroPersone);
    	if ( tavoloDisponibile == null ) {
			return false;
		}
    	Integer idTavoloDisponibile = repo.getIdByNumero(tavoloDisponibile);
    	if ( idTavoloDisponibile == null ) {
    		return false;
    	}
    	
    	Connection conn = this.dataSource.getConnection();
    	PreparedStatement stmt = null;
    	int rowsAffected;
    	
    	try {
    		stmt = conn.prepareStatement(INSERT);
    		stmt.setString(1,cognome);
    		stmt.setDate(2,new java.sql.Date(data.getTime()));
    		stmt.setInt(3, numeroPersone);
    		stmt.setString(4, cellulare);
    		stmt.setInt(5, idTavoloDisponibile);
    		
    		rowsAffected = stmt.executeUpdate();
    	} catch (SQLException e) {
    		throw new PersistenceException(e.getMessage());
    	} finally {
    		try {
    			if ( stmt != null ) {
    				stmt.close();
        		}
				if ( conn != null ) {
					conn.close();
				}
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
    	}
    	
    	if ( rowsAffected != 0 ) {
			return true;
		}
    	
    	return false;
    }
}

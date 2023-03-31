package it.unibo.paw.model;

import java.sql.Connection;

import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import it.unibo.paw.db.DataSource;
import it.unibo.paw.db.PersistenceException;

public class TavoloRepository {
	private DataSource dataSource;
	
    private static final String TABLE_NAME = "TAVOLO";
    
    private static final String ID = "id";
    private static final String NUMERO = "numero";
    private static final String CAPIENZA = "capienza";
    
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME 
    + " ("
        + ID + " INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1), "
        + NUMERO + " VARCHAR(20) NOT NULL UNIQUE, "
        + CAPIENZA + " INT NOT NULL, "
        + "PRIMARY KEY(" + ID + ")"
    + ")";

    private static final String DROP_TABLE = "DROP TABLE " + TABLE_NAME;
    
    private static final String AVAILABLE_TABLE = "SELECT " + NUMERO + " FROM " + TABLE_NAME
    		+ " WHERE capienza >= ? AND " + ID + " NOT IN ( SELECT idTavolo FROM PRENOTAZIONE WHERE data = ? )"; 
    
    private static final String GET_ID_BY_NUMBER = "SELECT " + ID + " FROM " + TABLE_NAME
    		+ " WHERE " + NUMERO + "= ?";
    
    public TavoloRepository(int type) {
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
    }

	public String disponibilitaTavolo(Date data, int numeroPersone) throws PersistenceException {
		Connection conn = this.dataSource.getConnection();
    	PreparedStatement stmt = null;
    	
    	try {
    		stmt = conn.prepareStatement(AVAILABLE_TABLE);

    		stmt.setInt(1, numeroPersone);
    		stmt.setDate(2, new java.sql.Date(data.getTime()));
    		
    		ResultSet result = stmt.executeQuery();
    		
    		if (result.next()) {
    			return result.getString(NUMERO);
    		}
    		
    		return null;
    		
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
	}
	
	public Integer getIdByNumero(String numero) throws PersistenceException {
		Connection conn = this.dataSource.getConnection();
    	PreparedStatement stmt = null;
    	
    	try {
    		stmt = conn.prepareStatement(GET_ID_BY_NUMBER);

    		stmt.setString(1, numero);
    		
    		ResultSet result = stmt.executeQuery();
    		
    		if (result.next()) {
    			return result.getInt(ID);
    		}
    		
    		return null;
    		
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
	}
}

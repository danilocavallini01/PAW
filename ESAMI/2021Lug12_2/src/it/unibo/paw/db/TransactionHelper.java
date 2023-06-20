package it.unibo.paw.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class TransactionHelper {

	public static void setTransaction(Connection conn, int isolationLevel) throws SQLException {

		DatabaseMetaData metadata = conn.getMetaData();

		if (!(metadata.supportsTransactions() && metadata.supportsTransactionIsolationLevel(isolationLevel))) {
			throw new SQLException("Connection doesnt support this type of transaction");
		}

		conn.setAutoCommit(false);
		conn.setTransactionIsolation(isolationLevel);
	}
}

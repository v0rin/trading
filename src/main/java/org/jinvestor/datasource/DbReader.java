package org.jinvestor.datasource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jinvestor.ext.SqlJool;

/**
 *
 * @author Adam
 */
public class DbReader<T> implements IReader<T> {

	private static final Logger LOG = LogManager.getLogger();

	private String dbConnectionString;
	private Properties dbConnectionProperties;
	private String sqlQuery;
	private Function<ResultSet, T> rowToPojoConverter;


	public DbReader(String dbConnectionString,
			 		String sqlQuery,
			 		Function<ResultSet, T> rowToPojoConverter) {
		this(dbConnectionString, new Properties(), sqlQuery, rowToPojoConverter);
	}


	public DbReader(String dbConnectionString,
					Properties dbConnectionProperties,
					String sqlQuery,
					Function<ResultSet, T> rowToPojoConverter) {

		this.dbConnectionString = dbConnectionString;
		this.dbConnectionProperties = dbConnectionProperties;
		this.sqlQuery = sqlQuery;
		this.rowToPojoConverter = rowToPojoConverter;
	}


	private Connection connection;
	@Override
	public Stream<T> stream() throws IOException {
		try {
			connection = DriverManager.getConnection(dbConnectionString, dbConnectionProperties);
			LOG.info("Connection to the database[" + dbConnectionString + "] has been established.");
		}
		catch (SQLException e) {
			throw new IOException(e);
		}

		try {
			return SqlJool.seq(connection.prepareStatement(sqlQuery), rowToPojoConverter).stream();
		}
		catch (SQLException e) {
			throw new IOException("Could not execute sqlQuery=" + sqlQuery, e);
		}
	}


	@Override
	public void close() throws Exception {
		connection.close();
	}
}

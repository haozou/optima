package org.optima.server.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.optima.server.model.Column;

/**
 * JDBC with hive service
 * 
 * @author hzou
 * 
 */
public class HiveService {
	final static String DRIVER_NAME = "org.apache.hive.jdbc.HiveDriver";
	private String host;
	private int port;
	private String dbname;
	private String username;
	private Statement stmt;

	/**
	 * Hive Service Constructor
	 * 
	 * @param host
	 * @param port
	 * @param dbname
	 * @param username
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public HiveService(String host, int port, String dbname, String username) throws SQLException, ClassNotFoundException {
		super();
		this.host = host;
		this.port = port;
		this.dbname = dbname;
		this.username = username;

		Class.forName(DRIVER_NAME);
		String connection = String.format("jdbc:hive2://%s:%d/%s", this.host, this.port, this.dbname);
		Connection con = DriverManager.getConnection(connection, this.username, "");
		this.stmt = con.createStatement();
	}

	/**
	 * Hive Service Constructor with default port number=10000
	 * 
	 * @param host
	 * @param dbname
	 * @param username
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public HiveService(String host, String dbname, String username) throws SQLException, ClassNotFoundException {
		super();
		this.host = host;
		this.port = 10000;
		this.dbname = dbname;
		this.username = username;

		Class.forName(DRIVER_NAME);
		String connection = String.format("jdbc:hive2://%s:%d/%s", this.host, this.port, this.dbname);
		Connection con = DriverManager.getConnection(connection, this.username, "");
		this.stmt = con.createStatement();
	}

	/**
	 * create table with given table name
	 * 
	 * @param tableName
	 * @param dropIfExist
	 * @throws SQLException
	 */
	public boolean createTable(String tableName, boolean dropIfExist, List<Column> columns) throws SQLException {
		String columnString = "";
		for (int i = 0; i < columns.size(); i++) {
			Column c = columns.get(i);
			if (i>0)
				columnString += ",";
			columnString+=c.getColumnName();
			columnString += "  ";
			columnString += c.getColumnType();
		}
		String sql = String.format("create table %s (%s)", tableName, columnString);
		if (dropIfExist) {
			System.out.println("drop table if exists " + tableName);
			stmt.execute("drop table if exists " + tableName);
		}
		System.out.println(sql);
		return this.stmt.execute(sql);
	}

	/**
	 * show all the tables
	 * 
	 * @return
	 * @throws SQLException
	 */
	public ResultSet showTables() throws SQLException {
		String sql = "show tables";
		return this.stmt.executeQuery(sql);
	}

	/**
	 * describe the table with given table name
	 * 
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public ResultSet describleTable(String tableName) throws SQLException {
		String sql = String.format("describe %s", tableName);
		return this.stmt.executeQuery(sql);
	}
	
	public ResultSet getDataFromTable(String tableName) throws SQLException {
		String sql = String.format("select * from  %s ", tableName);
		System.out.println("============"+sql);
		return this.stmt.executeQuery(sql);
	}

	/**
	 * load data
	 * 
	 * @param localPath
	 * @param tableName
	 * @throws SQLException
	 */
	public void loadLocalData(String localPath, String tableName) throws SQLException {
		String sql = "load data local inpath '" + localPath + "' into table " + tableName;
		System.out.println(sql);
		this.stmt.execute(sql);
	}
	
}

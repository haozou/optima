package org.optima.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.optima.server.model.Column;
import org.optima.server.services.HiveService;

/**
 * Example code
 *
 */
public class App {
	public static void main(String[] args) {
		try {
			List<Column> columns = new ArrayList<Column>();
			HiveService hive = new HiveService("localhost", "default", "hzou");
			hive.createTable("poo", true,columns);
			ResultSet res = hive.showTables();
			while (res.next()) {
				System.out.println(res.getString(1));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}

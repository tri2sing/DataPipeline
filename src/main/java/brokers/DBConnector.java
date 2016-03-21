package brokers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import utils.PropertiesLoader;

public class DBConnector {

	private Connection conn;

	public DBConnector(String connectorPropertiesFile) {
		PropertiesLoader loader = new PropertiesLoader(connectorPropertiesFile);
		String jdbcClass = loader.getProperty("jdbc.class");
		// For efficient batch inserts we add the rewriteBatchedStatements flag.
		String dbUrl = loader.getProperty("db.url") + "?rewriteBatchedStatements=true&autoReconnect=true&useSSL=false";
		try {
			Class.forName(jdbcClass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			// NOT FOR PRODUCION USE !!!
			// username/password should come from environment variable or encrypted file
			conn = DriverManager.getConnection(dbUrl, "root", "");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insert(JSONArray records) {
		String cpusql = "INSERT INTO CPU (Host, VM, Timestamp, Type, Value) VALUES(?, ?, ?, ?, ?)";
		String dsksql = "INSERT INTO DISK (Host, VM, Timestamp, Type, Value) VALUES(?, ?, ?, ?, ?)";
		String memsql = "INSERT INTO CPU (Host, VM, Timestamp, Type, Value) VALUES(?, ?, ?, ?, ?)";

		PreparedStatement cpups, dskps, memps;
		try {
			cpups = conn.prepareStatement(cpusql);
			dskps = conn.prepareStatement(dsksql);
			memps = conn.prepareStatement(memsql);
			for (Object record : records) {
				JSONObject obj = (JSONObject) record;
				String metric = (String) obj.get("metric");
				String host = (String) obj.get("host");
				String vm = (String) obj.get("vm");
				String unit = (String) obj.get("unit");
				float value = Float.parseFloat((String) obj.get("value"));
				long epoch = Long.parseLong((String) obj.get("epcoh"));
				Timestamp timestamp = new Timestamp(epoch);
				PreparedStatement ps = null;
				switch (metric) {
				case "cpu":
					ps = cpups;
					break;
				case "disk":
					ps = dskps;
					break;
				case "memory":
					ps = memps;
					break;
				}
				ps.setString(1, host);
				ps.setString(2, vm);
				ps.setTimestamp(3, timestamp);
				ps.setString(4, unit);
				ps.setFloat(5, value);
				ps.addBatch();
			}
			cpups.executeBatch();
			dskps.executeBatch();
			memps.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		DBConnector connector = new DBConnector("dbconnector.properties");
	}
}

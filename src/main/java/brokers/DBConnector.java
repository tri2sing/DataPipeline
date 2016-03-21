package brokers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
		String memsql = "INSERT INTO MEMORY (Host, VM, Timestamp, Type, Value) VALUES(?, ?, ?, ?, ?)";

		PreparedStatement cpups, dskps, memps;
		try {
			cpups = conn.prepareStatement(cpusql);
			dskps = conn.prepareStatement(dsksql);
			memps = conn.prepareStatement(memsql);
			for (Object record : records) {
				JSONObject obj = (JSONObject) record;
				System.out.println(obj);
				String metric = (String) obj.get("metric");
				String host = (String) obj.get("host");
				String vm = (String) obj.get("vm");
				String unit = (String) obj.get("unit");
				long epoch = Long.valueOf((Long) obj.get("epoch"));
				double value = Double.valueOf((Double) obj.get("value"));
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
				ps.setDouble(5, value);
				ps.addBatch();
			}
			cpups.executeBatch();
			dskps.executeBatch();
			memps.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the average of the readings for each VM.
	 * @param metric can be "cpu", "disk", "memory"
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getAverages(String metric) {
		JSONObject averages = new JSONObject();
		
		String sql = "select vm, avg(value) as avg from " + metric + " group by vm order by vm";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String vm = rs.getString("vm");
				float avg = rs.getFloat("avg");
				averages.put(vm, new Float(avg));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return averages;
	}
}

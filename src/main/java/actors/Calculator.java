package actors;

import org.json.simple.JSONObject;

import brokers.DBConnector;
import utils.PropertiesLoader;

public class Calculator {
	private DBConnector dbconnector;
	private float cpuThresholdPercent;
	private float dskThresholdPercent;
	private float memThresholdPercent;

	public Calculator(String calculatorPropertiesFile) {
		PropertiesLoader loader = new PropertiesLoader(calculatorPropertiesFile);
		dbconnector = new DBConnector(loader.getProperty("dbconnector.properties.file"));
		try {
			cpuThresholdPercent = Float.parseFloat(loader.getProperty("cpu.threshold.percent"));
			dskThresholdPercent = Float.parseFloat(loader.getProperty("disk.threshold.percent"));
			memThresholdPercent = Float.parseFloat(loader.getProperty("memory.threshold.percent"));
		} catch (NumberFormatException nfe) {
			System.out.println(nfe.getMessage());
		}
	}
	
	public void calculate() {
		JSONObject cpuAvgs = dbconnector.getAverages("cpu");
		JSONObject dskAvgs = dbconnector.getAverages("disk");
		JSONObject memAvgs = dbconnector.getAverages("memory");
		System.out.println("Number of cpu averages = " + cpuAvgs.size());
		System.out.println("Number of disk averages = " + dskAvgs.size());
		System.out.println("Number of memory averages = " + memAvgs.size());
	}
	
	public static void main(String[] args) {
		Calculator calc = new Calculator("calculator.properties");
		calc.calculate();
	}
}

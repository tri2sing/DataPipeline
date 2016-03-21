package actors;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

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
		HashSet<String> candidates = new HashSet<>();
		JSONObject cpuAvgs = dbconnector.getAverages("cpu");
		JSONObject dskAvgs = dbconnector.getAverages("disk");
		JSONObject memAvgs = dbconnector.getAverages("memory");
		for (Object entry: cpuAvgs.entrySet()) {
			Map.Entry<String, Float> eCast = (Map.Entry<String, Float>) entry;
			String hostvm = eCast.getKey();
			Float cpuVal = eCast.getValue();
			float dskVal = (Float) dskAvgs.get(hostvm);
			float memVal = (Float) memAvgs.get(hostvm);
			if (cpuVal < cpuThresholdPercent && dskVal < dskThresholdPercent && memVal < memThresholdPercent) {
				candidates.add(hostvm);
			}
		}
		if (candidates.size() == 0) {
			System.out.println("There are no reclamantion candidates");
		}
		else {
			System.out.println("The reclamantion candidates are:");
			System.out.println("Host:VM");
			System.out.println("=======");
			Iterator<String> itr = candidates.iterator();
			while(itr.hasNext()) {
				System.out.println(itr.next());
			}
		}
		
	}
	
	public static void main(String[] args) {
		Calculator calc = new Calculator("calculator.properties");
		calc.calculate();
	}
}

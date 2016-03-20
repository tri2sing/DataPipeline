package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class PropertiesLoader {

	Properties properties = null;

	public PropertiesLoader(String propertiesFile) {
		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream(propertiesFile);
		if (inStream == null) {
			System.out.println("Unable to find " + propertiesFile);
			System.exit(1);
		}
		try {
			properties = new Properties();
			properties.load(inStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void printProperties() {
		for(Map.Entry<?, ?> entry: properties.entrySet()) {
			String key = (String) entry.getKey();
			String val = (String) entry.getValue();
			System.out.println(key + ": " + val);
		}
	}
	
	public Properties getProperties() {
		return properties;
	}
	
	public String getProperty(String key) {
		return properties.getProperty(key);
	}
	
}

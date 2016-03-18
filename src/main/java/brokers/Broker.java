package brokers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class Broker {

	Properties props = null;

	public Broker(String propertiesFile) {
		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream(propertiesFile);
		if (inStream == null) {
			System.out.println("Unable to find " + propertiesFile);
			System.exit(1);
		}
		try {
			props = new Properties();
			props.load(inStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void printProperties() {
		for(Map.Entry<?, ?> entry: props.entrySet()) {
			String key = (String) entry.getKey();
			String val = (String) entry.getValue();
			System.out.println(key + ": " + val);
		}
	}
	
	
}

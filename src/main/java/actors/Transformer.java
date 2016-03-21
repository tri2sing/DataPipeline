package actors;

import org.json.simple.JSONArray;

import utils.PropertiesLoader;
import brokers.DBConnector;
import brokers.Subscriber;

public class Transformer {

	private Subscriber subscriber;
	private DBConnector dbconnector;

	public Transformer(String transformerPropsFile) {
		PropertiesLoader loader = new PropertiesLoader(transformerPropsFile);
		subscriber = new Subscriber(loader.getProperty("subscriber.properties.file"), loader.getProperty("consumer.subscribe.topic"));
		dbconnector = new DBConnector(loader.getProperty("dbconnector.properties.file"));
	}

	public void transform() {
		while (true) {
			JSONArray records = subscriber.receive();
			if (records == null) {
				continue;
			}
			System.out.println("Received records = " + records.size());
			dbconnector.insert(records);
		}
	}
	
	public static void main(String[] args) {
		Transformer transformer = new Transformer("transformer.properties");
		transformer.transform();
	}

}

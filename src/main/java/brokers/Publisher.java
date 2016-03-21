package brokers;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.simple.JSONObject;

import utils.PropertiesLoader;

public class Publisher {
	
	private KafkaProducer<String, String> sender;
	
	public Publisher(String propertiesFile) {
        PropertiesLoader loader = new PropertiesLoader(propertiesFile);
		sender = new KafkaProducer<String, String>(loader.getProperties());
	}

	public void send(String topic, JSONObject message ) {
		sender.send(new ProducerRecord<String, String>(topic, message.toString()));
	}
	
	public void send(String topic, String message) {
		sender.send(new ProducerRecord<String, String>(topic, message));
	}
	
}

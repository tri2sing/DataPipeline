package brokers;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.simple.JSONObject;

public class Sender extends Broker{
	
	private KafkaProducer<String, String> sender;
	
	public Sender() {
		this("sender.properties");
	}
	
	public Sender(String propertiesFile) {
		super(propertiesFile);
		sender = new KafkaProducer<String, String>(properties);
	}

	public void send(String topic, JSONObject message ) {
		sender.send(new ProducerRecord<String, String>(topic, message.toString()));
	}
	
	public void send(String topic, String message) {
		sender.send(new ProducerRecord<String, String>(topic, message));
	}
	
	public static void main(String[] args) {
		Sender s = new Sender();
		s.printProperties();
		for (int i = 0; i < 100; i++) {
			JSONObject obj = new JSONObject();
			obj.put("id", new Integer(i));
			s.send("metrics", obj);
		}
	}
}

package brokers;

import java.util.Arrays;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import utils.PropertiesLoader;

public class Subscriber {
	
	private static final long POLL_TIMEOUT = 5000; // Milliseonds to poll for data.
	private KafkaConsumer<String, String> receiver;
	
    public Subscriber(String propertiesFile, String topic) {
        PropertiesLoader loader = new PropertiesLoader(propertiesFile);
        receiver = new KafkaConsumer<>(loader.getProperties());
        receiver.subscribe(Arrays.asList(topic));
    }
    
    @SuppressWarnings("unchecked")
	public JSONArray receive() {
    	ConsumerRecords<String, String> records = receiver.poll(POLL_TIMEOUT);
    	if (records.isEmpty()) {
    		return null;
    	}
    	JSONParser parser = new JSONParser();
    	JSONArray results = new JSONArray();
    	for (ConsumerRecord<String, String> record: records) {
    			// System.out.println(record.value());
    			try {
					results.add((JSONObject) parser.parse(record.value()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
    	}
    	return results;
    }

}

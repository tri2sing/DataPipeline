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
	
	private KafkaConsumer<String, String> receiver;
	
    public Subscriber(String propertiesFile) {
        PropertiesLoader loader = new PropertiesLoader(propertiesFile);
        receiver = new KafkaConsumer<>(loader.getProperties());
    }
    
    @SuppressWarnings("unchecked")
	public JSONArray receive(String topic) {
        receiver.subscribe(Arrays.asList(topic));
    	ConsumerRecords<String, String> records = receiver.poll(1000);
    	if (records.isEmpty()) {
    		return null;
    	}
    	JSONParser parser = new JSONParser();
    	JSONArray results = new JSONArray();
    	for (ConsumerRecord<String, String> record: records) {
    			System.out.println(record.value());
    			try {
					results.add((JSONObject) parser.parse(record.value()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
    	}
    	return results;
    }

}

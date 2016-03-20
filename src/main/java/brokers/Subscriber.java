package brokers;

import java.util.Arrays;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import utils.PropertiesLoader;

public class Subscriber {
	
	private KafkaConsumer<String, String> receiver;
	
    public Subscriber() {
        this("subscriberer.properties");
    }

    public Subscriber(String propertiesFile) {
        PropertiesLoader loader = new PropertiesLoader(propertiesFile);
        receiver = new KafkaConsumer<>(loader.getProperties());
    }
    
    public void receive(String topic) {
        receiver.subscribe(Arrays.asList(topic));
    	while(true) {
    		ConsumerRecords<String, String> records = receiver.poll(1000);
    		for (ConsumerRecord<String, String> record: records) {
    			System.out.printf("offset = %d, value = %s\n", record.offset(), record.value());
    		}
    	}
    }

    public static void main(String[] args) {
        Subscriber receiver = new Subscriber();
        receiver.receive("metrics");
        
    }
}

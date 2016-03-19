package brokers;

import java.util.Arrays;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class Receiver extends Broker{
	
	private KafkaConsumer<String, String> receiver;
	
    public Receiver() {
        this("receiver.properties");
    }

    public Receiver(String propertiesFile) {
        super(propertiesFile);
        receiver = new KafkaConsumer<>(properties);
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
        Receiver receiver = new Receiver();
        receiver.printProperties();
        receiver.receive("metrics");
        
    }
}

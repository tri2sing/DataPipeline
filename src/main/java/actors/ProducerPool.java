package actors;

import utils.PropertiesLoader;

public class ProducerPool {

	private Thread[] producer;
	private int numProducers;
	private int numMinutes;
	private String publisherTopic;
	private String pubPropsFile;

	public ProducerPool() {
		this("producerpool.properties");
	}
	
	public ProducerPool(String propertiesFile) {
        PropertiesLoader loader = new PropertiesLoader(propertiesFile);
        this.publisherTopic = loader.getProperty("producer.publish.topic");
        this.pubPropsFile = loader.getProperty("publisher.properties.file");
        try {
        this.numProducers = Integer.parseInt(loader.getProperty("producers.count"));
        this.numMinutes = Integer.parseInt(loader.getProperty("producer.duration.minutes"));
        }
        catch (NumberFormatException nfe) {
        	System.out.println(nfe.getMessage());
        }
        instantiateProducers();
	}
	
	public ProducerPool(int numProducers, int numMinutes, String publisherTopic, String publisherPropertiesFile) {
		this.numProducers = numProducers;
		this.numMinutes = numMinutes;
		this.publisherTopic = publisherTopic;
		this.pubPropsFile = publisherPropertiesFile;
		instantiateProducers();
	}

	private void instantiateProducers() {
		producer = new Thread[numProducers];
		for (int i = 0; i < numProducers; i++) {
			producer[i] = new Thread(new Producer(numMinutes, publisherTopic, pubPropsFile));
		}
	}
	
	public void emulate() {
		for (int i = 0; i < numProducers; i++) {
			producer[i].start();
		}
		try {
			for (int i = 0; i < numProducers; i++) {
				producer[i].join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();	
		}
	}
	
	public static void main(String [] args) {
		ProducerPool pool = new ProducerPool();
		pool.emulate();
	}
}

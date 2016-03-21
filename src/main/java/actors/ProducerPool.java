package actors;

import utils.PropertiesLoader;

public class ProducerPool {

	private Thread[] producer;
	private int numProducers;
	private int numIterations;
	private String publisherTopic;
	private String pubPropsFile;

	public ProducerPool(String propertiesFile) {
		PropertiesLoader loader = new PropertiesLoader(propertiesFile);
		this.publisherTopic = loader.getProperty("producer.publish.topic");
		this.pubPropsFile = loader.getProperty("publisher.properties.file");
		try {
			this.numProducers = Integer.parseInt(loader.getProperty("producers.count"));
			this.numIterations = Integer.parseInt(loader.getProperty("producer.iterations"));
		} catch (NumberFormatException nfe) {
			System.out.println(nfe.getMessage());
		}
		instantiateProducers();
	}

	public ProducerPool(int numProducers, int numIterations, String publisherTopic, String publisherPropertiesFile) {
		this.numProducers = numProducers;
		this.numIterations = numIterations;
		this.publisherTopic = publisherTopic;
		this.pubPropsFile = publisherPropertiesFile;
		instantiateProducers();
	}

	private void instantiateProducers() {
		producer = new Thread[numProducers];
		for (int i = 0; i < numProducers; i++) {
			producer[i] = new Thread(new Producer(numIterations, publisherTopic, pubPropsFile));
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

	public static void main(String[] args) {
		ProducerPool pool = new ProducerPool("producerpool.properties");
		pool.emulate();
	}
}

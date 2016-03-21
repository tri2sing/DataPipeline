package actors;

import java.lang.Runnable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import org.json.simple.JSONObject;

import brokers.Publisher;

public class Producer implements Runnable {

	private static final float PERCENT_MULTIPLIER = 100.0f;

	private int numIterations;
	private int sleepSecs;
	private String publisherTopic;
	private String host;
	private String vm;
	private Random random;
	private Publisher publisher;

	public Producer(int numIterations, int sleepSecs, String publisherTopic, String publisherPropertiesFile) {
		this.numIterations = numIterations;
		this.sleepSecs = sleepSecs;
		this.publisherTopic = publisherTopic;
		this.random = new Random();
		publisher = new Publisher(publisherPropertiesFile);
		try {
			this.host = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			this.host = "unknown";
		}
	}

	@Override
	public void run() {
		long id = Thread.currentThread().getId();
		this.vm = Long.toString(id);
		for (int i = 0; i < numIterations; i++) {
			JSONObject cpu = createMetric("cpu");
			JSONObject dsk = createMetric("disk");
			JSONObject mem = createMetric("memory");
			publisher.send(publisherTopic, cpu);
			publisher.send(publisherTopic, dsk);
			publisher.send(publisherTopic, mem);
			// Sleep for a minute to emulate once a minute metrics generation.
			// Skip the sleep after the last metric generation.
			if (i < (numIterations - 1)) {
				try {
					Thread.sleep(sleepSecs * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.printf("Host = %s, VM = %s, Iteration = %d\n", host, vm, i);
		}
	}

	/***
	 * Generate a random utilization metric in range [0%, 100%) 
	 * 0% is inclusive and 100% is exclusive due to use of default random generator.
	 * 
	 * @param metric can be "cpu", "disk", "memory", etc.
	 * @return a JSON object with corresponding metric.
	 */
	// JSONObject is built using HashMap without parameterizing <k, v> correctly.
	// Hence we put in the suppress warning annotation when using the put method.
	@SuppressWarnings("unchecked")
	private JSONObject createMetric(String metric) {
		JSONObject obj = new JSONObject();
		obj.put("metric", metric);
		obj.put("host", this.host);
		obj.put("vm", this.vm);
		obj.put("unit", "percent");
		obj.put("value", new Float(random.nextFloat() * PERCENT_MULTIPLIER));
		obj.put("epoch", new Long(System.currentTimeMillis())); // Time since Epoch.
		return obj;
	}

}

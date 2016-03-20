package actors;

import java.lang.Runnable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import org.json.simple.JSONObject;

public class Producer implements Runnable {

	private static final float PERCENT_MULTIPLIER = 100.0f;
	private static final int DEFAULT_MINUTES = 5; // Default time to run the producer
	private static final String DEFAULT_TOPIC = "metrics";
	private static final int INTIAL_SLEEP_MILLIS = 15000; // Initial sleep to randomize thread start times.
	private static final int INTER_SAMPLE_SLEEP_MILLIS = 60000; // Milliseconds to sleep between each measurement

	private int numMinutes;
	private String host;
	private String vm;
	private Random random;

	public Producer() {
		this(DEFAULT_MINUTES, DEFAULT_TOPIC);
	}

	public Producer(int numMinutes, String topic) {
		this.numMinutes = numMinutes;
		random = new Random();
		try {
			host = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			host = "unknown";
		}
	}

	@Override
	public void run() {
		long id = Thread.currentThread().getId();
		vm = Long.toString(id);
		int millis = random.nextInt(INTIAL_SLEEP_MILLIS + 1);
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < numMinutes; i++) {
			JSONObject cpu = createMetric("cpu");
			JSONObject dsk = createMetric("disk");
			JSONObject mem = createMetric("memory");
			System.out.println(cpu.toString());
			// Sleep for a minute to emulate once a minute metrics generation.
			// Skip the sleep after the last metric generation.
			if (i < (numMinutes - 1)) {
				try {
					Thread.sleep(INTER_SAMPLE_SLEEP_MILLIS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/***
	 * Generate a random utilization metric in range [0%, 100%) 0% is inclusivee and 100% is exclusive due to use of default
	 * random generator.
	 * 
	 * @param object
	 *            can be "cpu", "disk", "memory", etc.
	 * @return a JSON object with corresponding metric.
	 */
	// JSONObject is built using HashMap without parameterizing <k, v> correctly.
	// Hence we put in the suppress warning annotation when using the put method.
	@SuppressWarnings("unchecked")
	private JSONObject createMetric(String object) {
		JSONObject obj = new JSONObject();
		obj.put("host", this.host);
		obj.put("vm", this.vm);
		obj.put("object", object);
		obj.put("type", "percentutilization");
		obj.put("value", new Float(random.nextFloat() * PERCENT_MULTIPLIER));
		obj.put("timestamp", System.currentTimeMillis()); // Time since Epoch.
		return obj;
	}

}

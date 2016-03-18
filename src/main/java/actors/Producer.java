package actors;

import java.lang.Runnable;
import java.util.Random;

public class Producer implements Runnable {

	private static final int SLEEP_UPPER_BOUND = 5000;  // Sleep up to these milliseconds.

	@Override
	public void run() {
		Random random = new Random();
		int millis = random.nextInt(SLEEP_UPPER_BOUND + 1);
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long id = Thread.currentThread().getId();
		System.out.println("Hello from thread: " + id);
	}

}

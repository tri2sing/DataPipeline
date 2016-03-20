package actors;

public class ProducerPool {

	private Thread[] producer;
	private int numProducers;
	int numMinutes;

	public ProducerPool(int numProducers, int numMinutes) {

		this.numProducers = numProducers;
		this.numMinutes = numMinutes;
		producer = new Thread[numProducers];
		for (int i = 0; i < numProducers; i++) {
			producer[i] = new Thread(new Producer(numMinutes));
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
		ProducerPool pool = new ProducerPool(5, 5);
		pool.emulate();
	}
}

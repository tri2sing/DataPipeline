package actors;

public class ProducerPool {

	private Thread[] producerThread;
	private int numThreads;

	public ProducerPool(int numProducers) {

		producerThread = new Thread[numProducers];
		for (int i = 0; i < numProducers; i++) {
			numThreads = numProducers;
			producerThread[i] = new Thread(new Producer());
		}
	}

	public void emulate() {
		for (int i = 0; i < numThreads; i++) {
			producerThread[i].start();
		}
		try {
			for (int i = 0; i < numThreads; i++) {
				producerThread[i].join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String [] args) {
		ProducerPool pool = new ProducerPool(10);
		pool.emulate();
	}
}

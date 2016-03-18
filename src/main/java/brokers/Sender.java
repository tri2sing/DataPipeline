package brokers;

public class Sender extends Broker{
	
	public Sender() {
		this("sender.properties");
	}
	
	public Sender(String fileName) {
		super(fileName);
	}
	
	public static void main(String[] args) {
		Sender s = new Sender();
		s.printProperties();
	}
}

package brokers;

public class Receiver extends Broker{
    public Receiver() {
        this("receiver.properties");
    }

    public Receiver(String fileName) {
        super(fileName);
    }

    public static void main(String[] args) {
        Receiver r = new Receiver();
        r.printProperties();
    }
}

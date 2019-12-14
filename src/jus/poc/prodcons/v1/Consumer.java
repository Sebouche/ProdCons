package jus.poc.prodcons.v1;

import java.util.Random;

public class Consumer extends Thread {
	int consTime;

	long id;

	ProdConsBuffer buffer;
	
	Random rand = new Random();

	public Consumer(int consTime, ProdConsBuffer buf) {
		this.consTime = consTime;
		id = getId();
		buffer = buf;
	}

	public Message cons() {
		// on crée un temps de consommation aléatoire de moyenne consTime et compris entre
		// consTime - consTime/2 et consTime + consTime/2 (le /2 est arbitraire)
		int diff;
		if (rand.nextBoolean()) {
			diff = (-rand.nextInt(consTime / 2));
		} else {
			diff = rand.nextInt(consTime / 2);
		}
		long beginCons = System.currentTimeMillis();
		while (beginCons > System.currentTimeMillis() - consTime + diff)
			;

		return buffer.get();
	}

	public void run() {
		System.out.println("Consumer thread " + id + " started");
		while (true) {
			System.out.println(" Consumer thread " + id + ": read " + cons().content());
		}
	}

}

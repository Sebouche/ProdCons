package jus.poc.prodcons.v1;

import java.util.Random;

public class Consumer extends Thread {
	int consTime, consTimeVariation;

	long id;

	ProdConsBuffer buffer;
	
	Random rand = new Random();

	public Consumer(int consTime, int consTimeVariation, ProdConsBuffer buf) {
		this.consTime = consTime;
		this.consTimeVariation = consTimeVariation;
		id = getId();
		buffer = buf;
	}

	public Message cons() {
		// on crée un temps de consommation aléatoire de moyenne consTime et compris entre
		// consTime - consTimeVariation et consTime + consTimeVariation
		int diff;
		if (rand.nextBoolean()) {
			diff = (-rand.nextInt(consTimeVariation));
		} else {
			diff = rand.nextInt(consTimeVariation);
		}
		long beginCons = System.currentTimeMillis();
		while (beginCons > System.currentTimeMillis() - consTime + diff)
			;

		Message mes;
		try {
			mes = buffer.get();
		} catch (InterruptedException e) {
			mes = new Message("InterruptedException in get method");
		}
		return mes;
	}

	public void run() {
		System.out.println("Consumer thread " + id + " started");
		while (buffer.nbProd > 0 || buffer.nmsg() > 0) {
			System.out.println(" Consumer thread " + id + ": read " + cons().content());
		}
	}

}

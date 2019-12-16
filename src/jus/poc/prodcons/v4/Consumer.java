package jus.poc.prodcons.v4;

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

		return buffer.get();
	}
	
	public Message[] consn(int n) {
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

		return buffer.getn(n);
	}

	public void run() {
		System.out.println("Consumer thread " + id + " started");
		while (buffer.nbProd > 0 || buffer.nmsg() > 0) {
			int n = 3;
			int i;
			Message[] res = consn(n);
			for (i = 0; i < n; i++) {
				System.out.println(" Consumer thread " + id + ": read " + res[i].content());
			}
			
		}
	}

}

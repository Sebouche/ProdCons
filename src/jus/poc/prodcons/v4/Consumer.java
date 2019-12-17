package jus.poc.prodcons.v4;

import java.util.Random;

public class Consumer extends Thread {
	boolean print;

	int consTime, consTimeVariation;

	long id;

	ProdConsBuffer buffer;

	Random rand = new Random();

	public Consumer(int consTime, int consTimeVariation, ProdConsBuffer buf, boolean print) {
		this.consTime = consTime;
		this.consTimeVariation = consTimeVariation;
		id = getId();
		buffer = buf;
		this.print = print;
	}

	public Message cons() {
		// on crée un temps de consommation aléatoire de moyenne consTime et compris
		// entre
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
		// on crée un temps de consommation aléatoire de moyenne consTime et compris
		// entre
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
		if (print)
			System.out.println("Consumer thread " + id + " started");
		Message[] res;
		int i, n;
		while (buffer.nbProd > 0 || buffer.nmsg() > 0) {
			n = 3;
			res = consn(n);
			for (i = 0; i < n; i++) {
				if (print)
					System.out.println(" Consumer thread " + id + ": read " + res[i].content());
			}

		}
	}

}

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

	public Message cons() throws InterruptedException {
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

	public Message[] cons(int n) throws InterruptedException {
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
		String res = "";
		if (print)
			System.out.println("Consumer thread " + id + " started");
		while (buffer.nbProd > 0 || buffer.nmsg() > 0) {
			try {
				res = cons().content();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			if (print)
					System.out.println(" Consumer thread " + id + ": read " + res);
		}
	}

}

package jus.poc.prodcons.v2;

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

		Message mes;
		try {
			mes = buffer.get();
		} catch (InterruptedException e) {
			mes = new Message("InterruptedException in get method");
		}
		return mes;
	}

	public void run() {
		String mes;
		if (print)
			System.out.println("Consumer thread " + id + " started");
		while (buffer.nbProd > 0 || buffer.nmsg() > 0) {
			mes = cons().content();
			if (print)
				System.out.println(" Consumer thread " + id + ": read " + mes);
		}
	}

}

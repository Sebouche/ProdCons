package jus.poc.prodcons.v2;

import java.util.Random;

public class Producer extends Thread {
	int nbMessage = 0;
	int nbMessageMin, nbMessageMax, prodTime;

	long id;

	ProdConsBuffer buffer;

	Random rand = new Random();

	public Producer(int nbMessageMin, int nbMessageMax, int prodTime, ProdConsBuffer buf) {
		this.nbMessageMin = nbMessageMin;
		this.nbMessageMax = nbMessageMax;
		this.prodTime = prodTime;
		id = getId();
		buffer = buf;
	}

	public void prod(String s) {
		nbMessage++;

		// on crée un temps de production aléatoire de moyenne prodTime et compris entre
		// prodTime - prodTime/2 et prodTime + prodTime/2 (le /2 est arbitraire)
		int diff;
		if (rand.nextBoolean()) {
			diff = (-rand.nextInt(prodTime / 2));
		} else {
			diff = rand.nextInt(prodTime / 2);
		}
		long beginProd = System.currentTimeMillis();
		while (beginProd > System.currentTimeMillis() - prodTime + diff)
			;

		try {
			buffer.put(new Message(s));
		} catch (InterruptedException e) {
			System.out.println("InterruptedException in put method");
		}
	}

	public void run() {
		System.out.println("Producer thread " + id + " started");
		int nbMes = rand.nextInt(nbMessageMax - nbMessageMin + 1) + nbMessageMin;
		int i;
		for (i = 0; i < nbMes; i++) {
			prod("Producer thread " + id + ": message nb " + (nbMessage + 1));
		}
	}
}

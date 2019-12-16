package jus.poc.prodcons.v2;

import java.util.Random;

public class Producer extends Thread {
	int nbMessage = 0;
	int nbMessageMin, nbMessageMax, prodTime, prodTimeVariation;

	long id;

	ProdConsBuffer buffer;

	Random rand = new Random();

	public Producer(int nbMessageMin, int nbMessageMax, int prodTime, int prodTimeVariation, ProdConsBuffer buf) {
		this.nbMessageMin = nbMessageMin;
		this.nbMessageMax = nbMessageMax;
		this.prodTime = prodTime;
		this.prodTimeVariation = prodTimeVariation;
		id = getId();
		buffer = buf;
	}

	public void prod(String s) {
		nbMessage++;

		// on crée un temps de production aléatoire de moyenne prodTime et compris entre
		// prodTime - prodTimeVariation et prodTime + prodTimeVariation
		int diff;
		if (rand.nextBoolean()) {
			diff = (-rand.nextInt(prodTimeVariation));
		} else {
			diff = rand.nextInt(prodTimeVariation);
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
		
		buffer.mutexIn.acquireUninterruptibly();
		buffer.nbProd--;
		if (buffer.nbProd == 0) {
			buffer.notEmpty.release(buffer.buffer_size);
		}
		buffer.mutexIn.release();
	}
}

package jus.poc.prodcons.v4;

import java.util.Random;

public class Producer extends Thread {
	boolean print;

	int nbMessage = 0;
	int nbMessageMin, nbMessageMax, prodTime, prodTimeVariation;

	long id;

	ProdConsBuffer buffer;

	Random rand = new Random();

	public Producer(int nbMessageMin, int nbMessageMax, int prodTime, int prodTimeVariation, ProdConsBuffer buf,
			boolean print) {
		this.nbMessageMin = nbMessageMin;
		this.nbMessageMax = nbMessageMax;
		this.prodTime = prodTime;
		this.prodTimeVariation = prodTimeVariation;
		id = getId();
		buffer = buf;
		this.print = print;
	}

	public void prod(String s) throws InterruptedException {
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

		buffer.put(new Message(s));
	}
	
	public void prod(String s, int n) throws InterruptedException {
		nbMessage += n;
		
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

		buffer.put(new SyncMessage(s, n));
	}

	public void run() {
		if (print)
			System.out.println("Producer thread " + id + " started");
		int nbMes = rand.nextInt(nbMessageMax - nbMessageMin + 1) + nbMessageMin;
		int i, n;
		
		n = 5;
				
		for (i = 0; i < nbMes; i += n) {
			try {
				prod("Producer thread " + id + ": message nb " + (nbMessage + 1), n);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		buffer.mutexIn.acquireUninterruptibly();
		buffer.nbProd--;
		if (buffer.nbProd == 0) {
			buffer.notEmpty.release(buffer.buffer_size);
		}
		buffer.mutexIn.release();
	}
}

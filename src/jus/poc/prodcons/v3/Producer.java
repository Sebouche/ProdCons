package jus.poc.prodcons.v3;

import java.util.Random;

public class Producer extends Thread {
	boolean print;

	int nbMessage = 0;
	int nbMessageMin, nbMessageMax, prodTime;

	long id;

	ProdConsBuffer buffer;

	Random rand = new Random();

	public Producer(int nbMessageMin, int nbMessageMax, int prodTime, ProdConsBuffer buf,
			boolean print) {
		this.nbMessageMin = nbMessageMin;
		this.nbMessageMax = nbMessageMax;
		this.prodTime = prodTime;
		id = getId();
		buffer = buf;
		this.print = print;
	}

	public void prod(String s) {
		nbMessage++;

		long beginProd = System.currentTimeMillis();
		while (beginProd > System.currentTimeMillis() - prodTime)
			;

		buffer.put(new Message(s));
	}

	public void run() {
		if (print)
			System.out.println("Producer thread " + id + " started");
		int nbMes = rand.nextInt(nbMessageMax - nbMessageMin + 1) + nbMessageMin;
		int i;
		for (i = 0; i < nbMes; i++) {
			prod("Producer thread " + id + ": message nb " + (nbMessage + 1));
		}
		synchronized (buffer) {
			buffer.nbProd--;
			buffer.notifyAll();
		}
	}
}

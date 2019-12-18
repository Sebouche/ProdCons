package jus.poc.prodcons.v2;

import java.util.Random;

public class Producer extends Thread {
	boolean print;
	
	int nbMessage = 0;
	int nbMessageMin, nbMessageMax, prodTime;

	long id;

	ProdConsBuffer buffer;

	Random rand = new Random();

	public Producer(int nbMessageMin, int nbMessageMax, int prodTime, ProdConsBuffer buf, boolean print) {
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

		try {
			buffer.put(new Message(s));
		} catch (InterruptedException e) {
			System.out.println("InterruptedException in put method");
		}
	}

	public void run() {
		if (print)
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

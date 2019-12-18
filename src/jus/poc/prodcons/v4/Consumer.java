package jus.poc.prodcons.v4;

import java.util.Random;

public class Consumer extends Thread {
	boolean print;

	int consTime;

	long id;

	ProdConsBuffer buffer;

	Random rand = new Random();

	public Consumer(int consTime, ProdConsBuffer buf, boolean print) {
		this.consTime = consTime;
		id = getId();
		buffer = buf;
		this.print = print;
	}

	public Message cons() throws InterruptedException {

		long beginCons = System.currentTimeMillis();
		while (beginCons > System.currentTimeMillis() - consTime)
			;

		return buffer.get();
	}

	public Message[] cons(int n) throws InterruptedException {

		long beginCons = System.currentTimeMillis();
		while (beginCons > System.currentTimeMillis() - consTime)
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

package jus.poc.prodcons.v3;

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

	public Message cons() {

		long beginCons = System.currentTimeMillis();
		while (beginCons > System.currentTimeMillis() - consTime)
			;

		return buffer.get();
	}

	public Message[] consn(int n) {

		long beginCons = System.currentTimeMillis();
		while (beginCons > System.currentTimeMillis() - consTime)
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

package jus.poc.prodcons.v1;

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

package jus.poc.prodcons.v4;

import java.util.concurrent.Semaphore;

public class SyncMessage extends Message {
	int n, ninit;
	
	Semaphore waiting = new Semaphore(0);

	public SyncMessage(String s, int nb) {
		super(s);
		n = nb;
		ninit = nb;
	}
	
	public int n() {
		return n;
	}
	
	public synchronized void decr() {
		n--;
		if (n == 0) {
			waiting.release(ninit + 1);
		}
	}
	
	public void waiting() throws InterruptedException {
		waiting.acquire();
	}

}

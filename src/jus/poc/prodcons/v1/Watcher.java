package jus.poc.prodcons.v1;

public class Watcher extends Thread {
	Producer[] p;
	boolean endProd = false;

	public boolean endProd() {
		return endProd;
	}

	public void setWatch(Producer[] p) {
		this.p = p;
	}

	// ce thread va attendre la fin de tous les producteurs, puis va indiquer que la
	// production est finie
	public void run() {
		int i;
		for (i = 0; i < p.length; i++) {
			try {
				p[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		endProd = true;
	}

}

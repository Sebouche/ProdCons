package jus.poc.prodcons.v2;

public class Watcher extends Thread {
	Producer[] p;
	ProdConsBuffer buffer;

	boolean endProd = false;

	public boolean endProd() {
		return endProd;
	}

	public void setWatch(Producer[] p) {
		this.p = p;
	}

	public void setBuffer(ProdConsBuffer buf) {
		buffer = buf;
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

		// on se bloque jusqu'à ce que le buffer soit vide. Comme tous les producteurs
		// sont arrêtés à ce moment là, cela signifie qu'il faut arrêter les consommateurs
		buffer.notFull.acquireUninterruptibly(buffer.buffer_size);

		endProd = true;
		buffer.notEmpty.release();
	}

}

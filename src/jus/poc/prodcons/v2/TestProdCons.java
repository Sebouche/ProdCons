package jus.poc.prodcons.v2;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;

public class TestProdCons {

	public static void main(String[] args) {

		Properties properties = new Properties();
		try {
			properties.loadFromXML(TestProdCons.class.getResourceAsStream("/jus/poc/prodcons/options.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int nProd = Integer.parseInt(properties.getProperty("nProd"));
		int nCons = Integer.parseInt(properties.getProperty("nCons"));
		int bufSz = Integer.parseInt(properties.getProperty("bufSz"));
		int prodTime = Integer.parseInt(properties.getProperty("prodTime"));
		int consTime = Integer.parseInt(properties.getProperty("consTime"));
		int minProd = Integer.parseInt(properties.getProperty("minProd"));
		int maxProd = Integer.parseInt(properties.getProperty("maxProd"));

		// un watcher, qui permettra aux consommateur de savoir quand la production est
		// finie
		Watcher watcher = new Watcher();

		// on crée le buffer
		ProdConsBuffer buffer = new ProdConsBuffer(bufSz, watcher);

		// et nos producteur / consommateur
		int i;
		Producer[] p = new Producer[nProd];
		for (i = 0; i < nProd; i++) {
			p[i] = new Producer(minProd, maxProd, prodTime, buffer);
		}

		watcher.setWatch(p);
		watcher.setBuffer(buffer);

		Consumer[] c = new Consumer[nCons];
		for (i = 0; i < nCons; i++) {
			c[i] = new Consumer(consTime, buffer, watcher);
		}

		// on les démarre dans un ordre aléatoire
		Random rand = new Random();
		int nProdStarted = 0;
		int nConsStarted = 0;
		boolean n;
		long beg = System.currentTimeMillis();
		while (nProdStarted + nConsStarted < nProd + nCons) {
			n = rand.nextBoolean();
			if (nProdStarted < nProd && n) {
				p[nProdStarted].start();
				nProdStarted++;
			} else if (nConsStarted < nCons) {
				c[nConsStarted].start();
				nConsStarted++;
			}
		}

		watcher.start();

		// on attend la fin des consommateurs
		for (i = 0; i < c.length; i++) {
			try {
				c[i].join();
			} catch (InterruptedException e) {
			}
		}

		long end = System.currentTimeMillis();
		System.out.println("Efficiency: " + (float) buffer.totmsg() / (end - beg) + " messages per millisecond");

	}

}

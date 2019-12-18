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
		int prodTimeVariation = Integer.parseInt(properties.getProperty("prodTimeVariation"));
		int consTime = Integer.parseInt(properties.getProperty("consTime"));
		int consTimeVariation = Integer.parseInt(properties.getProperty("consTimeVariation"));
		int minProd = Integer.parseInt(properties.getProperty("minProd"));
		int maxProd = Integer.parseInt(properties.getProperty("maxProd"));

		if (prodTime < prodTimeVariation) {
			System.out.println("prodTimeVariation too great");
			prodTimeVariation = prodTime / 2;
		}
		if (prodTimeVariation <= 0) {
			prodTimeVariation = 1;
		}
		if (consTime < consTimeVariation) {
			System.out.println("consTimeVariation too great");
			consTimeVariation = consTime / 2;
		}
		if (consTimeVariation <= 0) {
			consTimeVariation = 1;
		}

		boolean print, n;
		int nbTimes, maxNbTimes, i, nProdStarted, nConsStarted;
		long beg, end;
		float eff, sumEff;

		ProdConsBuffer buffer;
		Producer[] p;
		Consumer[] c;

		Random rand = new Random();

		// on décide si on veut afficher ou non les messages
		print = true;

		sumEff = 0;
		
		maxNbTimes = 1;
		for (nbTimes = 0; nbTimes < maxNbTimes; nbTimes++) {
			// on crée le buffer
			buffer = new ProdConsBuffer(bufSz, nProd);

			// et nos producteur / consommateur
			p = new Producer[nProd];
			for (i = 0; i < nProd; i++) {
				p[i] = new Producer(minProd, maxProd, prodTime, prodTimeVariation, buffer, print);
			}

			c = new Consumer[nCons];
			for (i = 0; i < nCons; i++) {
				c[i] = new Consumer(consTime, consTimeVariation, buffer, print);
			}

			// on les démarre dans un ordre aléatoire
			nProdStarted = 0;
			nConsStarted = 0;
			beg = System.currentTimeMillis();
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

			// on attend la fin des consommateurs
			for (i = 0; i < c.length; i++) {
				try {
					c[i].join();
				} catch (InterruptedException e) {
				}
			}

			end = System.currentTimeMillis();
			eff = (float) buffer.totmsg() / (end - beg);
			sumEff += eff;
			System.out.println("Efficiency: " + eff + " messages per millisecond");
		}
		System.out.println("Average Efficiency: " + (float) sumEff/maxNbTimes  + " messages per millisecond");
	}

}

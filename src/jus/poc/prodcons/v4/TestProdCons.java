package jus.poc.prodcons.v4;

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
			prodTimeVariation = prodTime/2;
		}
		if (prodTimeVariation <= 0) {
			prodTimeVariation = 1;
		}
		if (consTime < consTimeVariation) {
			System.out.println("consTimeVariation too great");
			consTimeVariation = consTime/2;
		}
		if (consTimeVariation <= 0) {
			consTimeVariation = 1;
		}

		// on crée le buffer
		ProdConsBuffer buffer = new ProdConsBuffer(bufSz, nProd);

		// et nos producteur / consommateur
		int i;
		Producer[] p = new Producer[nProd];
		for (i = 0; i < nProd; i++) {
			p[i] = new Producer(minProd, maxProd, prodTime, prodTimeVariation, buffer);
		}

		Consumer[] c = new Consumer[nCons];
		for (i = 0; i < nCons; i++) {
			c[i] = new Consumer(consTime, consTimeVariation, buffer);
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
		
		// on attend la fin des consommateurs
		for (i = 0; i < c.length; i++) {
			try {
				c[i].join();
			} catch (InterruptedException e) {
			}
		}
		
		long end = System.currentTimeMillis();
		System.out.println("Efficiency: " + (float)buffer.totmsg()/(end - beg) + " messages per millisecond");

	}

}

package jus.poc.prodcons.v1;

import java.io.IOException;
import java.util.Properties;

public class TestProdCons {

	public static void main(String[] args) {
		Properties properties = new Properties();
		try {
			properties.loadFromXML(TestProdCons.class.getClassLoader().getResourceAsStream("options.xml"));
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
	}

}

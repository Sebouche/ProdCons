package jus.poc.prodcons.v1;

public class Message {
	String s;
	
	public void message(String s) {
		this.s=s;
	}
	
	public int len() {
		return s.length();
	}
	
	
}

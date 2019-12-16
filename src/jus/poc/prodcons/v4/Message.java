package jus.poc.prodcons.v4;

public class Message {
	String s;
	
	public Message(String s) {
		this.s=s;
	}
	
	public int len() {
		return s.length();
	}
	
	public String content() {
		return s;
	}
}

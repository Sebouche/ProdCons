package jus.poc.prodcons.v3;

public interface IProdConsBuffer {

	/**
	 * Put the message m in the prodcons buffer
	 **/

	public void put(Message m);

	/**
	 * Retrieve a message from the prodcons buffer, following a fifo order
	 **/

	public Message get();

	/**
	 * Retrieve n messages from the prodcons buffer
	 **/

	public Message[] getn(int n);

	/**
	 * Returns the number of messages currently available in the prodcons buffer
	 **/

	public int nmsg();

	public int totmsg();

}
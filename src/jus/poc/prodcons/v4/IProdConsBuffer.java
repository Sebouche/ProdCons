package jus.poc.prodcons.v4;

public interface IProdConsBuffer {

	/**
	 * Put the message m in the prodcons buffer
	 **/

	public void put(Message m) throws InterruptedException;

	/**
	 * Retrieve a message from the prodcons buffer, following a fifo order
	 **/

	public Message get() throws InterruptedException;

	/**
	 * Retrieve n messages from the prodcons buffer
	 **/

	public Message[] getn(int n) throws InterruptedException;

	/**
	 * Returns the number of messages currently available in the prodcons buffer
	 **/

	public int nmsg();

	public int totmsg();

}
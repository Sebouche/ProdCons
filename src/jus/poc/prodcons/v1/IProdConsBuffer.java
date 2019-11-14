package jus.poc.prodcons.v1;

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
* Retrieve n consecutive messages from the prodcons buffer
**/

	public Message[] get_list() throws InterruptedException;

/**
* Returns the number of messages currently available in the prodcons buffer
**/
	
public int nmsg();

}
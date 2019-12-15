package jus.poc.prodcons.v2;

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
* Returns the number of messages currently available in the prodcons buffer
**/
	
public int nmsg();


public int totmsg();

}
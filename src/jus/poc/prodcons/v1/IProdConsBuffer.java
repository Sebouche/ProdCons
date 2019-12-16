package jus.poc.prodcons.v1;

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
* Returns the number of messages currently available in the prodcons buffer
**/
	
public int nmsg();


public int totmsg();

}
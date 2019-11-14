package jus.poc.prodcons.v1;

public class ProdConsBuffer implements IProdConsBuffer {

	int buffer_size = 10;

	Message[] Buffer = new Message[buffer_size];
	int index_buff = 0;

	@Override
	public void put(Message m) throws InterruptedException {
		if (index_buff >= buffer_size) {
			wait();
		}
		Buffer[index_buff] = m;
		index_buff++;
	}

	@Override
	public Message get() throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message[] get_list() throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int nmsg() {
		// TODO Auto-generated method stub
		return 0;
	}

}

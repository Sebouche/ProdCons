package jus.poc.prodcons.v1;

public class ProdConsBuffer implements IProdConsBuffer {

	int buffer_size;
	Message[] Buffer = new Message[buffer_size];
	int index_buff = -1;
	int total_msgs = 0;

	public ProdConsBuffer(int buffer_size) {
		this.buffer_size = buffer_size;
	}

	@Override
	public void put(Message m) {
		while (index_buff >= buffer_size - 1) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		Buffer[index_buff + 1] = m;
		index_buff++;
		total_msgs++;
		notifyAll();
	}

	@Override
	public Message get() {
		Message m;
		while (nmsg() == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		m = Buffer[index_buff];
		index_buff--;
		notifyAll();
		return m;
	}

	@Override
	public int nmsg() {
		return index_buff + 1;
	}

	@Override
	public int totmsg() {
		return total_msgs;
	}

	public void setBuffer_size(int buffer_size) {
		this.buffer_size = buffer_size;
	}

}

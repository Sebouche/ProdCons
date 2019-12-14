package jus.poc.prodcons.v1;

public class ProdConsBuffer implements IProdConsBuffer {

	int buffer_size;
	Message[] Buffer;
	int index_prod = 0;
	int index_cons = 0;
	int nbMes = 0;

	public ProdConsBuffer(int buffer_size) {
		this.buffer_size = buffer_size;
		Buffer = new Message[buffer_size];
	}

	@Override
	public void put(Message m) {
		synchronized (this) {
			while (nbMes >= buffer_size) {
				try {
					wait();
				} catch (InterruptedException e) {
				}
			}
			Buffer[index_prod % buffer_size] = m;
			index_prod++;
			nbMes++;
			notifyAll();
		}
	}

	@Override
	public Message get() {
		synchronized (this) {
			Message m;
			while (nbMes == 0) {
				try {
					wait();
				} catch (InterruptedException e) {
				}
			}
			m = Buffer[index_cons % buffer_size];
			index_cons++;
			nbMes--;
			notifyAll();
			return m;
		}
	}

	@Override
	public int nmsg() {
		return nbMes;
	}

	@Override
	public int totmsg() {
		return index_prod;
	}
}

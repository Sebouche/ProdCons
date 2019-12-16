package jus.poc.prodcons.v1;

public class ProdConsBuffer implements IProdConsBuffer {

	int buffer_size;
	Message[] Buffer;
	int index_prod = 0;
	int index_cons = 0;
	int nbMes = 0;
	int nbProd;

	public ProdConsBuffer(int buffer_size, int nbProd) {
		this.buffer_size = buffer_size;
		Buffer = new Message[buffer_size];
		this.nbProd = nbProd;
	}

	@Override
	public void put(Message m) throws InterruptedException {
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
	public Message get() throws InterruptedException {
		Message m;
		synchronized (this) {
			// on vérifie si le buffer est vide et si la production est terminée
			while (nbMes == 0 && nbProd > 0) {
				try {
					wait();
				} catch (InterruptedException e) {
				}
			}
			// si la production est terminée, on envoie le message de fin
			if (nbProd == 0 && nbMes == 0) {
				return new Message("End");
			}
			m = Buffer[index_cons % buffer_size];
			index_cons++;
			nbMes--;
			notifyAll();
		}
		return m;
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

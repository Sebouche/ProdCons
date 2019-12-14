package jus.poc.prodcons.v1;

public class ProdConsBuffer implements IProdConsBuffer {

	int buffer_size;
	Message[] Buffer;
	int index_prod = 0;
	int index_cons = 0;
	int nbMes = 0;

	Watcher w;

	public ProdConsBuffer(int buffer_size, Watcher w) {
		this.buffer_size = buffer_size;
		Buffer = new Message[buffer_size];
		this.w = w;
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
			// on vérifie si le buffer est vide et si la production est terminée
			while (nbMes == 0 && !w.endProd()) {
				try {
					wait();
				} catch (InterruptedException e) {
				}
			}
			// si la production est terminée, on réveille tous les threads (qui ne sont plus
			// que des consommateurs) pour qu'ils puissent finir leur exécution
			if (w.endProd()) {
				notifyAll();
				return new Message("End");
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

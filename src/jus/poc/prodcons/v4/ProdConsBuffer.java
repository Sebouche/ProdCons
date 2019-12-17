package jus.poc.prodcons.v4;

import java.util.concurrent.Semaphore;

public class ProdConsBuffer implements IProdConsBuffer {

	int buffer_size;
	Message[] Buffer;
	int index_prod = 0;
	int index_cons = 0;
	int nbMes = 0;
	int nbProd;
	
	Semaphore consecutive = new Semaphore(1);

	public ProdConsBuffer(int buffer_size, int nbProd) {
		this.buffer_size = buffer_size;
		Buffer = new Message[buffer_size];
		this.nbProd = nbProd;
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
				m = new Message("End");
			} else {
				m = Buffer[index_cons % buffer_size];
				index_cons++;
				nbMes--;
			}
			notifyAll();
		}
		return m;
	}
	
	@Override
	public Message[] getn(int n) {
		Message[] m = new Message[n];
		int i = 0;
		
		try {
			consecutive.acquire();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		synchronized (this) {
			while (i < n) {
				// on vérifie si le buffer est vide et si la production est terminée
				while (nbMes == 0 && nbProd > 0) {
					try {
						wait();
					} catch (InterruptedException e) {
					}
				}
				
				// si la production est terminée, on remplit de message de fin
				if (nbProd == 0 && nbMes == 0) {
					m[i] = new Message("End");
				} else {
					m[i] = Buffer[index_cons % buffer_size];
					index_cons++;
					nbMes--;
				}
				i++;
			}
			notifyAll();
		}
		consecutive.release();
		
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

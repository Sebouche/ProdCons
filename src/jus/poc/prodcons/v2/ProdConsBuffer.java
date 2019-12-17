package jus.poc.prodcons.v2;

import java.util.concurrent.Semaphore;

public class ProdConsBuffer implements IProdConsBuffer {

	int buffer_size;
	Message[] Buffer;
	int index_prod = 0;
	int index_cons = 0;
	int nbMes = 0;
	int nbProd;

	Semaphore notFull;
	Semaphore notEmpty;
	Semaphore mutexIn, mutexOut;

	public ProdConsBuffer(int buffer_size, int nbProd) {
		this.buffer_size = buffer_size;
		Buffer = new Message[buffer_size];
		this.nbProd = nbProd;
		notFull = new Semaphore(buffer_size);
		notEmpty = new Semaphore(0);
		mutexIn = new Semaphore(1);
		mutexOut = new Semaphore(1);
	}

	@Override
	public void put(Message m) throws InterruptedException {
		// on vérifie si le buffer est plein
		notFull.acquire();

		// traitement exclusif du buffer
		mutexIn.acquire();
		Buffer[index_prod % buffer_size] = m;
		index_prod++;
		nbMes++;
		mutexIn.release();

		notEmpty.release();
	}

	@Override
	public Message get() throws InterruptedException {
		Message m;
		// on vérifie si le buffer est vide
		notEmpty.acquire();

		// traitement exclusif du buffer
		mutexOut.acquire();
		// si la production est terminée, on envoie le message de fin et on laisse
		// passer un autre thread (consommateur) pour qu'il puisse se finir à son tour
		if (nbProd == 0 && nbMes <= 0) {
			m = new Message("End");
		} else {
			m = Buffer[index_cons % buffer_size];
			index_cons++;
			nbMes--;
		}
		mutexOut.release();

		notFull.release();

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

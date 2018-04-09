package principal;

import utils.Helper;

public class EntradaESaida {

	private byte[] buffer;
	private Barramento barramento;

	public EntradaESaida() {
		buffer = new byte[Main.tamInstrucao]; //Buffer sempre tera o espaco de duas instrucoes
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}

	public Barramento getBarramento() {
		return barramento;
	}

	public void setBarramento(Barramento barramento) {
		this.barramento = barramento;
	}

	public boolean addToBuffer(byte[] b) {
		if(Helper.isVazio(buffer)) {
			buffer = b;
		} else {
			System.out.println("ENTROU");
			buffer = Helper.concatTwoArray(b, buffer);	//adiciona ao buffer
		}
		return true;
	}

	public boolean verificarEspacoBuffer(int nescessario) {
		int cont = 0;
		for(int i = 0; i < this.getBuffer().length; i++) {
			if(buffer[i] == 0) {
				cont++;
				if(cont == nescessario)
					return true;
			} else {
				cont = 0;
			}
		}

		return false;
	}


}

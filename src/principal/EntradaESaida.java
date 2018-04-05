package principal;

import utils.Helper;

public class EntradaESaida {

	private byte[] buffer;
	private Barramento barramento;
	
	public EntradaESaida(int tam) {
		switch(tam) {
		case 4:
			buffer = new byte[4];
			break;
		case 8:
			buffer = new byte[8];
			break;
		case 16:
			buffer = new byte[16];
			break;
		}
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
		
		if(verificarEspacoBuffer(b.length)) {
			Helper.saveAtoB(b, this.buffer);
			return true;
		}
		
		return false;
		
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

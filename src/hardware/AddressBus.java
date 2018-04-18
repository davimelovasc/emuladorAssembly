package hardware;

import java.util.ArrayList;
import java.util.List;

import utils.Constantes;
import utils.Logger;

public class AddressBus {
	
	private boolean offset;
	private List<String> listRam;
	private List<String> listEntradaESaida;
	private List<String> listCPU;
	
	public AddressBus() {
		listRam = new ArrayList<String>();
		listEntradaESaida = new ArrayList<String>();
		listCPU = new ArrayList<String>();
	}
	
	
public void send(String origem, String destino, String endereco, boolean offset) {
		
		switch(destino) {
		case Constantes.RAM:
			this.listRam.add(endereco);
			break;
		case Constantes.MOD_ENTRADA_E_SAIDA:
			this.listEntradaESaida.add(endereco);
			break;
		case Constantes.CPU: 
			this.listCPU.add(endereco);
			break;
			
		default:
			Logger.printError(this.getClass().getName(), "Origem desconhecida");
		}
		
		this.setOffset(offset);
	}

public String reciveRam() {
	String endereco = listRam.get(0);
	listRam.remove(endereco);
	return endereco;
}

public String reciveCPU() {
	String endereco = listCPU.get(0);
	listCPU.remove(endereco);
	return endereco;
}

public String reciveEntradaESaida() {
	String endereco = listEntradaESaida.get(0);
	listEntradaESaida.remove(endereco);
	return endereco;
}

public boolean isOffset() {
	return offset;
}

public void setOffset(boolean offset) {
	this.offset = offset;
}


}

package hardware;

import principal.Main;
import utils.Constantes;
import utils.Logger;

public class DataBus {
	
	private int largura;
	private byte[] listRam;
	private byte[] listEntradaESaida;
	private byte[] listCPU;
	
	public DataBus(int largura) {
		listRam = new byte[Main.tamInstrucao];
		listEntradaESaida = new byte[Main.tamInstrucao];
		listCPU = new byte[Main.tamInstrucao];
		this.largura = largura;
	}
	
public void send(String origem, String destino, byte[] dados) {
		switch(destino) {
		case Constantes.RAM:
			this.listRam = dados;
			break;
		case Constantes.MOD_ENTRADA_E_SAIDA:
			this.listEntradaESaida = dados;
			break;
		case Constantes.CPU: 
			this.listCPU = dados;
			break;
			
		default:
			Logger.printError(this.getClass().getName(), "Origem desconhecida");
		}
	}

public byte[] reciveRam() {
	return listRam;
}

public byte[] reciveCPU() {
	return listCPU;
}

public byte[] reciveEntradaESaida() {
	return listEntradaESaida;
}

public int getLargura() {
	return largura;
}

public void setLargura(int largura) {
	this.largura = largura;
}

}

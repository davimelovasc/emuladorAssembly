package hardware;

import java.util.List;

import utils.Constantes;
import utils.Logger;

public class BarramentoDados {
	
	private int largura;
	byte[] listRam;
	byte[] listEntradaESaida;
	byte[] listCPU;

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

}

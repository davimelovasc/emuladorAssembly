package hardware;

import java.util.List;

import utils.Constantes;
import utils.Logger;

public class BarramentoControle {
	
	List<String> listRam;
	List<String> listEntradaESaida;
	List<String> listCPU;
	
	
public void send(String origem, String destino, String controle) {
		
		switch(destino) {
		case Constantes.RAM:
			this.listRam.add(controle);
			break;
		case Constantes.MOD_ENTRADA_E_SAIDA:
			this.listEntradaESaida.add(controle);
			break;
		case Constantes.CPU: 
			this.listCPU.add(controle);
			break;
			
		default:
			Logger.printError(this.getClass().getName(), "Origem desconhecida");
		}
	}

}

package hardware;

import java.util.List;

import utils.Constantes;
import utils.Logger;

public class BarramentoEndereco {
	
	List<String> listRam;
	List<String> listEntradaESaida;
	List<String> listCPU;
	
	
public void send(String origem, String destino, String endereco) {
		
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
	}


}

package hardware;

import java.util.ArrayList;
import java.util.List;

import utils.Constantes;
import utils.Dado;
import utils.Logger;

public class Barramento {
	
	List<Dado> listRam;
	List<Dado> listEntradaESaida;
	List<Dado> listCPU;
	private int tamanho;
	
	public Barramento(int tamanho) {
		switch(tamanho) {
		case 8:
			this.tamanho = tamanho;
			break;
		case 16:
			this.tamanho = tamanho;
			break;
			
		case 32:
			this.tamanho = tamanho;
			break;
		default:
			Logger.printError(this.getClass().getName(), "Tamanho de barramento inválido");
				
		}
		
		listRam = new ArrayList<>(); 
		listEntradaESaida = new ArrayList<>();
		listCPU = new ArrayList<>();
		
	}
	

	public void send(String origem, String destino, Dado dado) {
		
		switch(destino) {
		case Constantes.RAM:
			this.listRam.add(dado);
			
			
			break;
		case Constantes.MOD_ENTRADA_E_SAIDA:
			this.listEntradaESaida.add(dado);
			break;
		case Constantes.CPU: 
			this.listCPU.add(dado);
			
			break;
			
		default:
			Logger.printError(this.getClass().getName(), "Origem desconhecida");
		}
	}
	
	public Dado reciveRam() {
		Dado d = listRam.get(0);
		listRam.remove(d);
		return d;
	}
	
	public Dado reciveEntradaESaida() {
		Dado d = listEntradaESaida.get(0);
		listEntradaESaida.remove(d);
		return d;
	}
	
	public Dado reciveCPU() {
		Dado d = listCPU.get(0);
		listCPU.remove(d);
		return d;
	}


	public int getTamanho() {
		return tamanho;
	}


	
}

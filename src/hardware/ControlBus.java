package hardware;

import java.util.ArrayList;
import java.util.List;

import utils.Constantes;
import utils.Logger;

public class ControlBus {

	private List<String> listRam;
	private List<String> listEntradaESaida;
	private List<String> listCPU;
	private int tamanho;
	
	public ControlBus(int tamanho) {
		listRam = new ArrayList<String>();
		listEntradaESaida = new ArrayList<String>();
		listCPU = new ArrayList<String>();
		this.tamanho = tamanho;
	}


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

	public String reciveRam() {
		String controle = listRam.get(0);
		listRam.remove(controle);
		return controle;
	}

	public String reciveCPU() {
		String controle = listCPU.get(0);
		listCPU.remove(controle);
		return controle;
	}

	public String reciveEntradaESaida() {
		String controle = listEntradaESaida.get(0);
		listEntradaESaida.remove(controle);
		return controle;
	}


	public int getTamanho() {
		return tamanho;
	}


	public void setTamanho(int tamanho) {
		this.tamanho = tamanho;
	}

}

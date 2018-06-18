package hardware;

import java.util.Arrays;

import principal.Main;
import utils.Constantes;
import utils.Helper;
import utils.Logger;

public class DataBus {

	private byte[] listRam;
	private byte[] listEntradaESaida;
	private byte[] listCPU;
	private int tamanho;

	public DataBus(int tamanho) {
		listRam = new byte[Main.tamInstrucao];
		listEntradaESaida = new byte[Main.tamInstrucao];
		listCPU = new byte[Main.tamInstrucao];
		this.tamanho = tamanho;
	}

	public void send(String origem, String destino, byte[] dados) {
		int larguraBanda = Main.larguraBanda;

		if(larguraBanda < dados.length) {
			System.out.println("\nEnviando dado maior que a largura de banda. Tamanho do dado: " + dados.length + ". Tamanho da largura de banda: " + Main.larguraBanda);
			int i = 0;
			while(larguraBanda < dados.length) {
				byte[] p = Arrays.copyOfRange(dados, i, larguraBanda);
				addFila(destino, p);
				i = larguraBanda;
				larguraBanda += Main.larguraBanda; 


				try {
					System.out.println("rajada sendo enviada ...");
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

			byte[] p = Arrays.copyOfRange(dados, i, dados.length);
			addFila(destino, p);
			try {
				System.out.println("rajada sendo enviada ...");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}


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



	//	}

	public byte[] reciveRam() {
		return listRam;
	}

	public byte[] reciveCPU() {
		return listCPU;
	}

	public byte[] reciveEntradaESaida() {
		return listEntradaESaida;
	}


	public int getTamanho() {
		return tamanho;
	}

	public void setTamanho(int tamanho) {
		this.tamanho = tamanho;
	}

	private void addFila(String destino, byte[] dados) {
		switch(destino) {
		case Constantes.RAM:
			Helper.concatTwoArray(dados, this.listRam);
			break;
		case Constantes.MOD_ENTRADA_E_SAIDA:
			Helper.concatTwoArray(dados, this.listEntradaESaida);
			break;
		case Constantes.CPU: 
			Helper.concatTwoArray(dados, this.listCPU);
			break;

		default:
			Logger.printError(this.getClass().getName(), "Origem desconhecida");

		}
	}

}

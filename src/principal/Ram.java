package principal;

import utils.Constantes;
import utils.Helper;
import utils.Logger;

public class Ram {
	private byte[] ram;
	private Barramento barramento;

	public Ram(int size) {
		if(size == 32 || size == 64 || size == 128 )
			this.ram = new byte[size];
		else {
			Logger.printError("Ram", "Tamanho de ram inválido");

		}

	}

	
	public void setRam(byte[] celulas) {
		if(celulas.length == this.ram.length) {
			this.ram = celulas;
		}
	}
		
	public byte[] getCelulas() {
		return ram;
	}


	public Barramento getBarramento() {
		return barramento;
	}


	public void setBarramento(Barramento barramento) {
		this.barramento = barramento;
	}

	public byte[] recive() {
		Dado d = barramento.reciveRam();
		String controle = d.getControle();
		byte[] dados = d.getDados();
		int endereco = 0;

		//Validação do endereço
		if(! Helper.validarEndereco(d.getEndereco())) 
			Logger.printError(getClass().getName(), "Endereco informádo inválido");

		if(d.isOffset()) {
			endereco = Helper.formatarEndereco(d.getEndereco()) + Main.tamInstrucao*2; //transforma end. fisico em end. logico
		}else {
			endereco = Helper.formatarEndereco(d.getEndereco());
		}

		if(controle.equals(Constantes.KEY_ESCREVER)) {

			if(this.getCelulas().length > dados.length) {
				for(int i = 0; i < dados.length; i++) {
					this.ram[endereco+i] = dados[i];
				}
			}else {
				Logger.printError(getClass().getName(), "Tamanho da ram muito pequena para armazenar duas palavras na ram");
			}


		} else if(controle.equals(Constantes.KEY_LER)) {

			for(int i = 0; i < dados.length; i ++) {
				dados[i] = this.ram[endereco+i];
			}

			return dados;


		} else if(controle.equals(Constantes.KEY_ATUALIZAR)){
			if(Main.cpu.getTam() == 16) {
				this.ram[endereco] += CPU.fromTwoByteArray(dados);
			}else if (Main.cpu.getTam() == 32) {
				this.ram[endereco] += CPU.fromFourByteArray(dados);
			}else if(Main.cpu.getTam() == 64) {
				this.ram[endereco] += CPU.fromEightByteArray(dados);
			}


		} else {
			//erro
		}
		return null;

	}
}

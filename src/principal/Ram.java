package principal;

import java.util.Base64.Decoder;

import utils.Constantes;
import utils.Helper;
import utils.Logger;
import utils.Validate;

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
			
			for(int i = 0; i < dados.length; i++) {
				this.ram[endereco+i] = dados[i];
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
	
	public void sendToCpu(byte[] dados) {
		
		
	}
	
/*	public boolean verificarEspacoRamInstrucao() {
		int espacoReservado;
		int nescessario;
		
		switch(Main.cpu.getTam()) {
		case 16:
			espacoReservado = 16; //duas instrucoes de 8 bytes cada
			nescessario = 8; //bytes nescessario para a maaior intrunção
			for (int i = 0; i < espacoReservado; i++) {
				if(getCelulas()[i] == 0) {
					nescessario--;
				} else {
					nescessario = 8;
				}
				if(nescessario == 0) {
					return true;
				}
			}
			
			return false;
		case 32:
			espacoReservado = 32; //duas instrucoes de 16 bytes cada
			nescessario = 16; //bytes nescessario para a maior instrunção
			for (int i = 0; i < espacoReservado; i++) {
				if(getCelulas()[i] == 0) {
					nescessario--;
				} else {
					nescessario = 16;
				}
				if(nescessario == 0) {
					return true;
				}
			}
			
			return false;
		case 64:
			espacoReservado = 64; //duas instrucoes de 32 bytes cada
			nescessario = 32; //bytes nescessario para a maior instrunção
			for (int i = 0; i < espacoReservado; i++) {
				if(getCelulas()[i] == 0) {
					nescessario--;
				} else {
					nescessario = 32;
				}
				if(nescessario == 0) {
					return true;
				}
			}
			
			
		
		}
		return false;
	}*/
	
	
	
	


	/*public void setCelulas(byte[] celulas) {
		this.celulas = celulas;
	}*/


	

/*
	public byte ler(String endereco) {
		endereco = Helpers.formatarEndereco(endereco);
		if(! validarEndereco(endereco)) {
			Logger.printError(this.getClass().getName(), "Erro no endere�o de ram!");
			return 0;
		}
			
		int end = Helpers.hexaToDec(Helpers.formatarEndereco(endereco));
		
		if(end >= ram.length) {
			Logger.printError("Ram", Constantes.ERROR_RAM_MESSAGE1);
			return 0;
		} else {
			return ram[end];
		}
		
	}


	public byte escrever(String endereco, byte dado) {
		endereco = Helpers.formatarEndereco(endereco);
		if(! validarEndereco(endereco)) {
			Logger.printError(this.getClass().getName(), "Erro no endere�o de ram!");
			return 0;
		}
		int end = Helpers.hexaToDec(endereco);
		
		if(end >= ram.length) {
			Logger.printError("Ram", Constantes.ERROR_RAM_MESSAGE1);
			return 0;
		} else {
			ram[end] = dado;
			return ram[end];
		}
	}
	
	public static boolean validarEndereco(String endereco) {
		
		return true;
	}*/


}

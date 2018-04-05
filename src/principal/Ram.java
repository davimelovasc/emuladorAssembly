package principal;

import utils.Constantes;
import utils.Helper;
import utils.Logger;
import utils.Validate;

public class Ram {
	private byte[] ram;
	private Barramento barramento;

	public Ram(int size) {
		if(size == 8 || size == 16 || size == 32 )
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
		int endereco = Integer.parseInt(d.getEndereco());
		//validar endereco
		
		if(controle.equals(Constantes.KEY_ESCREVER)) {
			
			for(int i = 0; i < dados.length; i++) {
				this.ram[endereco+i] = dados[i];
			}
			
			
		} else if(controle.equals(Constantes.KEY_LER)) {
			
			for(int i = 0; i < dados.length; i ++) {
				dados[i] = this.ram[endereco+i];
			}
			
			return dados;
			
			
		} else {
			//erro
		}
		return null;
		
	}
	
	public void sendToCpu(byte[] dados) {
		
		
	}
	
	
	
	


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

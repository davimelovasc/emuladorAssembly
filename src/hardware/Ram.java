package hardware;

import principal.Main;
import utils.Constantes;
import utils.Helper;
import utils.Logger;

public class Ram {
	private byte[] ram;
	/*private Barramento barramento;*/
	private ControlBus controlBus;
	private DataBus dataBus;
	private AddressBus addressBus;

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


/*	public Barramento getBarramento() {
		return barramento;
	}


	public void setBarramento(Barramento barramento) {
		this.barramento = barramento;
	}*/

	public byte[] recive() {
		
		String controle = controlBus.reciveRam();
		String enderecoStr = addressBus.reciveRam();
		boolean offset = addressBus.isOffset();
		byte[] dados = dataBus.reciveRam();
		
		
	/*	Dado d = barramento.reciveRam();
		String controle = d.getControle();
		byte[] dados = d.getDados();*/
		
		int endereco = 0;

		//Validação do endereço
		if(! Helper.validarEndereco(enderecoStr)) 
			Logger.printError(getClass().getName(), "Endereco informádo inválido");

		if(offset) {
			endereco = Helper.formatarEndereco(enderecoStr) + Main.tamInstrucao*2; //transforma end. fisico em end. logico
		}else {
			endereco = Helper.formatarEndereco(enderecoStr);
		}
		
		if(controle.equals(Constantes.KEY_ESCREVER)) {

			if(this.getCelulas().length > dados.length) {
				for(int i = 0; i < dados.length; i++) {
					this.ram[endereco+i] = dados[i];
				}
			}else {
				Logger.printError(getClass().getName(), "Tamanho da ram muito pequena para armazenar dados na ram");
			}


		} else if(controle.equals(Constantes.KEY_LER)) {

			for(int i = 0; i < dados.length; i ++) {
				dados[i] = this.ram[endereco+i];
			}

			return dados;


		}else {
			//erro
		}
		return null;

	}


	public ControlBus getControlBus() {
		return controlBus;
	}


	public void setControlBus(ControlBus controlBus) {
		this.controlBus = controlBus;
	}


	public DataBus getDataBus() {
		return dataBus;
	}


	public void setDataBus(DataBus dataBus) {
		this.dataBus = dataBus;
	}


	public AddressBus getAddressBus() {
		return addressBus;
	}


	public void setAddressBus(AddressBus addressBus) {
		this.addressBus = addressBus;
	}


	public byte[] getRam() {
		return ram;
	}

	
	
	
}

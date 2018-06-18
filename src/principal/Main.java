package principal;

import java.util.ArrayList;
import java.util.Scanner;

import entradaDeDados.Encoder;
import entradaDeDados.Parser;
import hardware.AddressBus;
import hardware.CPU;
import hardware.ControlBus;
import hardware.DataBus;
import hardware.EntradaESaida;
import hardware.Ram;
import utils.Constantes;
import utils.Helper;
import utils.Logger;
import utils.ReadFile;
import utils.Validate;

public class Main {

	static public Ram ram;
	static public CPU cpu;
	static public EntradaESaida entradaESaida;
	static public int larguraBanda;
	//static public Barramento barramento;
	static public int tamInstrucao;
	static public String ponteiroBuffer; //de onde sera lida a proxima instrucao na ram
	static public ControlBus controlBus;
	static public DataBus dataBus;
	static public AddressBus addressBus;
	static public boolean pausado;

	public static void main(String[] args) {

		startEmu();

		ReadFile readFile = new ReadFile("teste.txt");
		ArrayList<String> linhas = readFile.readFile();
		byte[] instrucoesEmByte = null;
		int l = 0;
		String linha;
		int aux = 0;


		while(l < linhas.size()) {
			
			Helper.clearBuffer();
			
			for(int i = 0; l < linhas.size(); i++ ) { //ler todas as linhas, porem de duas em duas
				if(i==5)
					break;
				linha = linhas.get(l);

				ArrayList<String> tokens =  Parser.parse(linha); //le a linha, faz as validacoes e retorna as tokens
				
				instrucoesEmByte = Encoder.encode(tokens); //transforma as tokens da linha em byte[]
				
				if(entradaESaida.addToBuffer(instrucoesEmByte)) { //tokens em byte sao adicionadas ao buffer (se tiver espaco)
					System.out.println("\nInstrução \"" + linha + "\"(em bytes) adicionada ao buffer de E/S\n");
					aux++;
					l++;
				} else
					break;
			}
			
			//Buffer com 5 ou menos instrucoes

			//barramento.send(Constantes.MOD_ENTRADA_E_SAIDA, Constantes.RAM, new Dado(Constantes.KEY_ESCREVER, entradaESaida.getBuffer(), ponteiroBuffer, false));
			controlBus.send(Constantes.MOD_ENTRADA_E_SAIDA, Constantes.RAM, Constantes.KEY_ESCREVER);
			dataBus.send(Constantes.MOD_ENTRADA_E_SAIDA, Constantes.RAM, entradaESaida.getBuffer());
			addressBus.send(Constantes.MOD_ENTRADA_E_SAIDA, Constantes.RAM, ponteiroBuffer, false);
			
			ram.recive(); //executa a escrita das inst. na ram
			System.out.println("\nDados do buffer de E/S enviados para a RAM\n");
			//Instrução(ões) salvas na ram
			
			if(l <= 5) {	
				Logger.printFeedBack();
			}

			while(aux != 0 ) {

				//interceptor																												
				//barramento.send(Constantes.MOD_ENTRADA_E_SAIDA, Constantes.CPU, new Dado(Constantes.KEY_INTERCEPTOR, new byte[tamInstrucao], ponteiroBuffer, false));
				controlBus.send(Constantes.MOD_ENTRADA_E_SAIDA, Constantes.CPU, Constantes.KEY_INTERCEPTOR);
				dataBus.send(Constantes.MOD_ENTRADA_E_SAIDA, Constantes.CPU, new byte[tamInstrucao]);
				addressBus.send(Constantes.MOD_ENTRADA_E_SAIDA, Constantes.CPU, ponteiroBuffer, false);
				
				cpu.recive(); //aqui dentro a cpu manda pedindo os dados da ram

				byte[] res = ram.recive(); //se for leitura retorna dados lidos;
				System.out.println("\nInstruções em byte indo para a cpu: ");
				Helper.printArrayByte(res);

				//barramento.send(Constantes.RAM, Constantes.CPU, new Dado(Constantes.RESPOSTA, res, null)); //ram retorna os dados que a cpu pediu
				
				controlBus.send(Constantes.RAM, Constantes.CPU, Constantes.RESPOSTA);
				dataBus.send(Constantes.RAM, Constantes.CPU, res);
				addressBus.send(Constantes.RAM, Constantes.CPU, null, true);
				
				Main.cpu.recive(); //executa a instrucao


				ponteiroBufferNext();
				
				aux--;
			}

			


		}


	}

	public static void startEmu() {
		Scanner s = new Scanner(System.in);

		System.out.println("Informe o tamanho do barramento: ");
		int tam = s.nextInt();
		dataBus = new DataBus(tam);
		addressBus = new AddressBus(tam);
		controlBus = new ControlBus(tam);
		

		System.out.println("Informe o tamanho da CPU: ");
		cpu = new CPU(s.nextInt());
		
		System.out.println("Informe a frquência (Hz): ");
		larguraBanda = tam * s.nextInt();

		System.out.println("\nTamanho da ram definido pelo sistema: 256 bytes\n");
		ram = new Ram();
		
		System.out.println("\nTamanho do Buffer de entrada e saída definido pelo sistema: 5 instruções\n");
		entradaESaida = new EntradaESaida();
		
		System.out.println("\nLargura de banda: " + larguraBanda +" bytes/seg\n");
		
		System.out.println("\nDeseja realizar a execução pausadamente ? (true or false)");
		pausado = s.nextBoolean();

		cpu.setAddressBus(addressBus);
		cpu.setControlBus(controlBus);
		cpu.setDataBus(dataBus);
		
		ram.setAddressBus(addressBus);
		ram.setControlBus(controlBus);
		ram.setDataBus(dataBus);
		
		entradaESaida.setAddressBus(addressBus);
		entradaESaida.setControlBus(controlBus);
		entradaESaida.setDataBus(dataBus);
		

		if(Validate.validarEmu(cpu, ram, entradaESaida, addressBus, controlBus, dataBus)) {
			
			return;
		}else {
			startEmu();
		}
		
		


	}

	public static void ponteiroBufferNext() {
		
			switch(Main.cpu.getTam()) {
			case 16:
				if(ponteiroBuffer.equalsIgnoreCase("0x0000")){
					ponteiroBuffer = "0x0008";
					return;
				}
				if(ponteiroBuffer.equalsIgnoreCase("0x0008")) {
					ponteiroBuffer = "0x0010";
					return;
				}
				if (ponteiroBuffer.equalsIgnoreCase("0x0010")) {
					ponteiroBuffer = "0x0018";
					return;
				}
				if(ponteiroBuffer.equalsIgnoreCase("0x0018")) {
					ponteiroBuffer = "0x0020";
					return;
				}
				if(ponteiroBuffer.equalsIgnoreCase("0x0020")) {
					ponteiroBuffer = "0x0000";
					return;
				}
				

			case 32:
				if(ponteiroBuffer.equalsIgnoreCase("0x00000000")) {
					ponteiroBuffer = "0x00000010"; //16 em hexa
					return;
				}
				if(ponteiroBuffer.equalsIgnoreCase("0x00000010")){
					ponteiroBuffer = "0x00000020";
					return;
				}
				if(ponteiroBuffer.equalsIgnoreCase("0x00000020")){
					ponteiroBuffer = "0x00000030";
					return;
				}
				if(ponteiroBuffer.equalsIgnoreCase("0x00000030")){
					ponteiroBuffer = "0x00000040";
					return;
				}
				if(ponteiroBuffer.equalsIgnoreCase("0x00000040")){
					ponteiroBuffer = "0x00000000";
					return;
				}
			case 64:
				if(ponteiroBuffer.equalsIgnoreCase("0x0000000000000000")) {
					ponteiroBuffer = "0x0000000000000020";
					return;
				}
				if(ponteiroBuffer.equalsIgnoreCase("0x0000000000000020")){
					ponteiroBuffer = "0x0000000000000040";
					return;
				}
				if(ponteiroBuffer.equalsIgnoreCase("0x0000000000000040")){
					ponteiroBuffer = "0x0000000000000060";
					return;
				}
				if(ponteiroBuffer.equalsIgnoreCase("0x0000000000000060")){
					ponteiroBuffer = "0x0000000000000080";
					return;
				}
				if(ponteiroBuffer.equalsIgnoreCase("0x0000000000000080")){
					ponteiroBuffer = "0x0000000000000000";
					return;
				}
			} 
		}
	

}

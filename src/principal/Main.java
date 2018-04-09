package principal;

import java.util.ArrayList;
import java.util.Scanner;

import utils.Constantes;
import utils.Helper;
import utils.Logger;
import utils.ReadFile;
import utils.Validate;

public class Main {

	static public Ram ram;
	static public CPU cpu;
	static public EntradaESaida entradaESaida;
	static public Barramento barramento;
	static public int tamInstrucao;
	static public String ponteiroBuffer;

	public static void main(String[] args) {

		startEmu();

		ReadFile readFile = new ReadFile("teste.txt");
		ArrayList<String> linhas = readFile.readFile();
		byte[] instrucoesEmByte = null;
		int l = 0;
		String linha;
		int aux = 0;


		while(! linhas.isEmpty()) {

			for(int i = 0; l < linhas.size(); i++ ) { //ler todas as linhas, porem de duas em duas
				if(i==2)
					break;
				linha = linhas.get(l);

				ArrayList<String> tokens =  Parser.parse(linha); //le a linha, faz as validacoes e retorna as tokens
				
				instrucoesEmByte = Encoder.encode(tokens); //transforma as tokens da linha em byte[]

				/*System.out.println("INSTRUÇÕES EM BYTES: ");
				Helper.printArrayByte(instrucoesEmByte);
				System.out.println("FIM");*/
				
				if(entradaESaida.addToBuffer(instrucoesEmByte)) { //tokens em byte sao adicionadas ao buffer (se tiver espaco)
					/*System.out.println("BUFFER EM BYTE");
					Helper.printArrayByte(Main.entradaESaida.getBuffer());*/
					//linhas.remove(l);
					l++;
				} else
					break;
			}
			

		
			
			//Buffer com 2 instrucoes

			while(aux < 2) {

				barramento.send(Constantes.MOD_ENTRADA_E_SAIDA, Constantes.RAM, new Dado(Constantes.KEY_ESCREVER, entradaESaida.getBuffer(), ponteiroBuffer, false));
				ram.recive(); //executa
			
				//Helper.ordenarRam(); AJEITAR
				Helper.printArrayByte(ram.getCelulas()); //APAGAR
				//ATE AQUI OK! Instrucoes na ram, precisa ordenar
				
				
				//interceptor																												//end -> msm que foi passado para salvar na ram
				barramento.send(Constantes.MOD_ENTRADA_E_SAIDA, Constantes.CPU, new Dado(Constantes.KEY_INTERCEPTOR, new byte[tamInstrucao], ponteiroBuffer));
				cpu.recive(); //aqui dentro a cpu manda pede os dados da ram

				byte[] res = ram.recive(); //se for leitura retorna dados lidos;


				barramento.send(Constantes.RAM, Constantes.CPU, new Dado(Constantes.RESPOSTA, res, null)); //ram retorna os dados que a cpu pediu
				Main.cpu.recive(); //executa a instrucao



				ponteiroBufferNext();


				aux++;
			}

			aux =0;


		}


	}

	public static void startEmu() {
		Scanner s = new Scanner(System.in);

		System.out.println("Informe o tamanho do barramento: ");
		barramento = new Barramento(32); //s.nextInt()

		System.out.println("Informe o tamanho da CPU: ");
		cpu = new CPU(32);

		System.out.println("Informe o tamanho da RAM: ");
		ram = new Ram(64);

		System.out.println("Informe o tamanho do Buffer de entrada e saída: ");
		entradaESaida = new EntradaESaida();

		cpu.setBarramento(barramento);
		ram.setBarramento(barramento);
		entradaESaida.setBarramento(barramento);

		if(Validate.validarEmu(cpu, ram, entradaESaida, barramento)) {
			s.close();
			return;
		}else {
			s.close();
			Logger.printError(Main.class.getName(), "Configurações do emulador inválidas!");
			startEmu();
		}
		
		


	}

	public static void ponteiroBufferNext() {
		if(ponteiroBuffer.endsWith("00")) {
			switch(Main.cpu.getTam()) {
			case 16:
				if(ponteiroBuffer.endsWith("00")) {
					ponteiroBuffer = "0x0008";
					return;
				}else {
					ponteiroBuffer = "0x0000";
					return;
				}

			case 32:
				if(ponteiroBuffer.endsWith("00")) {
					ponteiroBuffer = "0x00010";
					return;
				}else {
					ponteiroBuffer = "0x00000";
					return;
				}
			case 64:
				if(ponteiroBuffer.endsWith("00")) {
					ponteiroBuffer = "0x000020";
					return;
				}else {
					ponteiroBuffer = "0x000000";
					return;
				}
			} 
		}
	}

}

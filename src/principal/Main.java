package principal;

import java.util.ArrayList;
import java.util.Scanner;

import entradaDeDados.Encoder;
import entradaDeDados.Parser;
import hardware.Barramento;
import hardware.CPU;
import hardware.EntradaESaida;
import hardware.Ram;
import utils.Constantes;
import utils.Dado;
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
	static public String ponteiroBuffer; //de onde sera lida a proxima instrucao na ram

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
				if(i==2)
					break;
				linha = linhas.get(l);

				ArrayList<String> tokens =  Parser.parse(linha); //le a linha, faz as validacoes e retorna as tokens
				System.out.println("comecando o encode");
				instrucoesEmByte = Encoder.encode(tokens); //transforma as tokens da linha em byte[]
				System.out.println("encode finalizado");		
				if(entradaESaida.addToBuffer(instrucoesEmByte)) { //tokens em byte sao adicionadas ao buffer (se tiver espaco)
					System.out.println("\nInstrução \"" + linha + "\"(em bytes) adicionada ao buffer de E/S\n");
					aux++;
					l++;
				} else
					break;
			}
			
			//Buffer com 1 ou 2 instrucoes

			barramento.send(Constantes.MOD_ENTRADA_E_SAIDA, Constantes.RAM, new Dado(Constantes.KEY_ESCREVER, entradaESaida.getBuffer(), ponteiroBuffer, false));
			ram.recive(); //executa a escrita das inst. na ram
			System.out.println("\nDados do buffer de E/S enviados para a RAM\n");
			//Instrução(ões) salvas na ram
			
			if(l == 2 || l == 1) {	
				Logger.printFeedBack();
			}

			while(aux != 0 ) {

				//interceptor																												
				barramento.send(Constantes.MOD_ENTRADA_E_SAIDA, Constantes.CPU, new Dado(Constantes.KEY_INTERCEPTOR, new byte[tamInstrucao], ponteiroBuffer, false));
				cpu.recive(); //aqui dentro a cpu manda pedindo os dados da ram

				byte[] res = ram.recive(); //se for leitura retorna dados lidos;
				System.out.println("\nInstruções em byte indo para a cpu: ");
				Helper.printArrayByte(res);

				barramento.send(Constantes.RAM, Constantes.CPU, new Dado(Constantes.RESPOSTA, res, null)); //ram retorna os dados que a cpu pediu
				Main.cpu.recive(); //executa a instrucao


				ponteiroBufferNext();
				
				aux--;
			}

			


		}


	}

	public static void startEmu() {
		Scanner s = new Scanner(System.in);

		System.out.println("Informe o tamanho do barramento: ");
		barramento = new Barramento(s.nextInt());

		System.out.println("Informe o tamanho da CPU: ");
		cpu = new CPU(s.nextInt());

		System.out.println("Informe o tamanho da RAM: ");
		ram = new Ram(s.nextInt());
/*
		System.out.println("Informe o tamanho do Buffer de entrada e saída: ");*/
		entradaESaida = new EntradaESaida();

		cpu.setBarramento(barramento);
		ram.setBarramento(barramento);
		entradaESaida.setBarramento(barramento);

		if(Validate.validarEmu(cpu, ram, entradaESaida, barramento)) {
			s.close();
			return;
		}else {
			startEmu();
		}
		
		s.close();


	}

	public static void ponteiroBufferNext() {
		
			switch(Main.cpu.getTam()) {
			case 16:
				if(ponteiroBuffer.equalsIgnoreCase("0x0000")) {
					ponteiroBuffer = "0x0008"; //
					return;
				}else {
					ponteiroBuffer = "0x0000";
					return;
				}

			case 32:
				if(ponteiroBuffer.equalsIgnoreCase("0x00000000")) {
					ponteiroBuffer = "0x00000010"; //16 em hexa
					return;
				}else {
					ponteiroBuffer = "0x00000000";
					return;
				}
			case 64:
				if(ponteiroBuffer.equalsIgnoreCase("0x0000000000000000")) {
					ponteiroBuffer = "0x0000000000000020";
					return;
				}else {
					ponteiroBuffer = "0x0000000000000000";
					return;
				}
			} 
		}
	

}

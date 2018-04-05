package principal;

import java.util.ArrayList;
import java.util.Scanner;

import utils.Constantes;
import utils.Logger;
import utils.ReadFile;
import utils.Validate;

public class Main {
	
	static public Ram ram;
	static public CPU cpu;
	static public EntradaESaida entradaESaida;
	static public Barramento barramento;
	
	public static void main(String[] args) {

		startEmu();
	
		ReadFile readFile = new ReadFile("teste.txt");
		ArrayList<String> linhas = readFile.readFile();
		byte[] instrucoesEmByte = null;
		int l = 0, tamInstrucao = 0;
		int ponteiroBuffer = 0;
		for( String linha : linhas) {
			
			ArrayList<String> tokens =  Parser.parse(linha); //le a linha faz as validacoes e retorna as tokens
			instrucoesEmByte = Encoder.encode(tokens);
			tamInstrucao += instrucoesEmByte.length;
			if(entradaESaida.addToBuffer(instrucoesEmByte)) {
				l++;
				continue;
			} else
				break;
			
		}
		
		//Buffer cheio ou fim das instruções
		
		
		barramento.send(Constantes.MOD_ENTRADA_E_SAIDA, Constantes.RAM, new Dado(Constantes.KEY_ESCREVER, entradaESaida.getBuffer(), Integer.toString(ponteiroBuffer)));
		ram.recive();
		
		//instruncao ja estao salva no endere�o informado
		
		//interceptor																												//end -> msm que foi passado para salvar na ram
		barramento.send(Constantes.MOD_ENTRADA_E_SAIDA, Constantes.CPU, new Dado(Constantes.KEY_INTERCEPTOR, new byte[tamInstrucao], Integer.toString(ponteiroBuffer)));
		cpu.recive();
		
		byte[] res = ram.recive(); //se for leitura retorna dados lidos;
		
		
		barramento.send(Constantes.RAM, Constantes.CPU, new Dado(Constantes.RESPOSTA, res, null));
		Main.cpu.recive();
		
		
		
		
		
		ponteiroBuffer += entradaESaida.getBuffer().length;
		
		
		
		
		
		
		
		
	}
	
	public static void startEmu() {
		Scanner s = new Scanner(System.in);
		
		System.out.println("Informe o tamanho do barramento: ");
		barramento = new Barramento(s.nextInt());
		
		System.out.println("Informe o tamanho da CPU: ");
		cpu = new CPU(s.nextInt());
		
		System.out.println("Informe o tamanho da RAM: ");
		ram = new Ram(s.nextInt());
		
		System.out.println("Informe o tamanho do Buffer de entrada e saída: ");
		entradaESaida = new EntradaESaida(s.nextInt());
		
		cpu.setBarramento(barramento);
		ram.setBarramento(barramento);
		entradaESaida.setBarramento(barramento);
		
		if(Validate.validarEmu(cpu, ram, entradaESaida, barramento)) {
			return;
		}else {
			Logger.printError(Main.class.getName(), "Configurações do emulador inválidas!");
			startEmu();
		}
		
		
	}

}

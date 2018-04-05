package principal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import utils.Constantes;
import utils.Validate;

public class CPU {

	private long[] registradores64;
	private int[] registradores32;
	private short[] registradores16;
	private int tam;
	private Barramento barramento;

	public CPU(int tam_palavra) {
		switch(tam_palavra) {

		case 16:
			registradores16 = new short[4];
			tam = 16;
			break;
		case 32:
			registradores32 = new int[4];
			tam = 32;
			break;
		case 64:
			registradores64 = new long[4];
			tam = 64;
			break;
		}

	}

	public void executarInstrucao(ArrayList<String> tokens) {
		switch(tokens.get(0)) {
		case "mov":
			if(Validate.isRegistrador(tokens.get(1))) {
				switch (tokens.get(1)) {
				case "A":
					if(Validate.isEndRam(tokens.get(2))) { //tokens(2) é end de ram
						barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_LER, new byte[1], tokens.get(2) ));
						byte[] b = Main.ram.recive();
						int x = (int) b[0];
						
						if(tam == 16)
							registradores16[0] = (short) x;
						else if(tam == 32)
							registradores32[0] = Integer.valueOf(x);
						else if(tam == 64)
							registradores64[0] = Long.valueOf(x);
						
						
					} else { //tokens(2) é um numero
						if(tam == 16)
							registradores16[0] = Short.valueOf(tokens.get(2));
						else if(tam == 32)
							registradores32[0] = Integer.valueOf(tokens.get(2));
						else if(tam == 64)
							registradores64[0] = Long.valueOf(tokens.get(2));
					}
					break;
				case "B":
					if(Validate.isEndRam(tokens.get(2))) { //tokens(2) é end de ram
						
						barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_LER, new byte[1], tokens.get(2) ));
						byte[] b = Main.ram.recive();
						int x = (int) b[0];
						
						if(tam == 16)
							registradores16[1] = (short) x;
						else if(tam == 32)
							registradores32[1] = Integer.valueOf(x);
						else if(tam == 64)
							registradores64[1] = Long.valueOf(x);

					} else { //tokens(2) é um numero
						if(tam == 16)
							registradores16[1] = Short.valueOf(tokens.get(2));
						else if(tam == 32)
							registradores32[1] = Integer.valueOf(tokens.get(2));
						else if(tam == 64)
							registradores64[1] = Integer.valueOf(tokens.get(2));
					}
					break;
				case "C":
					if(Validate.isEndRam(tokens.get(2))) { //tokens(2) é end de ram
						barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_LER, new byte[1], tokens.get(2) ));
						byte[] b = Main.ram.recive();
						int x = (int) b[0];
						
						if(tam == 16)
							registradores16[2] = (short) x;
						else if(tam == 32)
							registradores32[2] = Integer.valueOf(x);
						else if(tam == 64)
							registradores64[2] = Long.valueOf(x);
					} else { //tokens(2) é um numero
						if(tam == 16)
							registradores16[2] = Short.valueOf(tokens.get(2));
						else if(tam == 32)
							registradores32[2] = Integer.valueOf(tokens.get(2));
						else if(tam == 64)
							registradores64[2] = Integer.valueOf(tokens.get(2));
					}
					break;
				case "D":
					if(Validate.isEndRam(tokens.get(2))) { //tokens(2) é end de ram
						barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_LER, new byte[1], tokens.get(2) ));
						byte[] b = Main.ram.recive();
						int x = (int) b[0];
						
						if(tam == 16)
							registradores16[3] = (short) x;
						else if(tam == 32)
							registradores32[3] = Integer.valueOf(x);
						else if(tam == 64)
							registradores64[3] = Long.valueOf(x);
					} else { //tokens(2) é um numero
						if(tam == 16)
							registradores16[3] = Short.valueOf(tokens.get(2));
						else if(tam == 32)
							registradores32[3] = Integer.valueOf(tokens.get(2));
						else if(tam == 64)
							registradores64[3] = Integer.valueOf(tokens.get(2));
					}
					break;
				default:
					break;
				}
			} else { //tokens(1) nao é um registrador, é um end de ram

			}

			break;
		case "add":

			break;
		case "inc":

			break;
		case "imul":

			break;
		}


	}

	public void pegarNaRam(String endereco, int tamInstrucao) {
		


	}

	public void recive() {
		ArrayList<String> tokens = new ArrayList<>();
		Dado d = barramento.reciveCPU();
		byte[] dados = d.getDados();
		
		
		if(d.getControle().equals(Constantes.RESPOSTA)) {
			//DECODIFICAÇÃO
			switch (Main.cpu.getTam()) {
			case 16:
				byte[] acao = Arrays.copyOfRange(dados, 0, 2);
				byte[] op1 = Arrays.copyOfRange(dados, 2, 4);
				byte[] op2 = null;
				byte[] op3 = null;

				if(dados.length > 6 && dados[5] != 0) {
					op2  = Arrays.copyOfRange(dados, 4, 6);
				}

				if(dados.length > 8 && dados[7] != 0) {
					op3 = Arrays.copyOfRange(dados, 6, 8);
				}

				int acao_int = fromTwoByteArray(acao);
				tokens.add(Constantes.getString(acao_int));
				int op1_int = fromTwoByteArray(op1);
				if(Validate.isRegistrador(Integer.toString(op1_int))) {
					tokens.add(Constantes.getString(op1_int));
				} else {
					tokens.add(Integer.toString(op1_int));
				}


				int op2_int;
				int op3_int;

				if(op2 != null) {
					op2_int = fromTwoByteArray(op2);
					tokens.add(Integer.toString(op2_int));
				}
				if(op3 != null) {
					op3_int = fromTwoByteArray(op3);
					tokens.add(Integer.toString(op3_int));
				}



				executarInstrucao(tokens);
				break;
			case 32:


				executarInstrucao(tokens);
				break;
			case 64:


				executarInstrucao(tokens);
				break;


			default:

			}
		} else if(d.getControle().equals(Constantes.KEY_INTERCEPTOR)) {
			
			pegarNaRam(d.getEndereco(), d.getDados().length);
			
			
		}

	}


	public int getTam() {
		return tam;
	}

	public void setTam(int tam) {
		this.tam = tam;
	}

	public Barramento getBarramento() {
		return barramento;
	}

	public void setBarramento(Barramento barramento) {
		this.barramento = barramento;
	}

	public static int fromTwoByteArray(byte[] bytes) {
		return ((bytes[0] & 0xFF) << 8 | (bytes[1] & 0xFF));
	}


	public static int fromThreeByteArray(byte[] bytes) {
		return (bytes[0] & 0xFF) << 16 | (bytes[1] & 0xFF) << 8 | (bytes[2] & 0xFF);
	}

	public static int fromFourByteArray(byte[] bytes) {
		return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
	}



}

package principal;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

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


	public void recive() {
		ArrayList<String> tokens = new ArrayList<>();
		Dado d = barramento.reciveCPU();
		byte[] dados = d.getDados();
		byte[] acao = null;
		byte[] op1 = null;
		byte[] op2 = null;
		byte[] op3 = null;
		
		
		
		if(d.getControle().equals(Constantes.RESPOSTA)) {
			
			//DECODIFICAÇÃO
			
			switch (Main.cpu.getTam()) {
			case 16:
				short acao_short;
				short op1_short;
				short op2_short;
				short op3_short;
				
				acao = Arrays.copyOfRange(dados, 0, 2);
				op1 = Arrays.copyOfRange(dados, 2, 4);
				op2 = null;
				op3 = null;

				if(dados.length > 5 && dados[5] != 0) {
					op2  = Arrays.copyOfRange(dados, 4, 6);
				}

				if(dados.length > 7 && dados[7] != 0) {
					op3 = Arrays.copyOfRange(dados, 6, 8);
				}

				acao_short = fromTwoByteArray(acao);
				tokens.add(Constantes.getString(acao_short));
				op1_short = fromTwoByteArray(op1);
				if(Validate.isRegistrador(Integer.toString(op1_short))) {
					tokens.add(Constantes.getString(op1_short));
				} else {
					tokens.add(Integer.toString(op1_short));
				}


				

				if(op2 != null) {
					op2_short = fromTwoByteArray(op2);
					tokens.add(Integer.toString(op2_short));
				}
				if(op3 != null) {
					op3_short = fromTwoByteArray(op3);
					tokens.add(Integer.toString(op3_short));
				}


				executarInstrucao(tokens);
				
				break;
			case 32:
				
				int acao_int;
				int op1_int;
				int op2_int;
				int op3_int;
				
				dados = Arrays.copyOfRange(dados, 0, 4);
				op1 = Arrays.copyOfRange(dados, 4, 8);
				op2 = null;
				op3 = null;

				if(dados.length > 11 && dados[11] != 0) {
					op2  = Arrays.copyOfRange(dados, 8, 12);
				}

				if(dados.length > 15 && dados[15] != 0) {
					op3 = Arrays.copyOfRange(dados, 12, 16);
				}

				acao_int = fromFourByteArray(acao);
				tokens.add(Constantes.getString(acao_int));
				op1_int = fromFourByteArray(op1);
				if(Validate.isRegistrador(Integer.toString(op1_int))) {
					tokens.add(Constantes.getString(op1_int));
				} else {
					tokens.add(Integer.toString(op1_int));
				}


				if(op2 != null) {
					op2_int = fromFourByteArray(op2);
					tokens.add(Integer.toString(op2_int));
				}
				if(op3 != null) {
					op3_int = fromFourByteArray(op3);
					tokens.add(Integer.toString(op3_int));
				}


				executarInstrucao(tokens);
				
				break;
			case 64:

				long acao_long;
				long op1_long;
				long op2_long;
				long op3_long;
				
				dados = Arrays.copyOfRange(dados, 0, 8);
				op1 = Arrays.copyOfRange(dados, 8, 16);
				op2 = null;
				op3 = null;

				if(dados.length > 23 && dados[23] != 0) {
					op2  = Arrays.copyOfRange(dados, 16, 24);
				}

				if(dados.length > 31 && dados[31] != 0) {
					op3 = Arrays.copyOfRange(dados, 24, 32);
				}

				acao_long = fromEightByteArray(acao);
				tokens.add(Constantes.getString(acao_long));
				op1_long = fromEightByteArray(op1);
				if(Validate.isRegistrador(Long.toString(op1_long))) {
					tokens.add(Constantes.getString(op1_long));
				} else {
					tokens.add(Long.toString(op1_long));
				}


				if(op2 != null) {
					op2_long = fromEightByteArray(op2);
					tokens.add(Long.toString(op2_long));
				}
				if(op3 != null) {
					op3_long = fromEightByteArray(op3);
					tokens.add(Long.toString(op3_long));
				}

				executarInstrucao(tokens);
				break;


			default:

			}
		} else if(d.getControle().equals(Constantes.KEY_INTERCEPTOR)) {
			
			String endereco = d.getEndereco();
			int tamInstrucao = d.getDados().length;
			
			switch(tam) {
			case 16:
				registradores16[4] = Short.parseShort(endereco); //PI
				barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_LER, new byte[tamInstrucao], endereco));
				break;

			case 32:
				registradores32[4] = Integer.parseInt(endereco);
				barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_LER, new byte[tamInstrucao], endereco));
				break;
			case 64:
				registradores64[4] = Long.parseLong(endereco);
				barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_LER, new byte[tamInstrucao], endereco));
				break;
			}

			
			
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

	public static short fromTwoByteArray(byte[] bytes) {
		ByteBuffer wrapped = ByteBuffer.wrap(bytes);
		short num = wrapped.getShort();
		return num;
	}

	public static int fromFourByteArray(byte[] bytes) {
		ByteBuffer wrapped = ByteBuffer.wrap(bytes); 
		int num = wrapped.getInt();
		return num;
	}
	
	public static long fromEightByteArray(byte[] bytes) {
		ByteBuffer wrapped = ByteBuffer.wrap(bytes); 
		long num = wrapped.getLong();
		return num;
	}

	

}

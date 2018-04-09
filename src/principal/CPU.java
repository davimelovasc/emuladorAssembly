package principal;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import utils.Constantes;
import utils.Helper;
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
			registradores16 = new short[5];
			tam = 16;
			Main.tamInstrucao = 8; //4 tokens de 2 bytes cada. ex: imul A B C
			Main.ponteiroBuffer = "0x0000";
			break;
		case 32:
			registradores32 = new int[5];
			tam = 32;
			Main.tamInstrucao = 16;
			Main.ponteiroBuffer = "0x000000";
			break;
		case 64:
			registradores64 = new long[5];
			tam = 64;
			Main.tamInstrucao = 32;
			Main.ponteiroBuffer = "0x00000000";
			break;
		}

	}



	/*public void executarInstrucao(ArrayList<String> tokens) {
		switch(tokens.get(0)) {
		case "mov":
			if(Validate.isRegistrador(tokens.get(1))) {
				switch (tokens.get(1)) {
				case "A":
					if(Validate.isEndRam(tokens.get(2))) { //tokens(2) é end de ram

						barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_LER, new byte[Main.cpu.tam/8], tokens.get(2) ));

						byte[] b = Main.ram.recive();

						if(tam == 16) {
							short x = fromTwoByteArray(b);
							registradores16[0] = (short) x;
						} else if(tam == 32) {
							int x = fromFourByteArray(b);
							registradores32[0] = Integer.valueOf(x);
						} else if(tam == 64) {
							long x = fromEightByteArray(b);
							registradores64[0] = Long.valueOf(x);
						}



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


	}*/


	public void recive() {
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
				int[] inst;
				short acao_short;
				short op1_short;
				short op2_short = 0;
				short op3_short = 0;

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

				op1_short = fromTwoByteArray(op1);

				if(op2 != null) {
					op2_short = fromTwoByteArray(op2);
				}
				if(op3 != null) {
					op3_short = fromTwoByteArray(op3);
				}

				inst = new int[4];
				inst[0] = acao_short;
				inst[1] = op1_short;
				inst[2] = op2_short;
				inst[3] = op3_short;




				executarInstrucoes(Helper.reductArray(inst)); //tira os espacos vazios


				break;
			case 32:

				int acao_int;
				int op1_int;
				int op2_int = 0;
				int op3_int = 0;

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
				op1_int = fromFourByteArray(op1);

				if(op2 != null) {
					op2_int = fromFourByteArray(op2);

				}
				if(op3 != null) {
					op3_int = fromFourByteArray(op3);
				}

				inst = new int[4];
				inst[0] = acao_int;
				inst[1] = op1_int;
				inst[2] = op2_int;
				inst[3] = op3_int;

				executarInstrucoes(Helper.reductArray(inst));


				break;
			case 64:

				long acao_long;
				long op1_long;
				long op2_long = 0;
				long op3_long = 0;

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

				op1_long = fromEightByteArray(op1);

				if(op2 != null) {
					op2_long = fromEightByteArray(op2);

				}
				if(op3 != null) {
					op3_long = fromEightByteArray(op3);

				}

				inst = new int[4];
				inst[0] = (int) acao_long;
				inst[1] = (int) op1_long;
				inst[2] = (int) op2_long;
				inst[3] = (int) op3_long;

				executarInstrucoes(Helper.reductArray(inst));

				break;

			default:

			}
		} else if(d.getControle().equals(Constantes.KEY_INTERCEPTOR)) {

			String endereco = d.getEndereco();
			int tamInstrucao = d.getDados().length;
			int enderecoFormatado = Helper.formatarEndereco(endereco);

			switch(tam) {
			case 16:
				registradores16[4] = (short) enderecoFormatado; //PI é atualizado
				barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_LER, new byte[tamInstrucao], endereco, false));
				break;

			case 32:
				registradores32[4] = enderecoFormatado; //PI é atualizado
				barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_LER, new byte[tamInstrucao], endereco, false));
				break;
			case 64:
				registradores64[4] = enderecoFormatado; //PI é atualizado
				barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_LER, new byte[tamInstrucao], endereco, false));
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

	public static int stringToNum(String a) {
		switch(a.toUpperCase()) {
		case "A":
			return 0;
		case "B":
			return 1;
		case "C":
			return 2;
		case "D":
			return 3;
		case "PI":
			return 4;
		}
		return 99;
	}



	public void executarInstrucoes(int... a) {

		switch(a[0]) {
		case Constantes.MOV_INT: 
			//a[1] = a[2];
			execMov(a);
			break;
		case Constantes.ADD_INT:
			//a[1] = a[1] + a[2];
			execAdd(a);
			break;
		case Constantes.INC_INT:
			//a[1]++;
			execInc(a);
			break;
		case Constantes.IMUL_INT:
			
			break;
		}


	}

	public void execAdd(int...a){
		if(Validate.isRegistrador(Integer.toString(a[1]))) { //a[1] é registrador
			int x = Constantes.NumRegOnVetor(a[1]);
			if(! Validate.isRegistrador(Integer.toString(a[2]))) { //a[2] é numero
				if(tam == 16) {
					registradores16[x] += (short) a[2];
				} else if(tam == 32) {
					registradores32[x] += a[2];
				} else if(tam == 64) {
					registradores64[x] += (long) a[2];
				}
			} else { //a[2] é registrador
				int y = Constantes.NumRegOnVetor(a[2]);
				if(tam == 16) {
					registradores16[x] += registradores16[y];
				} else if(tam == 32) {
					registradores32[x] += registradores32[y];
				} else if(tam == 64) {
					registradores64[x] += registradores64[y];
				}


			}

		}else { //a[1] é end. ram

			if(Validate.isRegistrador(Integer.toString(a[2]))) {//a[2] é registrador
				int x = Constantes.NumRegOnVetor(a[1]);
				long z;
				byte[] b = null;
				if(tam == 16) {
					z = registradores16[x];
					b = Encoder.toByteArray((byte) z);
				} else if(tam == 32) {
					z = registradores32[x];
					b = Encoder.toByteArray((int) z);
				} else if(tam == 64) {
					z = registradores64[x];
					b = Encoder.toByteArray(z);
				}

				Main.barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_ATUALIZAR, b, Integer.toString(a[1]), true )); //verificar
				Main.ram.recive();
			} else { //a[2] é numero
				byte[] b = null;
				if(tam == 16) {
					b = Encoder.toByteArray((short) a[2]);

				} else if(tam == 32) {
					b = Encoder.toByteArray(a[2]);

				} else if(tam == 64) {

					b = Encoder.toByteArray((long) a[2]);
				}

				Main.barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_ATUALIZAR, b, Integer.toString(a[1]), true )); //verificar
				Main.ram.recive();
			}


		}


	}

	public void execInc(int...a) {
		if(Validate.isRegistrador(Integer.toString(a[1]))) { //a[1] é registrador
			int x = Constantes.NumRegOnVetor(a[1]);

			if(tam == 16) {
				registradores16[x] ++;
			}else if(tam == 32) {
				registradores32[x] ++;
			} else if(tam == 64) {
				registradores64[x] ++;
			}

		} else { //a[1] é end. de ram
			byte[] b  = {1};
			barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_ATUALIZAR, b, Integer.toString(a[1]), true));
			Main.ram.recive();
		}

	}

	public void execMov(int... a) {
		if(Validate.isRegistrador(Integer.toString(a[1]))) { //a[1] é registrador
			int x = Constantes.NumRegOnVetor(a[1]);
			if(! Validate.isRegistrador(Integer.toString(a[2]))) { //a[2] é numero
				if(tam == 16) {
					registradores16[x] = (short) a[2];
				}else if(tam == 32) {
					registradores32[x] = (int) a[2];
				} else if(tam == 64) {
					registradores64[x] = (long) a[2];
				}
			} else { //a[2] for um registrador
				int y = Constantes.NumRegOnVetor(a[2]);
				if(tam == 16) {
					registradores16[x] = registradores16[y];
				}else if(tam == 32) {
					registradores32[x] = registradores32[y];
				} else if(tam == 64) {
					registradores64[x] = registradores64[y];
				}
			}
		} else { //a[1] é end. ram
			if(Validate.isRegistrador(Integer.toString(a[2]))) {//a[2] é registrador
				int y = Constantes.NumRegOnVetor(a[2]);
				if(tam == 16) {
					byte[] b = Encoder.toByteArray(registradores16[y]);
					Main.barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_ESCREVER, b, Integer.toString(a[1]), true));
					Main.ram.recive(); //executa
				}else if(tam == 32) {
					byte[] b = Encoder.toByteArray(registradores32[y]);
					Main.barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_ESCREVER, b, Integer.toString(a[1]), true));
					Main.ram.recive(); //executa
				}else if(tam == 64) {
					byte[] b = Encoder.toByteArray(registradores64[y]);
					Main.barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_ESCREVER, b, Integer.toString(a[1]), true));
					Main.ram.recive(); //executa
				}

			} else { //a[2] é numero
				byte[] num = Encoder.toByteArray(a[2]);
				Main.barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_ESCREVER, num, Integer.toString(a[1])));
				Main.ram.recive();
			}
		}

	}

}

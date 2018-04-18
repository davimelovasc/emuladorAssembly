package hardware;

import java.nio.ByteBuffer;
import java.util.Arrays;

import entradaDeDados.Encoder;
import principal.Main;
import utils.Constantes;
import utils.Helper;
import utils.Logger;
import utils.Validate;

public class CPU {

	private long[] registradores64;
	private int[] registradores32;
	private short[] registradores16;
	private int tam;
	//private Barramento barramento;
	private ControlBus controlBus;
	private DataBus dataBus;
	private AddressBus addressBus;;

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
			Main.ponteiroBuffer = "0x00000000";
			break;
		case 64:
			registradores64 = new long[5];
			tam = 64;
			Main.tamInstrucao = 32;
			Main.ponteiroBuffer = "0x0000000000000000";
			break;
		}

	}


	public long[] getRegistradores64() {
		return registradores64;
	}


	public void setRegistradores64(long[] registradores64) {
		this.registradores64 = registradores64;
	}





	public int[] getRegistradores32() {
		return registradores32;
	}





	public void setRegistradores32(int[] registradores32) {
		this.registradores32 = registradores32;
	}





	public short[] getRegistradores16() {
		return registradores16;
	}





	public void setRegistradores16(short[] registradores16) {
		this.registradores16 = registradores16;
	}


	public void recive() {
		String controle = controlBus.reciveCPU();
		byte[] d = dataBus.reciveCPU();
		String endereco = addressBus.reciveCPU();
		
		//Dado d = barramento.reciveCPU();
		byte[] dados = d;
		byte[] acao = null;
		byte[] op1 = null;
		byte[] op2 = null;
		byte[] op3 = null;



		if(controle.equals(Constantes.RESPOSTA)) {

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



				executarInstrucoes(inst);


				break;
			case 32:

				int acao_int;
				int op1_int;
				int op2_int = 0;
				int op3_int = 0;

				acao = Arrays.copyOfRange(dados, 0, 4);
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

		
				executarInstrucoes(inst);


				break;
			case 64:

				long acao_long;
				long op1_long;
				long op2_long = 0;
				long op3_long = 0;

				acao = Arrays.copyOfRange(dados, 0, 8);
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


				executarInstrucoes(inst);

				break;

			default:

			}
		} else if(controle.equals(Constantes.KEY_INTERCEPTOR)) {

			
			int tamInstrucao = d.length;
			int enderecoFormatado = Helper.formatarEndereco(endereco);


			switch(tam) {
			case 16:
				registradores16[4] = (short) enderecoFormatado; //PI é atualizado
				System.out.println("\nRegistrador PI atualizado!\nPI: "+ registradores16[4]);
				//barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_LER, new byte[tamInstrucao], endereco, false));
				
				break;

			case 32:
				registradores32[4] = enderecoFormatado; //PI é atualizado
				System.out.println("\nRegistrador PI atualizado!\nPI: "+ registradores32[4]);
				//barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_LER, new byte[tamInstrucao], endereco, false));
				break;
			case 64:
				registradores64[4] = enderecoFormatado; //PI é atualizado
				System.out.println("\nRegistrador PI atualizado!\nPI: "+ registradores64[4]);
				//barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_LER, new byte[tamInstrucao], endereco, false));
				break;
			}
			
			controlBus.send(Constantes.CPU, Constantes.RAM, Constantes.KEY_LER);
			dataBus.send(Constantes.CPU, Constantes.RAM, new byte[tamInstrucao]);
			addressBus.send(Constantes.CPU, Constantes.RAM, endereco, false);



		}

	}


	public int getTam() {
		return tam;
	}

	public void setTam(int tam) {
		this.tam = tam;
	}

/*	public Barramento getBarramento() {
		return barramento;
	}

	public void setBarramento(Barramento barramento) {
		this.barramento = barramento;
	}*/

	//DECODER

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

			System.out.println("\nEXECUTOU MOV\n");

			break;
		case Constantes.ADD_INT:
			//a[1] = a[1] + a[2];
			execAdd(a);
			System.out.println("\nEXECUTOU ADD\n");
			break;
		case Constantes.INC_INT:
			//a[1]++;
			execInc(a);
			System.out.println("\nEXECUTOU INC\n");
			break;
		case Constantes.IMUL_INT:
			execImul(a);
			System.out.println("\nEXECUTOU IMUL\n");
			break;
		}

		Helper.removerInstrucaoRam();
		Logger.printFeedBack();

	}

	public void execImul(int...a) {

		if(a[1] < 0) { //a[1] é registrador

			if(a[2] >= 0) { //a[2] é end. ram e [3] é numero
				//barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_LER, new byte[Main.cpu.tam/8], Integer.toString(a[2]), true));
				
				controlBus.send(Constantes.CPU, Constantes.RAM, Constantes.KEY_LER);
				dataBus.send(Constantes.CPU, Constantes.RAM, new byte[Main.cpu.tam/8]);
				addressBus.send(Constantes.CPU, Constantes.RAM, Integer.toString(a[2]), true );
				
				
				byte[] b = Main.ram.recive();
				int x = Constantes.NumRegOnVetor(a[1]);
				if(tam == 16) {
					registradores16[x] = (short) (fromTwoByteArray(b) * a[3]);
				} else if(tam == 32) {
					registradores32[x] = fromFourByteArray(b) * a[3];

				} else if(tam == 64) {
					registradores64[x] = (long) (fromEightByteArray(b) * a[3]);
				}
			}else { //[2] é registrador

				int x = Constantes.NumRegOnVetor(a[1]);
				int y = Constantes.NumRegOnVetor(a[2]);

				if(a[3] < 0) { //a[3] é registrador

					int z = Constantes.NumRegOnVetor(a[3]);

					if(tam == 16) {
						registradores16[x] = (short) (registradores16[y] * registradores16[z]);
					} else if(tam == 32) {
						registradores32[x] = registradores32[y] * registradores32[z];
					} else if(tam == 64) {
						registradores64[x] = registradores64[y] * registradores64[z];
					}
				} else { //a[3] é numero

					if(tam == 16) {
						registradores16[x] = (short) (registradores16[y] * a[3]);
					} else if(tam == 32) {
						registradores32[x] = registradores32[y] * a[3];
					} else if(tam == 64) {
						registradores64[x] = registradores64[y] * a[3];
					}

				}
			}
		} else { //a[1] é end., a[2] é numero
			if(a[3] >= 0) { //a[3] é numero
				byte[] b = null;
				if(tam == 16) {
					short s = (short) (a[2] * a[3]);
					b = Encoder.toByteArray(s);
				} else if(tam == 32) {
					int i = a[2] * a[3];
					b = Encoder.toByteArray(i);
				} else if(tam == 64) {
					long l = a[2] * a[3];
					b = Encoder.toByteArray(l);
				}

				//Main.barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_ESCREVER, b, Integer.toString(a[1]), true));
				
				controlBus.send(Constantes.CPU, Constantes.RAM, Constantes.KEY_ESCREVER);
				dataBus.send(Constantes.CPU, Constantes.RAM, b);
				addressBus.send(Constantes.CPU, Constantes.RAM, Integer.toString(a[1]), true );
				
				Main.ram.recive();

			} else { //a[3] é registrador
				int z = Constantes.NumRegOnVetor(a[3]);
				byte[] b = null;
				if(tam == 16) {
					short s = (short) (a[2] * registradores16[z]);
					b = Encoder.toByteArray(s);

				} else if(tam == 32) {
					int i = a[2] * registradores32[z];
					b = Encoder.toByteArray(i);
				} else if(tam == 64) {
					long l = a[2] * registradores64[z];
					b = Encoder.toByteArray(l);
				}

				//Main.barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_ESCREVER, b, Integer.toString(a[1]), true));
				
				controlBus.send(Constantes.CPU, Constantes.RAM, Constantes.KEY_ESCREVER);
				dataBus.send(Constantes.CPU, Constantes.RAM, b);
				addressBus.send(Constantes.CPU, Constantes.RAM, Integer.toString(a[1]), true );
				
				Main.ram.recive();
			}
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

				//Main.barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_LER, new byte[Main.cpu.tam/8], Integer.toString(a[1]), true ));
				
				controlBus.send(Constantes.CPU, Constantes.RAM, Constantes.KEY_LER);
				dataBus.send(Constantes.CPU, Constantes.RAM, new byte[Main.cpu.tam/8]);
				addressBus.send(Constantes.CPU, Constantes.RAM, Integer.toString(a[1]), true );
				
				byte b[] = Main.ram.recive();
				int x = Constantes.NumRegOnVetor(a[2]);
				byte[] result = null;

				if(tam == 16) {
					short s = fromTwoByteArray(b);
					short res = (short) (s + registradores16[x]);
					result = Encoder.toByteArray(res);
				} else if(tam == 32) {
					int i = fromFourByteArray(b);
					int res = i + registradores32[x];
					result = Encoder.toByteArray(res);
				} else if(tam == 64) {
					long l = fromEightByteArray(b);
					long res = l + registradores64[x];
					result = Encoder.toByteArray(res);
				}

				//Main.barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_ESCREVER, result, Integer.toString(a[1]), true ));
				
				controlBus.send(Constantes.CPU, Constantes.RAM, Constantes.KEY_ESCREVER);
				dataBus.send(Constantes.CPU, Constantes.RAM, result);
				addressBus.send(Constantes.CPU, Constantes.RAM, Integer.toString(a[1]), true );
				
				Main.ram.recive();

			} else { //a[2] é numero

				//Main.barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_LER, new byte[Main.cpu.tam/8], Integer.toString(a[1]), true ));
				
				controlBus.send(Constantes.CPU, Constantes.RAM, Constantes.KEY_LER);
				dataBus.send(Constantes.CPU, Constantes.RAM, new byte[Main.cpu.tam/8]);
				addressBus.send(Constantes.CPU, Constantes.RAM, Integer.toString(a[1]), true );
				
				byte b[] = Main.ram.recive();
				byte[] result = null;

				if(tam == 16) {
					short s = fromTwoByteArray(b);
					short res = (short) (s + a[2]);
					result = Encoder.toByteArray(res);
				} else if(tam == 32) {
					int i = fromFourByteArray(b);
					int res = i + a[2];
					result = Encoder.toByteArray(res);
				} else if(tam == 64) {
					long l = fromEightByteArray(b);
					long res = (long) (l + a[2]);
					result = Encoder.toByteArray(res);
				}

				//Main.barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_ESCREVER, result, Integer.toString(a[1]), true ));
				
				controlBus.send(Constantes.CPU, Constantes.RAM, Constantes.KEY_ESCREVER);
				dataBus.send(Constantes.CPU, Constantes.RAM, result);
				addressBus.send(Constantes.CPU, Constantes.RAM, Integer.toString(a[1]), true );
				
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
			//Main.barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_LER, new byte[Main.cpu.tam/8], Integer.toString(a[1]), true ));
			
			controlBus.send(Constantes.CPU, Constantes.RAM, Constantes.KEY_LER);
			dataBus.send(Constantes.CPU, Constantes.RAM, new byte[Main.cpu.tam/8]);
			addressBus.send(Constantes.CPU, Constantes.RAM, Integer.toString(a[1]), true );
			
			byte b[] = Main.ram.recive();
			byte[] result = null;
			if(tam == 16) {
				short s = fromTwoByteArray(b);
				short res = (short) (s + 1);
				result = Encoder.toByteArray(res);
			} else if(tam == 32) {
				int i = fromFourByteArray(b);
				int res = i + 1;
				result = Encoder.toByteArray(res);
			} else if(tam == 64) {
				long l = fromEightByteArray(b);
				long res =(long) (l + 1);
				result = Encoder.toByteArray(res);
			}

			//Main.barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_ESCREVER, result, Integer.toString(a[1]), true ));
			
			controlBus.send(Constantes.CPU, Constantes.RAM, Constantes.KEY_ESCREVER);
			dataBus.send(Constantes.CPU, Constantes.RAM, result);
			addressBus.send(Constantes.CPU, Constantes.RAM, Integer.toString(a[1]), true );
			
			Main.ram.recive();

		}

	}

	public void execMov(int... a) {
		if(Validate.isRegistrador(Integer.toString(a[1]))) { //a[1] é registrador
			int x = Constantes.NumRegOnVetor(a[1]);
			if(! Validate.isRegistrador(Integer.toString(a[2]))) { //a[2] é numero ou end
				
				if(a[3] == 1) { //a[2] é end. ram
					//Main.barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_LER, new byte[Main.cpu.tam/8], Integer.toString(a[2]), true ));
					controlBus.send(Constantes.CPU, Constantes.RAM, Constantes.KEY_LER);
					dataBus.send(Constantes.CPU, Constantes.RAM, new byte[Main.cpu.tam/8]);
					addressBus.send(Constantes.CPU, Constantes.RAM, Integer.toString(a[2]), true);
					
					byte[] r = Main.ram.recive();
					
					if(tam == 16) {
						registradores16[x] = fromTwoByteArray(r);
					}else if(tam == 32) {
						registradores32[x] = fromFourByteArray(r);
					} else if(tam == 64) {
						registradores64[x] = fromEightByteArray(r);
					}
					
				
				}else { //a[2] é numero
					
					if(tam == 16) {
						registradores16[x] = (short) a[2];
					}else if(tam == 32) {
						registradores32[x] = (int) a[2];
					} else if(tam == 64) {
						registradores64[x] = (long) a[2];
					}
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
				byte[] b = null;
				if(tam == 16) {
					b = Encoder.toByteArray(registradores16[y]);
					//Main.barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_ESCREVER, b, Integer.toString(a[1]), true));
					//Main.ram.recive(); //executa
				}else if(tam == 32) {
					b = Encoder.toByteArray(registradores32[y]);
					//Main.barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_ESCREVER, b, Integer.toString(a[1]), true));
					//Main.ram.recive(); //executa
				}else if(tam == 64) {
					b = Encoder.toByteArray(registradores64[y]);
					//Main.barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_ESCREVER, b, Integer.toString(a[1]), true));
					//Main.ram.recive(); //executa
				}
				
				controlBus.send(Constantes.CPU, Constantes.RAM, Constantes.KEY_ESCREVER);
				dataBus.send(Constantes.CPU, Constantes.RAM, b);
				addressBus.send(Constantes.CPU, Constantes.RAM, Integer.toString(a[1]), true);
				
				Main.ram.recive();  //executa

			} else { //a[2] é numero
				byte[] num = null;
				if(Main.cpu.tam == 16) {
					num = Encoder.toByteArray((short) a[2]);
				}else if(Main.cpu.tam == 32) {
					num = Encoder.toByteArray((int) a[2]);
				}else if(Main.cpu.tam == 64) {
					num = Encoder.toByteArray((long) a[2]);
				}

				//Main.barramento.send(Constantes.CPU, Constantes.RAM, new Dado(Constantes.KEY_ESCREVER, num, Integer.toString(a[1]), true));
				
				controlBus.send(Constantes.CPU, Constantes.RAM, Constantes.KEY_ESCREVER);
				dataBus.send(Constantes.CPU, Constantes.RAM, num);
				addressBus.send(Constantes.CPU, Constantes.RAM, Integer.toString(a[1]), true);
				
				Main.ram.recive();
			}
		}

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





}

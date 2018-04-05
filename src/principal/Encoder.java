package principal;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import utils.Constantes;
import utils.Helper;

public class Encoder {

	public Encoder() {
	}

	public static byte[] encode(ArrayList<String> instrucoes) {
		
		/*ByteBuffer c;
		
		if(Main.cpu.getTam() == 16) {
			c = ByteBuffer.allocate(2);
			
			
			
			
		}else if(Main.cpu.getTam() == 32) {
			c = ByteBuffer.allocate(4);
			
			
		}else if(Main.cpu.getTam() == 64) {
			c = ByteBuffer.allocate(8);
			
			
		}*/
		
		
		byte[] resposta = new byte[Main.entradaESaida.getBuffer().length];
		byte[] respostaRed = null;
		byte[] aux;
		// ArrayList<Byte> b = new ArrayList<>();
		int op1 = 0, op2 = 0, op3 = 0, op4 = 0;

		int acao = Constantes.getKey(instrucoes.get(0));

		op1 = Constantes.getKey(instrucoes.get(1));

		if (instrucoes.get(2) != null) {
			op2 = Constantes.getKey(instrucoes.get(2));

		}

		if (instrucoes.get(3) != null) {
			op3 = Constantes.getKey(instrucoes.get(3));

		}

		if (instrucoes.get(4) != null) {
			op4 = Constantes.getKey(instrucoes.get(4));

		}

		switch (Main.cpu.getTam()) {
		// duvida: o Buffer de e/s tem que conseguir armazenar todas as tokens de uma
		// vez ?
		case 16:

			aux = toTwoByteArray(acao); // duvida: o array b so podera guardar informação por vez? pode criar um array
										// pracada info?
			Helper.saveAtoB(aux, resposta);

			aux = toTwoByteArray(op1);
			Helper.saveAtoB(aux, resposta);

			if (resposta.length >= 6 && op2 != 0) {
				aux = toTwoByteArray(op2);
				Helper.saveAtoB(aux, resposta);
			}

			if (resposta.length >= 8 && op3 != 0) {
				aux = toTwoByteArray(op3);
				Helper.saveAtoB(aux, resposta);
			}

			if (resposta.length >= 10 && op4 != 0) {
				aux = toTwoByteArray(op4);
				Helper.saveAtoB(aux, resposta);
			}
			
			respostaRed = Helper.reductArray(resposta);

			break;
		case 32:
			aux = toFourByteArray(acao);
			Helper.saveAtoB(aux, resposta);

			aux = toFourByteArray(op1);
			Helper.saveAtoB(aux, resposta);

			if (resposta.length >= 6 && op2 != 0) {
				aux = toFourByteArray(op2);
				Helper.saveAtoB(aux, resposta);
			}

			if (resposta.length >= 8 && op3 != 0) {
				aux = toFourByteArray(op3);
				Helper.saveAtoB(aux, resposta);
			}

			if (resposta.length >= 10 && op4 != 0) {
				aux = toFourByteArray(op4);
				Helper.saveAtoB(aux, resposta);
			}
			
			 respostaRed = Helper.reductArray(resposta);

			break;


		case 64:
			aux = toEightByteArray(acao); // duvida: o array b so podera guardar informação por vez? pode criar um array
			// pracada info?
			Helper.saveAtoB(aux, resposta);

			aux = toEightByteArray(op1);
			Helper.saveAtoB(aux, resposta);

			if (resposta.length >= 6 && op2 != 0) {
				aux = toEightByteArray(op2);
				Helper.saveAtoB(aux, resposta);
			}

			if (resposta.length >= 8 && op3 != 0) {
				aux = toEightByteArray(op3);
				Helper.saveAtoB(aux, resposta);
			}

			if (resposta.length >= 10 && op4 != 0) {
				aux = toEightByteArray(op4);
				Helper.saveAtoB(aux, resposta);
			}
			
			respostaRed = Helper.reductArray(resposta);


			break;
		}

		return respostaRed;
	}

	public static byte[] toByteArray(int value) {
		return ByteBuffer.allocate(4).putInt(value).array();
	}

	public static byte[] toTwoByteArray(int value) {
		return new byte[] { (byte) (value >> 8), (byte) value };
	}

	public static byte[] toThreeByteArray(int value) {
		return new byte[] { (byte) (value >> 16), (byte) (value >> 8), (byte) value };
	}

	public static byte[] toFourByteArray(int value) {
		return new byte[] { (byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) value };
	}
	
	public static byte[] toEightByteArray(long value) {
		return new byte[] { (byte) (value >> 32), (byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) value };
	}

}

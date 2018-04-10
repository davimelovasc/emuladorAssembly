package principal;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import javax.swing.plaf.synth.SynthSeparatorUI;

import utils.Constantes;
import utils.Helper;
import utils.Validate;

public class Encoder {

	public Encoder() {
	}

	public static byte[] encode(ArrayList<String> instrucoes) {

		byte[] resposta = new byte[Main.cpu.getTam()/8];
		byte[] aux;
		// ArrayList<Byte> b = new ArrayList<>();
		int op1 = 0, op2 = 0, op3 = 0;
		
		int acao = Constantes.getKey(instrucoes.get(0));

		if(Validate.isEndRam(instrucoes.get(1))) {
			op1 = Helper.formatarEndereco(instrucoes.get(1));
		}else {
			op1 = Constantes.getKey(instrucoes.get(1));
		}
		
		

		if (instrucoes.size() > 2) {
			op2 = Constantes.getKey(instrucoes.get(2));

		}

		if (instrucoes.size() > 3) {
			op3 = Constantes.getKey(instrucoes.get(3));
		}

		

		switch (Main.cpu.getTam()) {
		case 16:

			aux = toTwoByteArray(acao); 
			resposta = aux;

			aux = toTwoByteArray(op1);
			resposta = Helper.concatTwoArray(aux, resposta);

			if (op2 != 0) {
				aux = toTwoByteArray(op2);
				resposta = Helper.concatTwoArray(aux, resposta);
			} else {
				aux = toTwoByteArray((short) 0);
				resposta = Helper.concatTwoArray(aux, resposta);
			}
			
			if (op3 != 0) {
				aux = toTwoByteArray(op3);
				resposta = Helper.concatTwoArray(aux, resposta);
			} else {
				aux = toTwoByteArray((short) 0);
				resposta = Helper.concatTwoArray(aux, resposta);
			}
			

			
			

			break;
		case 32:
			aux = toFourByteArray(acao);
			resposta = aux;

			aux = toFourByteArray(op1);
			resposta = Helper.concatTwoArray(aux, resposta);
			
			
			if (op2 != 0) {
				aux = toFourByteArray(op2);
				resposta = Helper.concatTwoArray(aux, resposta);
			} else {
				aux = toFourByteArray((int) 0);
				resposta = Helper.concatTwoArray(aux, resposta);
			}
			

			if (op3 != 0) {
				aux = toFourByteArray(op3);
				resposta = Helper.concatTwoArray(aux, resposta);
			} else {
				aux = toFourByteArray((int) 0);
				resposta = Helper.concatTwoArray(aux, resposta);
			}
			

			
			
			break;


		case 64:
			aux = toEightByteArray(acao);
			
			resposta = aux;

			aux = toEightByteArray(op1);
			resposta = Helper.concatTwoArray(aux, resposta);

			if (op2 != 0) {
				aux = toEightByteArray(op2);
				resposta = Helper.concatTwoArray(aux, resposta);
			} else {
				aux = toEightByteArray((long) 0);
				resposta = Helper.concatTwoArray(aux, resposta);
			}
			

			if (op3 != 0) {
				aux = toEightByteArray(op3);
				resposta = Helper.concatTwoArray(aux, resposta);
			} else {
				aux = toEightByteArray((long) 0);
				resposta = Helper.concatTwoArray(aux, resposta);
			}

			
		
			break;
		}

		return resposta;
	}

	public static byte[] toByteArray(int value) {
		return ByteBuffer.allocate(4).putInt(value).array();
	}

	public static byte[] toByteArray(short value) {
		return ByteBuffer.allocate(2).putShort(value).array();
	}

	public static byte[] toByteArray(long value) {
		return ByteBuffer.allocate(8).putLong(value).array();
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
		ByteBuffer buffer = ByteBuffer.allocate(8);
	    buffer.putLong(value);
	    return buffer.array();
	}

}

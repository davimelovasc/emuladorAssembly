package utils;

import principal.Main;

public class Helper {

	public static boolean isAInstrucao(String instrucao) {
		if (instrucao.equalsIgnoreCase("mov") || instrucao.equalsIgnoreCase("add") || instrucao.equalsIgnoreCase("inc")
				|| instrucao.equalsIgnoreCase("imul")) {
			return true;
		}
		return false;
	}

	public static String fromInt(int i) {
		switch (i) {
		case 250:
			return "mov";
		case 251:
			return "add";
		case 252:
			return "inc";
		case 253:
			return "imul";

		default:
			return Integer.toString(i);
		}
	}

	public static void saveAtoB(byte[] a, byte[] b) {
		int aux = 0;
		if (b.length >= a.length) {
			for (int i = 0; i < b.length; i++) {
				if (b[i] == 0) {
					for (byte c : a) {
						try {
							b[i + aux] = c;
							aux++;
						} catch (NullPointerException e) {
							Logger.printError("Encoder",
									"Erro: Insctruções muito grande para armazenar no buffer de Entrada e Saída");
						}
					}
					break;
				}
			}
		}

	}

	public static int formatarEndereco(String endereco) { // retorna a string correspondente ao hexadecimal
		endereco = endereco.trim();
		String resultado = endereco.substring(2); // ex.: entrada: 0x00F2 sa�da: 00F2
		return hexaToDec(resultado);
	}

	public static int hexaToDec(String hexa) {
		int rs = Integer.parseInt(hexa.trim(), 16);
		return rs;
	}
	
	public static byte[] reductArray(byte[] b) {
		int x = 0;
		byte[] c;
		for (int i = 0; i < b.length; i++) {
			if(b[i] == 0) {
				c = new byte[x];
				for (int j = 0; j < c.length; j++) {
					c[j] = b[j];
				}
				return c;
				
			}else {
				x++;
			}
		}
		
		return null;
	}

}

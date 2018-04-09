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

	/*public static String fromInt(int i) {
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
	}*/
	
	public static byte[] invertArray(byte[] a) {
		for(int i = 0; i < a.length / 2; i++){
		    byte temp = a[i];
		    a[i] = a[a.length - i - 1];
		   a[a.length - i - 1] = temp;
		}
		return a;
	}
	
	public static byte[] concatTwoArray(byte[] b, byte[] a) {
		byte[] c = new byte[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
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
		if(endereco.contains("x")) {
		endereco = endereco.substring(2); // ex.: entrada: 0x00F2 sa�da: 00F2
		return hexaToDec(endereco);
		}
		
		return Integer.parseInt(endereco);
		
	
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
	
	public static int[] reductArray(int[] b) {
		int x = 0;
		int[] c;
		for (int i = 0; i < b.length; i++) {
			if(b[i] == 0) {
				c = new int[x];
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
	
	public static boolean validarEndereco(String endereco) {
		switch (Main.cpu.getTam()) {
		case 16:
			if(endereco.length() == 6) { //ex: 0x0001
				return true;
			}
			return false;
		case 32:
			if(endereco.length() == 7) { 
				return true;
			}
		case 64:
			if(endereco.length() == 8) {
				return true;
			}

		default:
			System.out.println("Valor de memoria informado é diferente do tam. da palavra");
			return false;
		}
	}
	
	public static void printArrayByte(byte[] b) {
		for (int i = 0; i < b.length; i++) {
			System.out.println(b[i]);
		}
	}

}

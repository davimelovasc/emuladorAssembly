package utils;

import principal.Main;

public class Constantes {
	
	public static final String MOD_ENTRADA_E_SAIDA = "Modulo de Entrada e Saída";
	public static final String RAM = "Ram";
	public static final String CPU = "Cpu";
	public static final String RESPOSTA = "Resposta";
	
	public static final int REGISTRADOR_A_INT = 100;
	public static final int REGISTRADOR_B_INT = 101;
	public static final int REGISTRADOR_C_INT = 102;
	public static final int REGISTRADOR_D_INT = 103;
	public static final int REGISTRADOR_PI_INT = 104;
	
	public static final int MOV_INT = 50;
	public static final int ADD_INT = 51;
	public static final int INC_INT = 52;
	public static final int IMUL_INT = 53;
	
	
	
	
	public static final String KEY_LER = "ler"; 
	public static final String KEY_ESCREVER = "escrever"; 
	public static final String KEY_INTERCEPTOR = "interceptor";
	
	
/*	public String getKey(int i) {
		Constantes.class.getField()
	}*/
	
	public static String getString(int i) {
		switch (i) {
		case MOV_INT:
			return "mov";
		case ADD_INT:
			return "add";
		case INC_INT:
			return "inc";
		case IMUL_INT:
			return "imul";
		case REGISTRADOR_A_INT:
			return "A";
		case REGISTRADOR_B_INT:
			return "B";
		case REGISTRADOR_C_INT:
			return "C";
		case REGISTRADOR_D_INT:
			return "D";
		case REGISTRADOR_PI_INT:
			return "PI";

		default:
			return "";
		}
	}

	public static int getKey(String s) {
		switch(s) {
		case "A":
			return REGISTRADOR_A_INT;
		case "B":
			return REGISTRADOR_B_INT;
		case "C":
			return REGISTRADOR_C_INT;
		case "D":
			return REGISTRADOR_D_INT;
		case "PI":
			return REGISTRADOR_PI_INT;
		case "mov":
			return MOV_INT;
		case "add":
			return ADD_INT;
		case "inc":
			return INC_INT;
		case "imul":
			return IMUL_INT;
		default:
			return Integer.parseInt(s);
		}
	}
	

	
}

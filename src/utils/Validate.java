package utils;

import hardware.AddressBus;
import hardware.CPU;
import hardware.ControlBus;
import hardware.DataBus;
import hardware.EntradaESaida;
import hardware.Ram;

public class Validate {

	public static boolean validateMov(String a, String b, String c) {
		if(! a.toLowerCase().equals("mov"))
			return false;


		if(! isRegistrador(b) && ! isEndRam(b)) {
			return false;
		}
		
		if(! isInteger(c) && ! isRegistrador(c) && ! isEndRam(c))
			return false;
		
		return true;
	}

	public static boolean validateAdd(String a, String b, String c) {
	    
		if(! a.toLowerCase().equals("add"))
			return false;
		
		if(! isRegistrador(b) && ! isEndRam(b))
			return false;
		
		if(! isInteger(c) && ! isRegistrador(c))
			return false;
		
		return true;
	}

	public static boolean validateInc(String a, String b) {
		if(! a.toLowerCase().equals("inc"))
			return false;
		
		if(! isRegistrador(b) && ! isEndRam(b))
			return false;

		return true;
	}

	public static boolean validateImul(String a, String b, String c, String d) {
		if(! a.toLowerCase().equals("imul"))
			return false;

		if(! isRegistrador(b) && ! isEndRam(b)) 
			return false;
		
		if(! isRegistrador(c) && ! isEndRam(c) && ! isInteger(c))
			return false;
		
		if(! isRegistrador(d) && ! isEndRam(d) && ! isInteger(d))
			return false;

		return true;
	}
	
	public static boolean isEndRam(String s) {

		if(Helper.validarEndereco(s))
			return true;
		else 
			return false;
		

	}
	
	public static boolean isRegistrador(String s) {
		if(isInteger(s)) {
		if(		Constantes.REGISTRADOR_A_INT == Integer.valueOf(s) ||
				Constantes.REGISTRADOR_B_INT == Integer.valueOf(s) ||
				Constantes.REGISTRADOR_C_INT == Integer.valueOf(s) ||
				Constantes.REGISTRADOR_D_INT == Integer.valueOf(s) 	) {
			return true;
		}
		}
		
		if(s.equals("A") || s.equals("B") || s.equals("C") || s.equals("D") || s.equals("PI")) {
			return true;
		}
		return false;
	}

	public static boolean isInteger(String s) {
		try { 

			Integer.parseInt(s);

		} catch(NumberFormatException e) { 

			return false; 

		} catch(NullPointerException e) {

			return false;

		}

		return true;
	}
	
	public static boolean validarEmu(CPU cpu, Ram ram, EntradaESaida es, AddressBus ab, ControlBus cb, DataBus db) {
		
		if(Math.pow(2, db.getLargura()) < ram.getCelulas().length) {
			System.out.println("Barramento pequeno demais para a quantidade de ram");
			return false;
		}
										//Memoria reservada para instrucoes
		if(ram.getCelulas().length <= ((cpu.getTam()/8)*4)*2) {
			System.out.println("Mémoria ram informada pequena demais para o tamanho da palavra");
			return false;
		}
		
		
		//TODO validar emulador
		
		return true;
	}

}

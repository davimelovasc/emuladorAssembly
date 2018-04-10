package utils;

import java.util.Date;

import principal.Main;

public class Logger {
	
	public static void printError(String className, String error) {
		System.err.println("[ classe: " + className + "; data: "+ new Date() + "] -> "+ error);
		System.exit(0);
	}
	
	public static void printFeedBack() {
		byte[] ram = Main.ram.getCelulas();
		System.out.println("\n");
		System.out.println("\t\t\t\t\t\tEMULADOR ASSEMBLY\n\n");
		System.out.println("\nMEMÓRIA RAM: \n");
		for (int i = 0; i < ram.length; i++) {
			System.out.println(i + ": "+ ram[i]);
		}
		System.out.println("\nREGISTRADORES: \n");
		switch (Main.cpu.getTam()) {
		case 16:
			short[] s = Main.cpu.getRegistradores16();
			System.out.println("REGISTRADOR A: " + s[0]);
			System.out.println("REGISTRADOR B: " + s[1]);
			System.out.println("REGISTRADOR C: " + s[2]);
			System.out.println("REGISTRADOR D: " + s[3]);
			System.out.println("REGISTRADOR PI: " + s[4]);
			
			break;
		case 32:
			int[] i = Main.cpu.getRegistradores32();
			System.out.println("REGISTRADOR A: " + i[0]);
			System.out.println("REGISTRADOR B: " + i[1]);
			System.out.println("REGISTRADOR C: " + i[2]);
			System.out.println("REGISTRADOR D: " + i[3]);
			System.out.println("REGISTRADOR PI: " + i[4]);
			
			break;
		case 64:
			long[] l = Main.cpu.getRegistradores64();
			System.out.println("REGISTRADOR A: " + l[0]);
			System.out.println("REGISTRADOR B: " + l[1]);
			System.out.println("REGISTRADOR C: " + l[2]);
			System.out.println("REGISTRADOR D: " + l[3]);
			System.out.println("REGISTRADOR PI: " + l[4]);

		default:
			break;
		}
		
	}

}

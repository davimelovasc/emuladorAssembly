package utils;

import java.util.Date;

public class Logger {
	
	public static void printError(String className, String error) {
		System.err.println("[ classe: " + className + "; data: "+ new Date() + "] -> "+ error);
		
	}

}

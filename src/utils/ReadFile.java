package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import entradaDeDados.Parser;

public class ReadFile {
	
	private final String nomeArquivo;
	ArrayList<String> linhas;
	
	 public ReadFile(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	   }
		    
		public ArrayList<String> readFile() {
		     
		    final String fileName = nomeArquivo;
		    String line = null;
		     linhas = new ArrayList<>();
		   /* ArrayList<String> instrucoes = new ArrayList<>();*/
		  
		   try {
	        FileReader fileReader = new FileReader("src/entradaDeDados/"+fileName);
	        BufferedReader bufferedReader = new BufferedReader(fileReader);
	        
	        
	        while((line = bufferedReader.readLine()) != null) {
	            line = line.trim();
	            
	           
	            
	            linhas.add(line);
	           }
	        
	            
	        bufferedReader.close(); 
	        
	        return linhas;
	        

	        
		   }catch(IOException ex) {
			   System.out.println(ex);
			   Logger.printError(ReadFile.class.getName(), "Arquivo nao encontrado!");  
			   
	        }
		  
		   
		   return null;
			
		}
		
		public ArrayList<String> readLine(int linha) {
			return Parser.parse(linhas.get(linha));
		}

		public String getNomeArquivo() {
			return nomeArquivo;
		}
		
		
		

}

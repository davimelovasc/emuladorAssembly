package entradaesaida;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.Logger;
import utils.Validate;

public class Parser {
	
public static ArrayList<String> parse(String entrada) {
	
	ArrayList<String> tokens = new ArrayList<>();

    
	ArrayList<String> regexs = new ArrayList<>();
	ArrayList<Pattern> patterns = new ArrayList<>();


	String regex0 = "(MOV)\\s+(\\w+),\\s+(\\w+)";
	String regex1 = "(ADD)\\s+(\\w+),\\s+(\\w+)";
	String regex2 = "(INC)\\s+(\\w+)";
	String regex3 = "(IMUL)\\s+(\\w+),\\s+(\\w+),\\s+(\\w+)";

	regexs.add(regex0);
	regexs.add(regex1);
	regexs.add(regex2);
	regexs.add(regex3);


	for(String s : regexs) {
		patterns.add(Pattern.compile(s));
	}

	//Pattern p = Pattern.compile(regex0);
	
	for(Pattern p : patterns ) {

		Matcher m = p.matcher(entrada);

		if(m.find()){
			

			switch(m.group(1).toUpperCase()) {

			case "MOV":
				
				if(Validate.validateMov(m.group(1), m.group(2), m.group(3))){
					
					
					tokens.add(m.group(1));
					tokens.add(m.group(2));
					tokens.add(m.group(3));
					
					if(m.group(3).contains("x") || m.group(3).contains("X")) {
						tokens.add("1"); //identificador para saber que o 2 operando é ram ao inves de numero
					}

				
					
					return tokens;
				}
				
				
				Logger.printError(Parser.class.getName(), "Erro ao realizar o Parser de alguma instrucao");
				
			case "ADD":
				if(Validate.validateAdd(m.group(1), m.group(2), m.group(3))) {
			
					tokens.add(m.group(1));
					tokens.add(m.group(2));
					tokens.add(m.group(3));
					
					return tokens;
				}
				
				Logger.printError(Parser.class.getName(), "Erro ao realizar o Parser de alguma instrucao");
				
			case "INC":

				if(Validate.validateInc(m.group(1), m.group(2))){
					
					tokens.add(m.group(1));
					tokens.add(m.group(2));
					
				    return tokens;
				}
					
				Logger.printError(Parser.class.getName(), "Erro ao realizar o Parser de alguma instrucao");

			
			case "IMUL":
				
				if(Validate.validateImul(m.group(1), m.group(2), m.group(3), m.group(4))) {
					
					tokens.add(m.group(1));
					tokens.add(m.group(2));
					tokens.add(m.group(3));
					tokens.add(m.group(4));
					
					return tokens;
				}
			default:
				
				Logger.printError(Parser.class.getName(), "Erro ao realizar o Parser de alguma instrucao");
			}

		} else {
			
		}
		
	}
	Logger.printError(Parser.class.getName(), "Falha na validação sintaxica");
	return null;
}
}



package principal;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.Logger;
import utils.ReadFile;
import utils.Validate;

public class Parser {
	
public static ArrayList<String> parse(String entrada) {
	
	ArrayList<String> tokens = new ArrayList<>();

    
	ArrayList<String> regexs = new ArrayList<>();
	final ArrayList<Pattern> patterns = new ArrayList<>();


	String regex0 = "(mov)\\s+(\\w+),\\s+(\\w+)\\Z";
	String regex1 = "(add)\\s+(\\w+),\\s+(\\w+)\\Z";
	String regex2 = "(inc)\\s+(\\w+)\\Z";
	String regex3 = "(imul)\\s+(\\w+),\\s+(\\w+),\\s+(\\w+)\\Z";

	regexs.add(regex0);
	regexs.add(regex1);
	regexs.add(regex2);
	regexs.add(regex3);


	for(String s : regexs) {
		patterns.add(Pattern.compile(s));
	}

	
	for(Pattern p : patterns ) {

		Matcher m = p.matcher(entrada);

		if(m.find()){

			switch(m.group(1)) {

			case "mov":
				if(Validate.validateMov(m.group(1), m.group(2), m.group(3))){
			        //VALIDAR LOGICAS DA ARQUITETURA E CHAMAR O ENCODER PARA TRANSFORMAR AS INFORMA��ES EM BYTES[] PARA IR AO BUFFER E/S
					
					tokens.add(m.group(1));
					tokens.add(m.group(2));
					tokens.add(m.group(3));
					
					return tokens;
				}
				
				
				return null;
				
			case "add":
				if(Validate.validateAdd(m.group(1), m.group(2), m.group(3))) {
			
					tokens.add(m.group(1));
					tokens.add(m.group(2));
					tokens.add(m.group(3));
					
					return tokens;
				}
				
				return null;
				
			case "inc":

				if(Validate.validateInc(m.group(1), m.group(2))){
					
					tokens.add(m.group(1));
					tokens.add(m.group(2));
					
				    return tokens;
				}
					
				return null;

			
			case "imul":

				if(Validate.validateImul(m.group(1), m.group(2), m.group(3), m.group(4))) {
					
					tokens.add(m.group(1));
					tokens.add(m.group(2));
					tokens.add(m.group(3));
					tokens.add(m.group(4));
					
					return tokens;
				}
				
				return null;
			}

		} 
		
	}
	return null;
}
}



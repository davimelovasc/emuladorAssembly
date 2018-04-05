package principal;

import java.util.ArrayList;

public class Dado {

	private String controle;
	private byte[] dados;
	private String endereco;
	
	public Dado() {
	}
	
	public Dado(String controle, byte[] dados, String endereco) {
		super();
		this.controle = controle;
		this.dados = dados;
		this.endereco = endereco;
	}
	
	public String getControle() {
		return controle;
	}
	public void setControle(String controle) {
		this.controle = controle;
	}
	public byte[] getDados() {
		return dados;
	}
	public void setDados(byte[] daados) {
		this.dados = daados;
	}
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	
	
}

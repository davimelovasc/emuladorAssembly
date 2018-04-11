package utils;

public class Dado {

	private String controle;
	private byte[] dados;
	private String endereco;
	private boolean offset;
	
	public Dado() {
	}
	
	public Dado(String controle, byte[] dados, String endereco) {
		super();
		this.controle = controle;
		this.dados = dados;
		this.endereco = endereco;
		this.offset = false;
	}
	
	public Dado(String controle, byte[] dados, String endereco, boolean offset) {
		super();
		this.controle = controle;
		this.dados = dados;
		this.endereco = endereco;
		this.setOffset(offset);
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

	public boolean isOffset() {
		return offset;
	}

	public void setOffset(boolean offset) {
		this.offset = offset;
	}
	
	
}

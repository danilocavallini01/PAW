package it.unibo.paw;

public class GTInfo {
	private String nome;
	private String marca;

	public GTInfo(String nome, String marca) {
		super();
		this.nome = nome;
		this.marca = marca;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	@Override
	public String toString() {
		return "GTInfo [nome=" + nome + ", marca=" + marca + "]";
	}

}

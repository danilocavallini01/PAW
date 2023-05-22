package it.unibo.paw;

public class Abbigliamento {
	private String fotografia;
	private String descrizioneString;
	private Integer prezzo;

	public Abbigliamento(String fotografia, String descrizioneString, Integer prezzo) {
		super();
		this.fotografia = fotografia;
		this.descrizioneString = descrizioneString;
		this.prezzo = prezzo;
	}

	@Override
	public String toString() {
		return "Abbigliamento [fotografia=" + fotografia + ", descrizioneString=" + descrizioneString
				+ ", prezzo=" + prezzo + "]";
	}

}

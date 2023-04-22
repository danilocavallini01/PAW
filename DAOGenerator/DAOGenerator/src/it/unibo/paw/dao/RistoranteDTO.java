package it.unibo.paw.dao;

public class RistoranteDTO implements Serializable {


	private static final  long serialVersionUID = 1L;

	//-------------------------------------

	private int id;
	private boolean loaded;
	private String name;

	//-------------------------------------


	public RistoranteDTO() {
		this.loaded = false;
	}
}

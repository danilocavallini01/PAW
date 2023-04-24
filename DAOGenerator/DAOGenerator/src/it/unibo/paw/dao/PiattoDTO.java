package it.unibo.paw.dao;

import java.io.Serializable;

public class PiattoDTO implements Serializable {


	private static final  long serialVersionUID = 1L;

	//-------------------------------------

	private int id;
	private boolean loaded;
	private String name;
	private String ingredienti;

	//-------------------------------------


	public PiattoDTO() {
		this.loaded = false;
	}
}

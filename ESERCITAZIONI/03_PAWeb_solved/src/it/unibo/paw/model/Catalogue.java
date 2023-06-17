package it.unibo.paw.model;

import java.util.ArrayList;
import java.util.List;

public class Catalogue {
	
	private List<Item> items;
	
	public Catalogue() {
		items = new ArrayList<Item>();
	}
	
	public void setItems(List<Item> items) {
		this.items = items;
	}

	public List<Item> getItems() {
		return this.items;
	}
	
	public Item get(int i) {
		return this.items.get(i);
	}
	
	public void empty() {
		this.items = new ArrayList<Item>();
	}
	
	
}

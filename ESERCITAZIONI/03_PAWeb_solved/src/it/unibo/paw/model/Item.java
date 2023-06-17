package it.unibo.paw.model;

public class Item {
	private int codItem;
	private String description;
	private double price;
	private int quantityGiacenza;
	
	public Item() {}
	
	public Item(int codItem, String description, double price, int quantityGiacenza) {
		this.codItem = codItem;
		this.description = description;
		this.price = price;
		this.quantityGiacenza = quantityGiacenza;
	}

	public int getCodItem() {
		return codItem;
	}

	public void setCodItem(int codItem) {
		this.codItem = codItem;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantityGiacenza() {
		return quantityGiacenza;
	}

	public void setQuantityGiacenza(int quantityGiacenza) {
		this.quantityGiacenza = quantityGiacenza;
	}
}

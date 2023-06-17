package it.unibo.paw.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Cart {
	private int codCart;
	private String email;
	
	private Map<Item, Integer> cartItems;
	
	public Cart() {
		cartItems = new HashMap<Item, Integer>();
	}

	public Cart(int codCart, String email) {
		this();
		this.codCart = codCart;
		this.email = email;
	}

	public int getCodCart() {
		return codCart;
	}

	public void setCodCart(int codCart) {
		this.codCart = codCart;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Map<Item, Integer> getCartItems() {
		return cartItems;
	}

	public void setCartItems(Map<Item, Integer> cartItems) {
		this.cartItems = cartItems;
	}
	
	public void put(Item item, int quantityOrdered) {
		this.cartItems.put(item, quantityOrdered);
	}
	
	public Set<Item> getItems() {
		return this.cartItems.keySet();
	}
	
	public int getQuantityOrdered(Item item) {
		return this.cartItems.get(item);
	}
	
	public void empty() {
		this.cartItems = new HashMap<Item, Integer>();
	}

	
	//Utilities
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + codCart;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cart other = (Cart) obj;
		if (codCart != other.codCart)
			return false;
		return true;
	}
	
	
	
}

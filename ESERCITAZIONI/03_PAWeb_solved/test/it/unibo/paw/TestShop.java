package it.unibo.paw;

import java.util.List;
import java.util.Map;

import it.unibo.paw.db.*;
import it.unibo.paw.model.*;

public class TestShop {

	private static ShopRepository sr;
	private static Catalogue catalogue;

	private static void initCatalogue() throws PersistenceException {
		Item item = new Item(1, "Decsrizione1", 19.90, 10);
		sr.persistItem(item);
		item = new Item(2, "Decsrizione2", 29.90, 20);
		sr.persistItem(item);
		item = new Item(3, "Decsrizione3", 39.90, 30);
		sr.persistItem(item);
		item = new Item(4, "Decsrizione4", 9.90, 40);
		sr.persistItem(item);
		item = new Item(5, "Decsrizione5", 5.90, 50);
		sr.persistItem(item);

		catalogue = new Catalogue();
		catalogue.setItems(sr.retrieveAllItems());
		System.out.println("CATALOGUE:");
		for (Item i : catalogue.getItems()) {
			System.out.println("- codItem=" + i.getCodItem() + ", description=" + i.getDescription() + ", price=" + i.getPrice() + ", quantity="
					+ i.getQuantityGiacenza());
		}
	}

	public static void main(String[] args) throws PersistenceException {

		sr = new ShopRepository(DataSource.DB2);
		sr.dropAndCreateTables();

		initCatalogue();

		Cart cart = new Cart();
		cart.setCodCart(1);
		cart.setEmail("mario.rossi@unibo.it");
		cart.put(catalogue.get(0), 1);
		cart.put(catalogue.get(1), 2);
		cart.put(catalogue.get(2), 3);
		cart.put(catalogue.get(3), 4);
		sr.persistOrder(cart);

		cart = new Cart();
		cart.setCodCart(2);
		cart.setEmail("giuseppe.verdi@unibo.it");
		cart.put(catalogue.get(4), 5);
		sr.persistOrder(cart);

		cart = new Cart();
		cart.setCodCart(3);
		cart.setEmail("mario.rossi@unibo.it");
		sr.persistOrder(cart);

		System.out.println();
		System.out.println("TestShopRepository - retrieveAllOrders");
		Map<String, List<Cart>> ordersByEmails = sr.retrieveAllOrders();
		String[] emails = ordersByEmails.keySet().toArray(new String[0]);
		for (String email : emails) {
			List<Cart> orders = ordersByEmails.get(email);
			System.out.println("=========== "+ email + " ===========");
			for (Cart c : orders) {
				System.out.println("CART [code: " + c.getCodCart() + "]:");
				if (c.getCartItems().isEmpty()) {
					System.out.println("---- EMPTY ----");
				} else {
					for (Map.Entry<Item, Integer> cartItem : c.getCartItems().entrySet()) {
						Item i = cartItem.getKey();
						System.out.println("- " + i.getDescription() + "; " + i.getPrice() + " (" + cartItem.getValue() + ")");
					}
				}
			}
		}

		System.out.println();
		System.out.println("TestShopRepository - retrieveOrdersByEmail --> giuseppe.verdi@unibo.it");
		List<Cart> orders = sr.retrieveOrdersByEmail("giuseppe.verdi@unibo.it");
		for (Cart c : orders) {
			System.out.println("CART [code: " + c.getCodCart() + ", email: " + c.getEmail() + "]:");
			if (c.getCartItems().isEmpty()) {
				System.out.println("==== EMPTY CART====");
			} else {
				for (Map.Entry<Item, Integer> cartItem : c.getCartItems().entrySet()) {
					Item i = cartItem.getKey();
					System.out.println("- " + i.getDescription() + "; " + i.getPrice() + " (" + cartItem.getValue() + ")");
				}
			}
		}

	}
}

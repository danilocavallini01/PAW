package it.unibo.paw.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.unibo.paw.model.Cart;
import it.unibo.paw.model.Item;

public class ShopRepository {
	private DataSource dataSource;

	//=== COSTANTI PER NON SBAGLIARSI ============================

	//------ TABLE ITEM -------------------------------------------------------------------

	private static final String TABLE_ITEM = "item";

	private static final String CODITEM = "codItem";
	private static final String DESCRIPTION = "description";
	private static final String PRICE = "price";
	private static final String QTY_GIACENZA = "quantity_giacenza";

	//------ TABLE CART-------------------------------------------------------------------

	private static final String TABLE_CART = "cart";

	private static final String CODCART = "codCart";
	private static final String EMAIL = "email";

	//------ TABLE CART_ITEM --------------------------------------------------------------

	private static final String TABLE_CARTITEM = "cart_item";

	private static final String CODCART_CI = "codCart";
	private static final String CODITEM_CI = "codItem";
	private static final String QTY_ORDERED = "quantity_ordered";

	// == STATEMENT SQL ====================================================================
	//------ ITEM -------------------------------------------------------------------

	private static final String CREATE_TABLE_ITEM = "CREATE TABLE " + TABLE_ITEM + " ("
			+ CODITEM + " int NOT NULL PRIMARY KEY,"
			+ DESCRIPTION + " varchar(50),"
			+ PRICE + "	double,"
			+ QTY_GIACENZA + " int"
			+ ")";

	private static final String DROP_TABLE_ITEM = "DROP TABLE " + TABLE_ITEM;

	private static final String INSERT_ITEM = "INSERT INTO " + TABLE_ITEM + " ("
			+ CODITEM + ", "
			+ DESCRIPTION + ", "
			+ PRICE + ", "
			+ QTY_GIACENZA
			+ ") VALUES (?,?,?,?)";

	private static final String DELETE_ITEM = "DELETE FROM " + TABLE_ITEM + "WHERE " + CODITEM + " = ?";

	private static final String RETRIEVE_ITEM_BY_KEY = "SELECT * FROM " + TABLE_ITEM + "WHERE " + CODITEM + " = ?";

	private static final String RETRIEVE_ALL_ITEMS = "SELECT * FROM " + TABLE_ITEM;

	//------ CART -------------------------------------------------------------------

	private static final String CREATE_TABLE_CART = "CREATE TABLE " + TABLE_CART + " ("
			+ CODCART + "	int NOT NULL PRIMARY KEY,"
			+ EMAIL + "	varchar(25)"
			+ ")";

	private static final String DROP_TABLE_CART = "DROP TABLE " + TABLE_CART;

	private static final String INSERT_CART = "INSERT INTO " + TABLE_CART + " ("
			+ CODCART + ", "
			+ EMAIL
			+ ") VALUES (?,?)";

	private static final String RETRIEVE_CARTS_BY_EMAIL = "SELECT "
			+ "C." + CODCART + ", C." + EMAIL + ", CI." + QTY_ORDERED + ", I." + CODITEM + ", I." + DESCRIPTION + ", I." + PRICE + ", I." + QTY_GIACENZA
			+ " FROM " + TABLE_CART + " C "
			+ " LEFT JOIN " + TABLE_CARTITEM + " CI ON C." + CODCART + " = CI." + CODCART_CI
			+ " LEFT JOIN " + TABLE_ITEM + " I ON CI." + CODITEM_CI + " = I." + CODITEM
			+ " WHERE C." + EMAIL + " = ?";

	private static final String RETRIEVE_ALL_CARTS = "SELECT"
			+ " C." + CODCART + ", C." + EMAIL + ", CI." + QTY_ORDERED + ", I." + CODITEM + ", I." + DESCRIPTION + ", I." + PRICE + ", I." + QTY_GIACENZA
			+ " FROM " + TABLE_CART + " C "
			+ " LEFT JOIN " + TABLE_CARTITEM + " CI ON C." + CODCART + " = CI." + CODCART_CI
			+ " LEFT JOIN " + TABLE_ITEM + " I ON CI." + CODITEM_CI + " = I." + CODITEM;

	//------ CART_ITEM -------------------------------------------------------------------

	private static final String CREATE_TABLE_CARTITEM = "CREATE TABLE " + TABLE_CARTITEM + " ("
			+ CODITEM_CI + " int NOT NULL,"
			+ CODCART_CI + " int NOT NULL,"
			+ QTY_ORDERED + " int,"
			+ "PRIMARY KEY("+CODITEM_CI+", "+CODCART_CI+")"
			+ ")";

	private static final String DROP_TABLE_CARTITEM = "DROP TABLE " + TABLE_CARTITEM;

	private static final String INSERT_CARTITEM = "INSERT INTO " + TABLE_CARTITEM + " ("
			+ CODITEM_CI + ", "
			+ CODCART_CI + ", "
			+ QTY_ORDERED
			+ ") VALUES (?,?,?)";

	// =====================================================================================

	public ShopRepository(int dbType) {
		dataSource = new DataSource(dbType);
	}

	public void dropAndCreateTables() throws PersistenceException {
		Connection conn = this.dataSource.getConnection();
		Statement stmt = null;

		try {
			stmt = conn.createStatement();
			try {
				stmt.executeUpdate(DROP_TABLE_ITEM);
			} catch (SQLException e) {
				// the table does not exist
			}
			try {
				stmt.executeUpdate(DROP_TABLE_CART);
			} catch (SQLException e) {
				// the table does not exist
			}
			try {
				stmt.executeUpdate(DROP_TABLE_CARTITEM);
			} catch (SQLException e) {
				// the table does not exist
			}
			stmt.executeUpdate(CREATE_TABLE_ITEM);
			stmt.executeUpdate(CREATE_TABLE_CART);
			stmt.executeUpdate(CREATE_TABLE_CARTITEM);
		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				throw new PersistenceException(e.getMessage());
			}
		}
	}

	public void persistOrder(Cart cart) throws PersistenceException {
		Connection conn = this.dataSource.getConnection();
		PreparedStatement pstmtCart = null;
		PreparedStatement pstmtCartItem = null;
		Item item = null;

		try {
			pstmtCart = conn.prepareStatement(INSERT_CART);
			pstmtCartItem = conn.prepareStatement(INSERT_CARTITEM);

			for (Map.Entry<Item, Integer> cartItem : cart.getCartItems().entrySet()) {
				item = cartItem.getKey();
				pstmtCartItem.setInt(1, item.getCodItem());
				pstmtCartItem.setInt(2, cart.getCodCart());
				pstmtCartItem.setInt(3, cartItem.getValue());
				pstmtCartItem.executeUpdate();
			}
			pstmtCart.setInt(1, cart.getCodCart());
			pstmtCart.setString(2, cart.getEmail());
			pstmtCart.executeUpdate();

		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		} finally {
			try {
				if (pstmtCart != null) {
					pstmtCart.close();
				}
				if (pstmtCartItem != null) {
					pstmtCartItem.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
	}

	public List<Cart> retrieveOrdersByEmail(String email) throws PersistenceException {
		Connection conn = this.dataSource.getConnection();
		PreparedStatement pstmt = null;
		Map<Integer, Cart> orders = null;
		Cart cart = null;
		Item item = null;

		try {
			pstmt = conn.prepareStatement(RETRIEVE_CARTS_BY_EMAIL);
			pstmt.setString(1, email);
			ResultSet result = pstmt.executeQuery();

			if (result.next()) {
				orders = new HashMap<Integer, Cart>();
				do {
					int codCart = result.getInt(CODCART);
					if ((cart = orders.get(codCart)) == null) {
						cart = new Cart(codCart, result.getString(EMAIL));
						orders.put(codCart, cart);
					}
					int codItem = result.getInt(CODITEM);
					if (!result.wasNull()) {
						//the cart has at least one cart item
						item = new Item(codItem, result.getString(DESCRIPTION), result.getDouble(PRICE), result.getInt(QTY_GIACENZA));
						cart.put(item, result.getInt(QTY_ORDERED));
					}
				} while (result.next());
			}
		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		}

		return new ArrayList<Cart>(orders.values());
	}

	public Map<String, List<Cart>> retrieveAllOrders() throws PersistenceException {
		Connection conn = this.dataSource.getConnection();
		PreparedStatement pstmt = null;
		Map<String, List<Cart>> ordersByEmail = null;
		List<Cart> orders = null;
		Cart cart = null;
		Item item = null;

		try {
			pstmt = conn.prepareStatement(RETRIEVE_ALL_CARTS);
			ResultSet result = pstmt.executeQuery();

			if (result.next()) {
				ordersByEmail = new HashMap<String, List<Cart>>();
				do {
					int codCart = result.getInt(CODCART);
					String email = result.getString(EMAIL);
					if ((orders = ordersByEmail.get(email)) == null) {
						orders = new ArrayList<Cart>();
						ordersByEmail.put(email, orders);
					}

					int i;
					cart = new Cart(codCart, email);
					if ((i = orders.indexOf(cart)) == -1) {
						orders.add(cart);
					} else {
						cart = orders.get(i);
					}

					int codItem = result.getInt(CODITEM);
					if (!result.wasNull()) {
						//the cart has at least one cart item
						item = new Item(codItem, result.getString(DESCRIPTION), result.getDouble(PRICE), result.getInt(QTY_GIACENZA));
						cart.put(item, result.getInt(QTY_ORDERED));
					}
				} while (result.next());
			}
		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		}

		return ordersByEmail;
	}

	public void persistItem(Item item) throws PersistenceException {
		Connection conn = this.dataSource.getConnection();
		PreparedStatement pstmtUpdate = null;
		PreparedStatement pstmtInsert = null;

		try {
			pstmtInsert = conn.prepareStatement(INSERT_ITEM);
			pstmtInsert.setInt(1, item.getCodItem());
			pstmtInsert.setString(2, item.getDescription());
			pstmtInsert.setDouble(3, item.getPrice());
			pstmtInsert.setInt(4, item.getQuantityGiacenza());
			pstmtInsert.executeUpdate();
		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		} finally {
			try {
				if (pstmtUpdate != null) {
					pstmtUpdate.close();
				}
				if (pstmtInsert != null) {
					pstmtInsert.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				throw new PersistenceException(e.getMessage());
			}
		}
	}

	public void deleteItem(int codItem) throws PersistenceException {
		Connection conn = this.dataSource.getConnection();
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement(DELETE_ITEM);
			pstmt.setInt(1, codItem);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				throw new PersistenceException(e.getMessage());
			}
		}
	}

	public Item retrieveItemByKey(int codItem) throws PersistenceException {
		Connection conn = this.dataSource.getConnection();
		PreparedStatement pstmt = null;
		Item item = null;

		try {
			pstmt = conn.prepareStatement(RETRIEVE_ITEM_BY_KEY);
			pstmt.setInt(1, codItem);
			ResultSet result = pstmt.executeQuery();

			if (result.next()) {
				item = new Item(result.getInt(CODITEM), result.getString(DESCRIPTION), result.getDouble(PRICE), result.getInt(QTY_GIACENZA));
			}
		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				throw new PersistenceException(e.getMessage());
			}
		}

		return item;
	}

	public List<Item> retrieveAllItems() throws PersistenceException {
		Connection conn = this.dataSource.getConnection();
		PreparedStatement pstmt = null;
		List<Item> items = null;
		Item item = null;

		try {
			pstmt = conn.prepareStatement(RETRIEVE_ALL_ITEMS);
			ResultSet result = pstmt.executeQuery();

			if (result.next()) {
				items = new LinkedList<Item>();
				do {
					item = new Item(result.getInt(CODITEM), result.getString(DESCRIPTION), result.getDouble(PRICE), result.getInt(QTY_GIACENZA));
					items.add(item);
				} while (result.next());
			}
		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				throw new PersistenceException(e.getMessage());
			}
		}

		return items;
	}

}

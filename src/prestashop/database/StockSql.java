package prestashop.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StockSql extends BaseSql {
	
	private final static String sqlUpdateStock = "UPDATE ps_stock_available SET quantity="+TAG_QUANTITY+" WHERE id_product="+TAG_PRODUCT+" AND id_shop="+TAG_SHOP+";";
	private final static String sqlExistsStock = "SELECT count(*) as result FROM ps_stock_available WHERE id_product="+TAG_PRODUCT+" AND id_shop="+TAG_SHOP+";";
	private final static String sqlAddStock = "INSERT INTO ps_stock_available(id_product,id_product_attribute,id_shop,id_shop_group,quantity,depends_on_stock,out_of_stock)" +
										"VALUES("+TAG_PRODUCT+",0,"+TAG_SHOP+",0,"+TAG_QUANTITY+",0,0);";
	
	public static void reset(DbConnector db)
	{
		db.execute("TRUNCATE TABLE ps_stock_available;");
	}
	
	public static void addUpdate(DbConnector db, String idProduct, String idShop, String quantity)
	{
		if (exists(db, idProduct,idShop))
			update(db, idProduct, idShop, quantity);
		else
			add(db, idProduct, idShop, quantity);
	}
	
	public static void add(DbConnector db, String idProduct, String idShop, String quantity)
	{
		int result = db.execute(
				sqlAddStock.replace(TAG_PRODUCT,idProduct).replace(TAG_SHOP,idShop).replace(TAG_QUANTITY,quantity)
			);
		if (result != 1) {
			System.out.println("Blad podczas dodawania ilosci produktu "+idProduct);
		}
	}
	
	public static boolean exists(DbConnector db, String idProduct, String idShop)
	{
		boolean exists = true;
		try {
			ResultSet rs = db.executeSelect(
					sqlExistsStock.replace(TAG_PRODUCT, idProduct).replace(TAG_SHOP, idShop)
					);
			if (rs.next()) {
				if (rs.getInt("result") == 0)
					exists = false;
			} else {
				System.out.println("Blad sprawdzania stanu produktu "+idProduct);
				System.exit(1);
			}
		} catch(SQLException e) {
			System.out.println("Blad zapytania sql !! "+e);
			System.exit(1);
		}
		return exists;
	}
	
	public static void update(DbConnector db, String idProduct, String idShop, String quantity)
	{
		int result = db.execute(
						sqlUpdateStock.replace(TAG_PRODUCT,idProduct).replace(TAG_SHOP,idShop).replace(TAG_QUANTITY,quantity)
					);
		if (result != 1) {
			System.out.println("Blad podczas aktualizacji ilosci produktu "+idProduct);
		}
	}
}

package prestashop.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import prestashop.utils.DATA_TYPE;

public class DbHelper {
	private final static String LANGUAGE = "pl";
	private final static String DOMAIN = "brakeparts.pl";

	
	private final static String TAG_NAME = "<NAME>";
	

	private final static String sqlGetLanguage = "SELECT id_lang FROM ps_lang WHERE iso_code = '"+LANGUAGE+"'";
	private final static String sqlGetShop = "SELECT id_shop FROM ps_shop WHERE name = '"+DOMAIN+"'";
	private final static String sqlGetRootCategory = "SELECT id_category FROM ps_category_lang WHERE name = '"+TAG_NAME+"'";
	
	public static String getLanguage(DbConnector db) {
		int id_lang = -1;
		try {
			ResultSet rs = db.executeSelect(sqlGetLanguage);
			if (rs.next()) {
				id_lang = rs.getInt("id_lang");
				System.out.println("ID dla jezyka polskiego: " + id_lang);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Blad sql" + e.getMessage());
			System.exit(1);
		}
		return Integer.toString(id_lang);
	}

	public static String getShop(DbConnector db) {
		int id_shop = -1;
		try {
			ResultSet rs = db.executeSelect(sqlGetShop);
			if (rs.next()) {
				id_shop = rs.getInt("id_shop");
				System.out.println("ID dla sklepu prestashop: " + id_shop);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Blad sql" + e.getMessage());
			System.exit(1);
		}
		return Integer.toString(id_shop);
	}
	
	public static String getRoot(DbConnector db, DATA_TYPE type, String id_lang)
	{
		int id_root = -1;
		try {
			ResultSet rs = db.executeSelect(
					sqlGetRootCategory.replace(TAG_NAME,  type.getCategory())
					);
			if (rs.next()) {
				id_root = rs.getInt("id_category");
				if (!type.getDbName().equals("NONE")) {
					String category = CategorySql.getCurrentCategory(db, Integer.toString(id_root), type.getDbName(), id_lang);
					if (category.equals("-1")) {
						System.out.println("Nie ma takiej kategorii: "+type.getDbName());
						System.exit(1);
					}
					return category;
				}
			} else {
				System.out.println("Nie ma takiej kategorii: "+type.getCategory());
				System.exit(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Blad sql" + e.getMessage());
			System.exit(1);
		}
		System.out.println("ID dla kategorii nadrzednej: "+id_root);
		return Integer.toString(id_root);
	}
}

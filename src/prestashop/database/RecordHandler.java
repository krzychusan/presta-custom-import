package prestashop.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import prestashop.parser.DATA_TYPE;
import prestashop.parser.Record;



public class RecordHandler {

	private DbConnector db = null;
	private String rootType = null;

	private int id_shop = 0;
	private int id_lang = 0;
	private int id_root = 0;
	
	private final String TAG_NAME = "<NAME>";
	private final String TAG_CATEGORY = "<CATEGORY>";
	private final String TAG_GROUP = "<GROUP>";
	private final String TAG_REWRITE = "<REWRITE>";
	private final String TAG_SHOP = "<SHOP>";
	private final String TAG_LANG = "<LANG>";
	private final String TAG_PARENT = "<PARENT>";

	private final String sqlGetLang = "SELECT id_lang FROM ps_lang WHERE iso_code = 'pl'";
	private final String sqlGetShop = "SELECT id_shop FROM ps_shop WHERE name = 'brakeparts.pl'";
	private final String sqlGetRootCategory = "SELECT id_category FROM ps_category_lang WHERE name = '"+TAG_NAME+"'";
	
	private final String sqlGetCategory = 
				"SELECT lang.id_category FROM ps_category cat"+
				"JOIN ps_category_lang lang"+
				"ON cat.id_category = lang.id_category"+
				"WHERE cat.id_parent = "+TAG_PARENT+" AND lang.id_lang = "+TAG_LANG+" AND lang.name = "+TAG_NAME+";";
	
	
	private final String sqlInsertCategoryGroup = "INSERT INTO ps_category_group(id_category,id_group) VALUES("+TAG_CATEGORY+","+TAG_GROUP+");";
	private final String sqlInsertCategoryLang = "INSERT INTO ps_category_lang(id_category,id_shop,id_lang,name,link_rewrite) VALUES("+TAG_CATEGORY+","+TAG_SHOP+","+TAG_LANG+","+TAG_NAME+","+TAG_REWRITE+");";
	private final String sqlInsertCategoryShop = "INSERT INTO ps_category_shop(id_category,id_shop)VALUES("+TAG_CATEGORY+","+TAG_SHOP+");";
	
	
	private HashMap<String, String> categoryCache = new HashMap<String,String>();
	
	public void close()
	{
		
	}
	
	public void init(DbConnector database, DATA_TYPE dataType) {
		db = database;

		getLanguage();
		getShop();
		getRoot(dataType.getDbName());
	}

	public void handleRecord(Record record) {
		String idCategory = null;
		//idCategory = addCategoryIfNew(record.brand, id_root);
		//idCategory = addCategoryIfNew(record.model, idCategory);
		//idCategory = addCategoryIfNew(record.???, idCategory);
		
		addProducts();
		
	}
	
	public String addCategoryIfNew(String name, String parentId)
	{
		String cacheKey = generateKey(name, parentId);
		if (categoryCache.containsKey(cacheKey))
			return categoryCache.get(cacheKey);
		
		int category = getCurrentCategory(parentId, name);
		if (category == -1) {
			category = addCategory(name, parentId);
		}
		

		

		String idCategory = Integer.toString(category);
		
		categoryCache.put(cacheKey, idCategory);
		return idCategory;
	}
	
	public String addCategory(String name, String parentId)
	{
		String idCategory = addCategoryRecord(parentId);
		
		addCategoryGroup(idCategory, GROUPS.GROUP1);
		addCategoryGroup(idCategory, GROUPS.GROUP2);
		addCategoryGroup(idCategory, GROUPS.GROUP3);
		
		addCategoryLang(idCategory, name, name);
		addCategoryShop(idCategory);
		
		return idCategory;
	}
	
	
	
	//----------
	
	private String generateKey(String name, String parentName)
	{
		return name+"_|_"+parentName;
	}
	
	private String addCategoryRecord()
	{
		return "1";
	}
	
	private int getCurrentCategory(...)
	{
		ResultSet rs = db.executeSelect(
				sqlGetCategory.replace(aa, bb).replace(aa, bb)
				);
		if (rs.next()) {
			return rs.getInt("id_category");
		} else {
			return -1;
		}
	}

	private void addCategoryShop(String idCategory)
	{
		int result = db.execute(
				sqlInsertCategoryShop.replace(TAG_CATEGORY,idCategory).replace(TAG_SHOP,Integer.toString(id_shop))
				);
		if (result != 1) {
			System.out.println("Blad podczas laczenia kategorii "+idCategory+" ze sklepem "+id_shop);
		}
	}
	
	private void addCategoryLang(String idCategory, String name, String linkRewrite)
	{
		int result = db.execute(
				sqlInsertCategoryLang.replace(TAG_CATEGORY,idCategory).replace(TAG_SHOP,Integer.toString(id_shop)).replace(TAG_LANG, Integer.toString(id_lang)).replace(TAG_NAME,name).replace(TAG_REWRITE, linkRewrite)
				);
		if (result != 1) {
			System.out.println("Blad podczas laczenia kategorii "+idCategory+" z nazwa "+name);
		}
	}
	
	private void addCategoryGroup(String idCategory, GROUPS group)
	{
		int result = db.execute(
				sqlInsertCategoryGroup.replace(TAG_CATEGORY,idCategory).replace(TAG_GROUP,group.toString())
				);
		if (result != 1) {
			System.out.println("Blad podczas dodawania kategorii "+idCategory+" do grupy "+group.toString());
		}
	}

	private void getLanguage() {
		try {
			ResultSet rs = db.executeSelect(sqlGetLang);
			if (rs.next()) {
				id_lang = rs.getInt("id_lang");
				System.out.println("ID dla jezyka polskiego: " + id_lang);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Blad sql" + e.getMessage());
		}
	}

	private void getShop() {
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
		}
	}
}

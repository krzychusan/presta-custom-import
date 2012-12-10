package prestashop.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import prestashop.parser.DATA_TYPE;
import prestashop.parser.Record;



public class RecordHandler {

	private DbConnector db = null;

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
	private final String TAG_DEPTH = "<DEPTH>";
	private final String TAG_LEFT = "<LEFT>";
	private final String TAG_RIGHT = "<RIGHT>";
	private final String TAG_DEC_RIGHT = "<DEC_RIGHT>";

	private final String sqlGetLang = "SELECT id_lang FROM ps_lang WHERE iso_code = 'pl'";
	private final String sqlGetShop = "SELECT id_shop FROM ps_shop WHERE name = 'brakeparts.pl'";
	private final String sqlGetRootCategory = "SELECT id_category FROM ps_category_lang WHERE name = '"+TAG_NAME+"'";
	private final String sqlGetCategoryOnly = "SELECT id_category, id_parent, level_depth, nleft, nright FROM ps_category WHERE id_category = "+ TAG_CATEGORY+";";
	private final String sqlGetLastInsertedCategory = "SELECT max(id_category) as id_category FROM ps_category;";
	
	private final String sqlGetCategory = 
				"SELECT lang.id_category FROM ps_category cat "+
				"JOIN ps_category_lang lang "+
				"ON cat.id_category = lang.id_category "+
				"WHERE cat.id_parent = "+TAG_PARENT+" AND lang.id_lang = "+TAG_LANG+" AND lang.name = '"+TAG_NAME+"';";
	
	private final String sqlUpdateCategoryRight = "UPDATE ps_category SET nright = nright+2 WHERE nright > "+TAG_DEC_RIGHT+";";
	private final String sqlUpdateCategoryLeft =  "UPDATE ps_category SET nleft = nleft+2 WHERE nleft > "+TAG_DEC_RIGHT+";";

	
	private final String sqlInsertCategoryGroup = "INSERT INTO ps_category_group(id_category,id_group) VALUES("+TAG_CATEGORY+","+TAG_GROUP+");";
	private final String sqlInsertCategoryLang = "INSERT INTO ps_category_lang(id_category,id_shop,id_lang,name,link_rewrite) VALUES("+TAG_CATEGORY+","+TAG_SHOP+","+TAG_LANG+",'"+TAG_NAME+"','"+TAG_REWRITE+"');";
	private final String sqlInsertCategoryShop = "INSERT INTO ps_category_shop(id_category,id_shop)VALUES("+TAG_CATEGORY+","+TAG_SHOP+");";
	private final String sqlInsertCategory     = "INSERT INTO ps_category(id_parent,id_shop_default,level_depth,nleft,nright,active,date_add,date_upd)VALUES("+
																		  TAG_PARENT+","+TAG_SHOP+","+TAG_DEPTH+","+TAG_LEFT+","+TAG_RIGHT+",1,NOW(),NOW());";
	
	
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
		idCategory = addCategoryIfNew(record.getBrand(), Integer.toString(id_root));
		idCategory = addCategoryIfNew(record.getModel(), idCategory);
		//idCategory = addCategoryIfNew(record.???, idCategory);
		
		//addProducts(idCategory, record.getProducts());
		
	}
	
	//public void 
	
	public String addCategoryIfNew(String name, String parentId)
	{
		String cacheKey = generateKey(name, parentId);
		if (categoryCache.containsKey(cacheKey))
			return categoryCache.get(cacheKey);
		
		int category = getCurrentCategory(parentId, name);
		System.out.println("Current category for "+name+" is "+category);
		String idCategory = Integer.toString(category);
		if (category == -1) {
			idCategory = addCategory(name, parentId);
		}
		
		categoryCache.put(cacheKey, idCategory);
		return idCategory;
	}
	
	public String addCategory(String name, String parentId)
	{
		String idCategory = addCategoryRecord(name, parentId);
		
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
	
	private String addCategoryRecord(String name, String parentId)
	{
		System.out.println("Adding category: "+name+" to "+parentId);
		String newCategoryId = "-1";
		int parentDepth=0, parentLeft=0, parentRight=0;
		ResultSet rs;
		try {
			rs = db.executeSelect(
					sqlGetCategoryOnly.replace(TAG_CATEGORY, parentId)
					);
			if (rs.next()) {
				parentDepth = rs.getInt("level_depth");
				parentLeft = rs.getInt("nleft");
				parentRight = rs.getInt("nright");
			} else {
				System.out.println("Nie ma takiej kategorii!: "+parentId);
				System.exit(1);
			}
		
			int result;
			result = db.execute(
					sqlUpdateCategoryRight.replace(TAG_DEC_RIGHT, Integer.toString(parentRight-1))
					);
	
			result = db.execute(
					sqlUpdateCategoryLeft.replace(TAG_DEC_RIGHT, Integer.toString(parentRight-1))
					);
		
			result = db.execute(
				sqlInsertCategory.replace(TAG_PARENT, parentId)
								.replace(TAG_SHOP, Integer.toString(id_shop))
								.replace(TAG_DEPTH, Integer.toString(parentDepth+1))
								.replace(TAG_LEFT, Integer.toString(parentRight-1))
								.replace(TAG_RIGHT, Integer.toString(parentRight))
								);
			if (result != 1) {
				System.out.println("Blad podczas dodawania kategorii "+name);
			}
			
			rs = db.executeSelect(
					sqlGetLastInsertedCategory
					);
			if (rs.next()) {
				newCategoryId = Integer.toString(rs.getInt("id_category"));
			} else {
				System.out.println("Blad przy dodawaniu kategorii :/");
				System.exit(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Blad sql" + e.getMessage());
			System.exit(1);
		}
		
		return newCategoryId;
	}
	
	private void getRoot(String category)
	{
		try {
			ResultSet rs = db.executeSelect(
					sqlGetRootCategory.replace(TAG_NAME, category)
					);
			if (rs.next()) {
				id_root = rs.getInt("id_category");
			} else {
				System.out.println("Nie ma takiej kategorii!: "+category);
				System.exit(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Blad sql" + e.getMessage());
			System.exit(1);
		}
		System.out.println("ID dla kategorii nadrzednej: "+id_root);
	}
	
	private int getCurrentCategory(String parentId, String name)
	{
		try {
			ResultSet rs = db.executeSelect(
					sqlGetCategory.replace(TAG_PARENT, parentId).replace(TAG_LANG, Integer.toString(id_lang)).replace(TAG_NAME, name)
					);
			if (rs.next()) {
				return rs.getInt("id_category");
			} else {
				return -1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Blad sql" + e.getMessage());
			System.exit(1);
		}
		return 0;
	}

	private void addCategoryShop(String idCategory)
	{
		System.out.println("Adding category shop: "+idCategory);
		int result = db.execute(
				sqlInsertCategoryShop.replace(TAG_CATEGORY,idCategory).replace(TAG_SHOP,Integer.toString(id_shop))
				);
		if (result != 1) {
			System.out.println("Blad podczas laczenia kategorii "+idCategory+" ze sklepem "+id_shop);
		}
	}
	
	private void addCategoryLang(String idCategory, String name, String linkRewrite)
	{
		System.out.println("Adding category lang: "+idCategory+" for "+name);
		int result = db.execute(
				sqlInsertCategoryLang.replace(TAG_CATEGORY,idCategory).replace(TAG_SHOP,Integer.toString(id_shop)).replace(TAG_LANG, Integer.toString(id_lang)).replace(TAG_NAME,name).replace(TAG_REWRITE, linkRewrite)
				);
		if (result != 1) {
			System.out.println("Blad podczas laczenia kategorii "+idCategory+" z nazwa "+name);
		}
	}
	
	private void addCategoryGroup(String idCategory, GROUPS group)
	{
		System.out.println("Adding category group: "+idCategory+ " GR:"+group.toString());
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

package prestashop.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategorySql extends BaseSql {

	private final static String sqlGetCategoryOnly = "SELECT id_category, id_parent, level_depth, nleft, nright FROM ps_category WHERE id_category = "+ TAG_CATEGORY+";";
	private final static String sqlGetLastInsertedCategory = "SELECT max(id_category) as id_category FROM ps_category;";
	private final static String sqlUpdateCategoryRight = "UPDATE ps_category SET nright = nright+2 WHERE nright > "+TAG_DEC_RIGHT+";";
	private final static String sqlUpdateCategoryLeft =  "UPDATE ps_category SET nleft = nleft+2 WHERE nleft > "+TAG_DEC_RIGHT+";";

	private final static String sqlInsertCategoryGroup = "INSERT INTO ps_category_group(id_category,id_group) VALUES("+TAG_CATEGORY+","+TAG_GROUP+");";
	private final static String sqlInsertCategoryLang = "INSERT INTO ps_category_lang(id_category,id_shop,id_lang,name,link_rewrite) VALUES("+TAG_CATEGORY+","+TAG_SHOP+","+TAG_LANG+",'"+TAG_NAME+"','"+TAG_REWRITE+"');";
	private final static String sqlInsertCategoryShop = "INSERT INTO ps_category_shop(id_category,id_shop)VALUES("+TAG_CATEGORY+","+TAG_SHOP+");";
	
	private final static String sqlInsertCategory = 
			"INSERT INTO ps_category(id_parent,id_shop_default,level_depth,nleft,nright,active,date_add,date_upd,is_root_category)VALUES("+
			TAG_PARENT+","+TAG_SHOP+","+TAG_DEPTH+","+TAG_LEFT+","+TAG_RIGHT+",1,NOW(),NOW(),"+TAG_ISROOT+");";

	private final static String sqlGetCategory = 
			"SELECT lang.id_category FROM ps_category cat "+
			"JOIN ps_category_lang lang "+
			"ON cat.id_category = lang.id_category "+
			"WHERE cat.id_parent = "+TAG_PARENT+" AND lang.id_lang = "+TAG_LANG+" AND lang.name = '"+TAG_NAME+"';";
	
	
	
	public static void reset(DbConnector db)
	{
		db.execute("TRUNCATE TABLE ps_category;");
		db.execute("TRUNCATE TABLE ps_category_group;");
		db.execute("TRUNCATE TABLE ps_category_lang;");
		db.execute("TRUNCATE TABLE ps_category_shop;");
		db.execute("TRUNCATE TABLE ps_category_product;");
	}
	
	public static String getCurrentCategory(DbConnector db, String parentId, String name, String lang)
	{
		String category = "-1";
		try {
			ResultSet rs = db.executeSelect(
					sqlGetCategory.replace(TAG_PARENT, parentId).replace(TAG_LANG, lang).replace(TAG_NAME, name)
					);
			if (rs.next()) {
				category = Integer.toString(rs.getInt("id_category"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Blad sql" + e.getMessage());
			System.exit(1);
		}
		return category;
	}
	
	public static String getLastInsertedId(DbConnector db)
	{
		String id = "-1";
		ResultSet rs = db.executeSelect(
				sqlGetLastInsertedCategory
				);

		try {
			if (rs.next()) {
				id = Integer.toString(rs.getInt("id_category"));
			} else {
				System.out.println("Blad przy dodawaniu kategorii :/");
				System.exit(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Blad sql" + e.getMessage());
			System.exit(1);
		}
		return id;
	}
	
	public static String addCategoryRecord(DbConnector db, String name, String parentId, String shop)
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
								.replace(TAG_SHOP, shop)
								.replace(TAG_DEPTH, Integer.toString(parentDepth+1))
								.replace(TAG_LEFT, Integer.toString(parentRight-1))
								.replace(TAG_RIGHT, Integer.toString(parentRight))
								.replace(TAG_ISROOT, "0")
								);
			if (result != 1) {
				System.out.println("Blad podczas dodawania kategorii "+name);
			}
			
			newCategoryId = getLastInsertedId(db);

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Blad sql" + e.getMessage());
			System.exit(1);
		}
		
		return newCategoryId;
	}
	
	public static void addCategoryGroup(DbConnector db, String idCategory, GROUPS group)
	{
		System.out.println("Adding category group: "+idCategory+ " GR:"+group.toString());
		int result = db.execute(
				sqlInsertCategoryGroup.replace(TAG_CATEGORY,idCategory).replace(TAG_GROUP,group.toString())
				);
		if (result != 1) {
			System.out.println("Blad podczas dodawania kategorii "+idCategory+" do grupy "+group.toString());
		}
	}
	
	public static void addCategoryShop(DbConnector db, String idCategory, String shop)
	{
		System.out.println("Adding category shop: "+idCategory);
		int result = db.execute(
				sqlInsertCategoryShop.replace(TAG_CATEGORY,idCategory).replace(TAG_SHOP,shop)
				);
		if (result != 1) {
			System.out.println("Blad podczas laczenia kategorii "+idCategory+" ze sklepem "+shop);
		}
	}
	
	public static void addCategoryLang(DbConnector db, String idCategory, String name, String linkRewrite, String shop, String lang)
	{
		System.out.println("Adding category lang: "+idCategory+" for "+name);
		int result = db.execute(
				sqlInsertCategoryLang.replace(TAG_CATEGORY,idCategory).replace(TAG_SHOP,shop).replace(TAG_LANG, lang).replace(TAG_NAME,name).replace(TAG_REWRITE, prepareLink(linkRewrite))
				);
		if (result != 1) {
			System.out.println("Blad podczas laczenia kategorii "+idCategory+" z nazwa "+name);
		}
	}

	public static String addRootCategory(DbConnector db, String idShop, String idLang) 
	{
		db.execute(				
				sqlInsertCategory.replace(TAG_PARENT, "0")
				.replace(TAG_SHOP, idShop)
				.replace(TAG_DEPTH, "0")
				.replace(TAG_LEFT, "1")
				.replace(TAG_RIGHT, "2")
				.replace(TAG_ISROOT, "1")
				);
		
		return getLastInsertedId(db);
	}

}

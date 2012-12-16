package prestashop.handler;

import java.util.HashMap;
import java.util.Map.Entry;

import prestashop.database.CategorySql;
import prestashop.database.DbConnector;
import prestashop.database.DbHelper;
import prestashop.database.GROUPS;
import prestashop.database.ProductSql;
import prestashop.interfaces.Record;
import prestashop.interfaces.RecordHandler;
import prestashop.utils.DATA_TYPE;
import prestashop.utils.ProductRecord;

public class ProductHandler implements RecordHandler {

	private DbConnector db = null;
	private DATA_TYPE type = null;
	
	private String id_shop = "0";
	private String id_lang = "0";
	private String id_root = "0";
	
	private HashMap<String, String> categoryCache = new HashMap<String,String>();
	
	public ProductHandler(DATA_TYPE type)
	{
		this.type = type;
	}

	@Override
	public void open(DbConnector database) {
		db = database;
		id_shop = DbHelper.getShop(db);
		id_lang = DbHelper.getLanguage(db);
		id_root = DbHelper.getRoot(db, type);
	}

	@Override
	public void close() {
	}

	@Override
	public void handleRecord(Record rc) {
		if (rc instanceof ProductRecord) {
			ProductRecord record = (ProductRecord)rc; 
			String idCategory = null;
			idCategory = addCategoryIfNew(record.getBrand(), id_root);
			idCategory = addCategoryIfNew(record.getModel(), idCategory);
			idCategory = addCategoryIfNew(record.getYear(), idCategory);
			if (record.getComment() != null) {
				idCategory = addCategoryIfNew(record.getComment(), idCategory);
			}
			handleProducts(idCategory, record.getProducts());
		} else {
			System.out.println("Bledny rekord"+rc);
		}
		
	}
	
	public String addCategoryIfNew(String name, String parentId)
	{
		String cacheKey = generateKey(name, parentId);
		if (categoryCache.containsKey(cacheKey))
			return categoryCache.get(cacheKey);
		
		String category = CategorySql.getCurrentCategory(db, parentId, name, id_lang);
		System.out.println("Current category for "+name+" is "+category);
		if (category == "-1") {
			category = addCategory(name, parentId);
		}
		
		categoryCache.put(cacheKey, category);
		return category;
	}
	
	public String addCategory(String name, String parentId)
	{
		String idCategory = CategorySql.addCategoryRecord(db, name, parentId, id_shop);
		
		CategorySql.addCategoryGroup(db, idCategory, GROUPS.GROUP1);
		CategorySql.addCategoryGroup(db, idCategory, GROUPS.GROUP2);
		CategorySql.addCategoryGroup(db, idCategory, GROUPS.GROUP3);
		
		CategorySql.addCategoryLang(db, idCategory, name, name, id_shop, id_lang);
		CategorySql.addCategoryShop(db, idCategory, id_shop);
		
		return idCategory;
	}
	
	public void handleProducts(String idCategory, HashMap<String, String[]> products)
	{
		for (Entry<String, String[]> product : products.entrySet())
		{
			String owner = product.getKey();
			for (String title: product.getValue())
			{
				handleProduct(idCategory, owner, title);
			}
		}
	}
	
	public void handleProduct(String idCategory, String owner, String name)
	{
		String idProduct = addProductIfNew(name, idCategory);
		if (!ProductSql.isProductInCategory(db, idProduct, idCategory))
			ProductSql.addProductToCategory(db, idProduct, idCategory);
	}
	
	public String addProductIfNew(String name, String idCategory)
	{
		String idProduct = ProductSql.getProductId(db, name);
		if (idProduct == "-1") {
			idProduct = ProductSql.addProduct(db, idCategory, name, id_shop);
			System.out.println("Dodano produkt "+name+" o ID "+idProduct);
			ProductSql.addProductLang(db, idProduct, name, id_shop, id_lang);
			ProductSql.addProductShop(db, idProduct, idCategory, id_shop);
			//addProductSupplier();
			//addProductTag();
		}
		return idProduct;
	}
	
	private String generateKey(String name, String parentName)
	{
		return name+"_|_"+parentName;
	}
}

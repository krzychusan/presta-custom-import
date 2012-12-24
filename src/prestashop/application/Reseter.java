package prestashop.application;

import prestashop.database.CategorySql;
import prestashop.database.DbConnector;
import prestashop.database.DbHelper;
import prestashop.database.GROUPS;
import prestashop.database.ImageSql;
import prestashop.database.ProductSql;
import prestashop.database.StockSql;
import prestashop.handler.ProductHandler;
import prestashop.utils.DATA_TYPE;

public class Reseter {
	private CommandLineParser cliParser = new CommandLineParser();
	private DbConnector connector = new DbConnector();
	
	public static void main(String [] args)
	{
		Reseter reseter = new Reseter();
		reseter.run(args);
		System.out.println("Reset skonczony");
	}
	
	public void run(String [] args)
	{
		cliParser.setReseterParams();
		cliParser.parse(args);
		connector.open(cliParser.getDb(), cliParser.getLogin(), cliParser.getPassword());
		ProductSql.reset(connector);
		CategorySql.reset(connector);
		ImageSql.reset(connector);
		StockSql.reset(connector);
		
		String idRoot = addRoot();
		
		ProductHandler handler = new ProductHandler(DATA_TYPE.ROOT);
		handler.open(connector);
		String brakepads = handler.addCategory("klocki hamulcowe", idRoot);
		handler.addCategory(DATA_TYPE.BRAKEPADS_FRONT.getDbName(), brakepads);
		handler.addCategory(DATA_TYPE.BRAKEPADS_BACK.getDbName(), brakepads);
		String shields = handler.addCategory("tarcze hamulcowe", idRoot);
		handler.addCategory(DATA_TYPE.SHIELDS_FRONT.getDbName(), shields);
		handler.addCategory(DATA_TYPE.SHIELDS_BACK.getDbName(), shields);
	}
	
	public String addRoot()
	{
		String idLang = DbHelper.getLanguage(connector);
		String idShop = DbHelper.getShop(connector);
		String idRoot = CategorySql.addRootCategory(connector, idShop, idLang);

		CategorySql.addCategoryGroup(connector, idRoot, GROUPS.GROUP1);
		CategorySql.addCategoryGroup(connector, idRoot, GROUPS.GROUP2);
		CategorySql.addCategoryGroup(connector, idRoot, GROUPS.GROUP3);
		CategorySql.addCategoryLang(connector, idRoot, "Root", "root", idShop, idLang);
		CategorySql.addCategoryShop(connector, idRoot, idShop);
		
		return idRoot;
	}
}

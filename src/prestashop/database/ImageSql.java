package prestashop.database;

public class ImageSql extends BaseSql {

	
	public static void reset(DbConnector db)
	{
		db.execute("TRUNCATE TABLE ps_image;");
		db.execute("TRUNCATE TABLE ps_image_lang;");
		db.execute("TRUNCATE TABLE ps_image_shop;");
	}
}

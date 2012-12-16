package prestashop.handler;

import prestashop.database.DbConnector;
import prestashop.database.DbHelper;
import prestashop.interfaces.Record;
import prestashop.interfaces.RecordHandler;
import prestashop.utils.DATA_TYPE;
import prestashop.utils.PricelistRecord;

public class PricelistHandler implements RecordHandler {

	private DbConnector db = null;
	private DATA_TYPE type = null;
	
	private String id_shop = "0";
	private String id_lang = "0";
	private String id_root = "0";

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
		if (rc instanceof PricelistRecord) {
			//TODO implement
		} else {
			System.out.println("Otrzymany rekord nie jest z cennika");
			System.exit(1);
		}
	}

}

package prestashop.handler;

import prestashop.database.DbConnector;
import prestashop.database.DbHelper;
import prestashop.database.ProductSql;
import prestashop.database.StockSql;
import prestashop.interfaces.Record;
import prestashop.interfaces.RecordHandler;
import prestashop.utils.DATA_TYPE;
import prestashop.utils.PricelistRecord;

public class PricelistHandler implements RecordHandler {

	private DbConnector db = null;
	private DATA_TYPE type = null;
	
	private String id_shop = "0";

	@Override
	public void open(DbConnector database) {
		db = database;
		id_shop = DbHelper.getShop(db);
	}

	@Override
	public void close() {
	}

	@Override
	public void handleRecord(Record rc) {
		if (rc instanceof PricelistRecord) {
			PricelistRecord record = (PricelistRecord) rc;
			String idProduct = ProductSql.getProductId(db, record.getName());
			if (idProduct != "-1") {
				System.out.println("Aktualizacja produktu: "+record);
				ProductSql.update(db,idProduct, record.getPrice());
				StockSql.addUpdate(db, idProduct, id_shop, record.getQuantity());
			} else {
				System.out.println("Produkt nie istnieje "+rc);
			}
		} else { 
			System.out.println("Otrzymany rekord nie jest z cennika");
			System.exit(1);
		}
	}
}

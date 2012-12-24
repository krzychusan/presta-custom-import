package prestashop.handler;

import prestashop.database.DbConnector;
import prestashop.database.DbHelper;
import prestashop.database.ProductSql;
import prestashop.interfaces.Record;
import prestashop.interfaces.RecordHandler;
import prestashop.utils.DescriptionRecord;

public class DescriptionHandler implements RecordHandler {

	private DbConnector db = null;
	
	@Override
	public void open(DbConnector database) {
		db = database;
	}

	@Override
	public void close() {
		
	}

	@Override
	public void handleRecord(Record rc) {
		if (rc instanceof DescriptionRecord) {
			DescriptionRecord record = (DescriptionRecord)rc;
			System.out.println("Aktualizacja rekordu: "+record);
			String id = ProductSql.getProductId(db, record.getName());
			if (id != "-1") {
			ProductSql.updateDescription(db, id, record.getDescription(), record.getShortDescription());
			ProductSql.updateProductParams(db, id, record.getWidth(), record.getHeight(), record.getDepth(), record.getWeight());
			} else {
				System.out.println("Produkt nie istnieje "+record);
			}
		}
	}

}

package prestashop.parser;

import org.apache.poi.ss.usermodel.Row;

import prestashop.interfaces.Record;
import prestashop.interfaces.RecordCreator;
import prestashop.utils.PricelistRecord;

public class PriceListRecordCreator extends RecordCreator {
	
	private int PRODUCT_NAME_COLUMN_INDEX = 0;
	private int PRIZE_COLUMN_INDEX = 1;
	private int QANTITY_COLUMN_INDEX =  2;
	
	public Record create(Row row) {
		PricelistRecord record = new PricelistRecord();
		int howmany = row.getPhysicalNumberOfCells();
		if (howmany >= QANTITY_COLUMN_INDEX) {
			record.setName(getStringValue(row.getCell(PRODUCT_NAME_COLUMN_INDEX)));
			record.setPrice(getStringValue(row.getCell(PRIZE_COLUMN_INDEX)));
			record.setQuantity(getStringValue(row.getCell(QANTITY_COLUMN_INDEX)));		
		}
		return record;
	}

}

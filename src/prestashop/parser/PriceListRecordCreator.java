package prestashop.parser;

import org.apache.poi.ss.usermodel.Row;

public class PriceListRecordCreator implements RecordCreator {
	
	private int PRODUCT_NAME_COLUMN_INDEX = 0;
	private int PRIZE_COLUMN_INDEX = 1;
	private int QANTITY_COLUMN_INDEX =  2;
	
	public PriceListRecord createRecord(Row row) {
		PriceListRecord record = new PriceListRecord();
		int howmany = row.getPhysicalNumberOfCells();
		if (howmany >= QANTITY_COLUMN_INDEX) {
			record.setName(ExcelParser.getStringValue(row.getCell(PRODUCT_NAME_COLUMN_INDEX)));
			record.setPrice(ExcelParser.getStringValue(row.getCell(PRIZE_COLUMN_INDEX)));
			record.setQuantity(ExcelParser.getStringValue(row.getCell(QANTITY_COLUMN_INDEX)));		
		}
		return record;
	}

}

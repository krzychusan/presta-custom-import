package prestashop.parser;

import java.util.HashMap;

import org.apache.poi.ss.usermodel.Row;

public class BrakePadRecordCreator implements RecordCreator {
	
    private final static int PRODUCTS_LIST_START_COLUMN_INDEX = 4;
    private final static int BRAND_COLUMN_INDEX = 0;
    private final static int MODEL_COLUMN_INDEX = 1;
    private final static int YEAR_COLUMN_INDEX = 2;
    private final static int COMMENT_COLUMN_INDEX = 3;
    private final static String SPLITTING_STRING = ";";

	public Record createRecord(Row row) {
		Record record = new Record();
		int howmany = row.getPhysicalNumberOfCells();
		if (howmany >= PRODUCTS_LIST_START_COLUMN_INDEX) {
			record.setBrand(ExcelParser.getStringValue(row.getCell(BRAND_COLUMN_INDEX)));
			record.setModel(ExcelParser.getStringValue(row.getCell(MODEL_COLUMN_INDEX)));
			record.setYear(ExcelParser.getStringValue(row.getCell(YEAR_COLUMN_INDEX)));
			record.setComment(ExcelParser.getStringValue(row.getCell(COMMENT_COLUMN_INDEX)));
			record.setProducts(parseProductsList(row, PRODUCTS_LIST_START_COLUMN_INDEX, howmany));			
		}
		return record;
	}
	
	private HashMap<String, String[]> parseProductsList(Row row, int startIndex, int endIndex){
		HashMap<String, String[]> products = new HashMap<String, String[]>(); 
		for (int i = startIndex; i < endIndex; ++i) {
			String values[] = null;
			String ids = ExcelParser.getStringValue(row.getCell(i));
			if (ids != null) {
				values = ids.split(SPLITTING_STRING);
			    products.put(ExcelParser.getStringValue(row.getSheet().getRow(0).getCell(i)), values);
			}
		}
		return products;
	}

}

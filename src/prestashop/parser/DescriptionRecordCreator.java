package prestashop.parser;

import org.apache.poi.ss.usermodel.Row;

import prestashop.interfaces.Record;
import prestashop.interfaces.RecordCreator;
import prestashop.utils.DescriptionRecord;

public class DescriptionRecordCreator extends RecordCreator {
	private final int REQUIRED_COLUMNS = 6;
	private final int ID_COLUMN_INDEX = 0;
	private final int WIDTH_COLUMN_INDEX = 1;
	private final int HEIGHT_COLUMN_INDEX = 2;
	private final int DEPTH_COLUMN_INDEX = 3;
	private final int WEIGHT_COLUMN_INDEX = 4;
	private final int DESCRIPTION_COLUMN_INDEX = 5;
	private final int SHORT_DESCRIPTION_COLUMN_INDEX = 6;
	
	@Override
	public Record create(Row row) {
		DescriptionRecord record = new DescriptionRecord();
		int howmany = row.getPhysicalNumberOfCells();
		if (howmany >= REQUIRED_COLUMNS) {
			record.setName(getStringValue(row.getCell(ID_COLUMN_INDEX)));
			record.setWidth(getStringValue(row.getCell(WIDTH_COLUMN_INDEX)));
			record.setHeight(getStringValue(row.getCell(HEIGHT_COLUMN_INDEX)));
			record.setDepth(getStringValue(row.getCell(DEPTH_COLUMN_INDEX)));
			record.setWeight(getStringValue(row.getCell(WEIGHT_COLUMN_INDEX)));
			record.setDescription(getStringValue(row.getCell(DESCRIPTION_COLUMN_INDEX)));
			record.setShortDescription(getStringValue(row.getCell(SHORT_DESCRIPTION_COLUMN_INDEX)));
		}
		return record;
	}
}

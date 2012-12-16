package prestashop.interfaces;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import prestashop.interfaces.Record;

public abstract class RecordCreator {
	public abstract Record create(Row row);

	public String getStringValue(Cell cell){
		if (cell == null)
			return null;
		else if (cell.getCellType() == Cell.CELL_TYPE_STRING)
			return cell.getRichStringCellValue().toString();
		else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
			return Double.toString(cell.getNumericCellValue());
		return null;
		}
}

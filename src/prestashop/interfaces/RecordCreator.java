package prestashop.interfaces;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import prestashop.interfaces.Record;

public abstract class RecordCreator {
	public abstract Record create(Row row);
	
	public boolean verifyContent(Cell cell) {
		if (cell == null)
			return false;
		if (cell.getCellType() == Cell.CELL_TYPE_BLANK)
			return false;
		if (cell.getRichStringCellValue().toString().equals(""))
			return false;
		return true;
	}

	public String getStringValue(Cell cell){
		if (verifyContent(cell))
			return cell.getRichStringCellValue().toString();
		else
			return null;
	}
}

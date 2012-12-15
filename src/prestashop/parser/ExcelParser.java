package prestashop.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelParser implements InputParser {
	
	private InputStream inp;
	private Workbook wb;
	private Sheet sheet;
	private final int sheetNumber = 0;
    private ArrayList<Record> records = new ArrayList<Record>();
    private ArrayList<PriceListRecord> priceListRecords = new ArrayList<PriceListRecord>();
    private BrakePadRecordCreator rc = new BrakePadRecordCreator();
    private PriceListRecordCreator prc = new PriceListRecordCreator();
    private DATA_TYPE type;
    
       	
	@Override
	public boolean open(String filename, DATA_TYPE type) {
		this.type = type;
		try {		
			inp = new FileInputStream(filename);
			wb = new HSSFWorkbook(inp);
		} catch (FileNotFoundException e) {
			System.out.println(filename + " could not be opened.");
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			System.out.println("Unable to opeate on " + filename);
			e.printStackTrace();
			return false;
		}
		sheet = wb.getSheetAt(sheetNumber);	
		parseRecords();		
		return true;
	}
	
	private boolean parseRecords() {
		int numOfRows = sheet.getPhysicalNumberOfRows();
		System.out.println("Numer of rows is " + numOfRows);
		for (int i = 1; i < numOfRows; ++i) {
			  Row row = sheet.getRow(i);
			  switch(this.type) {
			  case BRAKEPADS_BACK:
			  case BRAKEPADS_FRONT:
				  records.add(rc.createRecord(row));
				  break;
			  case PRICELIST:
			      priceListRecords.add(prc.createRecord(row));
			  }
			  
		}
		System.out.println("Finished");
		printRecords();
		return true;
	}

	public Iterator<Record> getRecordIterator() {
		if (records == null)
			return null;
		return records.iterator();
	}
	
	public Iterator<PriceListRecord> getPriceListRecordIterator() {
		if (priceListRecords == null)
			return null;
		return priceListRecords.iterator();
	}

	@Override
	public void close() {	
		try {
		   if (inp != null)
		     inp.close();
	    } catch (IOException e) {
		  	System.out.println("Unable to close excel file.");
		    e.printStackTrace();
	    }	
	}
	
	public static boolean verifyContent(Cell cell) {
		if (cell == null)
			return false;
		if (cell.getCellType() == Cell.CELL_TYPE_BLANK)
			return false;
		if (cell.getRichStringCellValue().toString().equals(""))
			return false;
		return true;
	}
	
	public static String getStringValue(Cell cell){
		if (verifyContent(cell))
			return cell.getRichStringCellValue().toString();
		else
			return null;
	}
	
	
	private void printRecords(){
		for (Iterator<Record> it = this.getRecordIterator(); it.hasNext();){
			Record r = it.next();
			r.print();
		}			
	}
	

}

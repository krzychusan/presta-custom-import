package prestashop.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import prestashop.interfaces.InputParser;
import prestashop.interfaces.Record;
import prestashop.interfaces.RecordCreator;

public class ExcelParser implements InputParser {
	
	private InputStream inp;
	private Workbook wb;
	private Sheet sheet;
	private final int sheetNumber = 0;
    private ArrayList<Record> records = new ArrayList<Record>();
    private RecordCreator creator = null;
    
    public ExcelParser(RecordCreator rc)
    {
    	creator = rc;
    }
    
	@Override
	public void open(String filename) {
		try {		
			inp = new FileInputStream(filename);
			wb = new HSSFWorkbook(inp);
		} catch (FileNotFoundException e) {
			System.out.println(filename + " could not be opened.");
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			System.out.println("Unable to opeate on " + filename);
			e.printStackTrace();
			System.exit(1);
		}
		sheet = wb.getSheetAt(sheetNumber);	
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

	@Override
	public Iterator<Record> getRecordIterator() {
		if (records == null)
			return null;
		return records.iterator();
	}

	@Override
	public void parseRecords() {
		int numOfRows = sheet.getPhysicalNumberOfRows();
		System.out.println("Numer of rows is " + numOfRows);
		for (int i = 1; i < numOfRows; ++i) {
			  Row row = sheet.getRow(i);
			  records.add(creator.create(row));
		}
		System.out.println("Finished");
		printRecords();
	}
	
	private void printRecords(){
		for (Iterator<Record> it = this.getRecordIterator(); it.hasNext();){
			Record r = it.next();
			r.print();
		}			
	}

}

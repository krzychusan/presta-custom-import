package prestashop.interfaces;

import java.util.Iterator;

public interface InputParser {
	public void open(String path);
	public void close();
	
	public Iterator<Record> getRecordIterator();
	public void parseRecords();
}

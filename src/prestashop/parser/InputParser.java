package prestashop.parser;

import java.util.Iterator;

public interface InputParser {
	public boolean open(String filename, DATA_TYPE type);
	public Iterator<Record> getIterator();
	public void close();
}

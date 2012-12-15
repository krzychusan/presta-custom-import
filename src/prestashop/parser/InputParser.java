package prestashop.parser;

import java.util.Iterator;

public interface InputParser {
	public boolean open(String filename, DATA_TYPE type);
	public void close();
}

package prestashop.database;

import prestashop.parser.Record;

public interface SqlGenerator {
	public void addProduct(DbConnector conn, Record record);
	public void open();
	public void close();
}

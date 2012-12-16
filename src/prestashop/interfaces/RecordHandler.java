package prestashop.interfaces;

import prestashop.database.DbConnector;

public interface RecordHandler {
	public void open(DbConnector db);
	public void close();
	
	public void handleRecord(Record record);
}

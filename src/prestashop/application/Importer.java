package prestashop.application;

import java.util.Iterator;

import prestashop.parser.ShieldRecordCreator;
import prestashop.database.DbConnector;
import prestashop.handler.ProductHandler;
import prestashop.handler.DescriptionHandler;
import prestashop.handler.PricelistHandler;
import prestashop.interfaces.InputParser;
import prestashop.interfaces.Record;
import prestashop.interfaces.RecordCreator;
import prestashop.interfaces.RecordHandler;
import prestashop.parser.BrakePadRecordCreator;
import prestashop.parser.DescriptionRecordCreator;
import prestashop.parser.ExcelParser;
import prestashop.parser.PriceListRecordCreator;
import prestashop.utils.DATA_TYPE;


public class Importer {

	private CommandLineParser cliParser = new CommandLineParser();
	private DbConnector connector = new DbConnector();
	
	private InputParser input = null;
	private RecordHandler handler = null;
	
	public static void main(String [] args)
	{
		Importer app = new Importer();
		app.run(args);
	}
	
	public void run(String [] args)
	{
		parseArguments(args);
		createObjects();
		open();
		handleData();
		close();
	}
	
	private void createObjects()
	{
		if (cliParser.getType() == DATA_TYPE.PRICELIST)
			createPricelistObjects();
		else if (cliParser.getType() == DATA_TYPE.DESCRIPTIONS)
			createDescriptionObjects();
		else
			createProductObjects(cliParser.getType());
	}
	
	public void parseArguments(String [] args)
	{
		cliParser.setImporterParams();
		cliParser.parse(args);
	}
	
	private void open()
	{
		connector.open(cliParser.getDb(), cliParser.getLogin(), cliParser.getPassword());
		input.open(cliParser.getFilename());
		handler.open(connector);
	}
	
	private void close()
	{
		handler.close();
		input.close();
		connector.close();
	}
	private void handleData()
	{
		input.parseRecords();
		Iterator<Record> it = input.getRecordIterator();
		while (it.hasNext()) {
			handler.handleRecord(it.next());
		}
	}
	
	private void createDescriptionObjects()
	{
		input = new ExcelParser(new DescriptionRecordCreator());
		handler = new DescriptionHandler();
	}
	
	private void createPricelistObjects()
	{
		input = new ExcelParser(new PriceListRecordCreator());
		handler = new PricelistHandler();
	}
	
	private void createProductObjects(DATA_TYPE type)
	{
		RecordCreator rc = null;
		if (type == DATA_TYPE.BRAKEPADS_BACK || type == DATA_TYPE.BRAKEPADS_FRONT)
			rc = new BrakePadRecordCreator();
		else if (type == DATA_TYPE.SHIELDS_BACK || type == DATA_TYPE.SHIELDS_FRONT)
			rc = new ShieldRecordCreator();
		
		input = new ExcelParser(rc);
		handler = new ProductHandler(type);
	}
}

package prestashop.utils;

import java.util.Iterator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import prestashop.database.DbConnector;
import prestashop.database.RecordHandler;
import prestashop.parser.DATA_TYPE;
import prestashop.parser.ExcelParser;
import prestashop.parser.InputParser;
import prestashop.parser.Record;

public class CatalogImporter {
	// protected Connector connector = null;
	protected InputParser input = null;
	protected RecordHandler handler = null;
	protected DbConnector connector = null;

	private final String[] argFile = {"f", "file", "Plik z danymi"};
	private final String[] argType = {"t", "type", "Co zawiera plik z danymi"};
	private final String[] argLogin = {"l", "login", "Login do bazy danych"};
	private final String[] argPassword = {"p", "password", "Haslo do bazy danych"};
	private final String[] argDb = {"h", "host", "Adres bazy danych (format  ip_address:port/db_name)"};

	public static void main(String[] args) {
		CatalogImporter importer = new CatalogImporter();
		importer.run(args);
	}

	public CatalogImporter() {
	}
	
	private void AddOption(Options options, String[] args)
	{
		//opt, longOpt, hasArg, description
		options.addOption(args[0], args[1], true, args[2]);
		options.getOption(args[0]).setRequired(true);
	}
	
	private String GetArgument(CommandLine line, String[] arg) throws Exception
	{
		String value = line.getOptionValue(arg[0]);
		if (value == null)
		{
			throw new Exception("Brakuje argumentu "+arg[1]);
		}
		return value;
		
	}

	public void init(String[] args) {
		input = new ExcelParser();
		handler = new RecordHandler();
		connector = new DbConnector();

		Options options = new Options();
		AddOption(options, argFile);
		AddOption(options, argType);
		AddOption(options, argLogin);
		AddOption(options, argPassword);
		AddOption(options, argDb);

		CommandLineParser cliParser = new PosixParser();
		String filename = null, db = null, login = null, password = null;
		DATA_TYPE type = null;
		try {
			CommandLine line = cliParser.parse(options, args);
			filename = GetArgument(line, argFile);
			type = DATA_TYPE.GetValue(GetArgument(line, argType));
			db = GetArgument(line, argDb);
			login = GetArgument(line, argLogin);
			password = line.getOptionValue(argPassword[0]);
		} catch (ParseException e) {
			e.printStackTrace();
			System.out.println("Blad podczas parsowania argumentów");
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.exit(1);
		}

		input.open(filename, type);
		connector.open(db, login, password);
		handler.init(connector,  type);
	}

	public void close() {
		connector.close();
		handler.close();
		input.close();
	}

	public void run(String[] arguments) {
		init(arguments);
		handleData();
		close();
	}

	public void handleData() {
		Iterator<Record> it = input.getIterator();
		while (it.hasNext()) {
			handler.handleRecord(it.next());
		}
	}
}

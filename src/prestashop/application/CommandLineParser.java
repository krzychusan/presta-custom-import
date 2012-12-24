package prestashop.application;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

import prestashop.utils.DATA_TYPE;




class MissingArgument extends Exception {
	public MissingArgument(String message) {
		super(message);
	}
	private static final long serialVersionUID = 1L;
}

public class CommandLineParser {
	private Options options = new Options();

	private final String[] argFile = {"f", "file", "Plik z danymi"};
	private final String[] argType = {"t", "type", "Co zawiera plik z danymi"};
	private final String[] argLogin = {"l", "login", "Login do bazy danych"};
	private final String[] argPassword = {"p", "password", "Haslo do bazy danych"};
	private final String[] argDb = {"h", "host", "Adres bazy danych (format  ip_address:port/db_name)"};
	
	private String filename = null;
	private String db = null;
	private String login = null;
	private String password = null;
	DATA_TYPE type = null;
	
	public String getFilename()
	{
		return filename;
	}
	
	public String getDb()
	{
		return db;
	}
	
	public String getLogin()
	{
		return login;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public DATA_TYPE getType()
	{
		return type;
	}
	
	public void setImporterParams()
	{
		AddOption(options, argFile);
		AddOption(options, argType);
		AddOption(options, argLogin);
		AddOption(options, argPassword);
		AddOption(options, argDb);
	}
	
	public void setReseterParams()
	{
		AddOption(options, argLogin);
		AddOption(options, argPassword);
		AddOption(options, argDb);
	}
	
	public void parse(String [] args)
	{
	
		PosixParser cliParser = new PosixParser();
		try {
			CommandLine line = cliParser.parse(options, args);
			if (options.hasOption(argFile[0]))
				filename = GetArgument(line, argFile);
			if (options.hasOption(argType[0]))
				type = DATA_TYPE.GetValue(GetArgument(line, argType));
			db = GetArgument(line, argDb);
			login = GetArgument(line, argLogin);
			password = line.getOptionValue(argPassword[0]);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
	
	private String GetArgument(CommandLine line, String[] arg) throws Exception
	{
		String value = line.getOptionValue(arg[0]);
		if (value == null)
		{
			throw new MissingArgument("Brakuje argumentu "+arg[1]);
		}
		return value;
	}
	
	private void AddOption(Options options, String[] args)
	{
		//opt, longOpt, hasArg, description
		options.addOption(args[0], args[1], true, args[2]);
		options.getOption(args[0]).setRequired(true);
	}
}
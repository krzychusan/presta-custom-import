package prestashop.database;

import java.util.HashMap;
import java.util.Map.Entry;

public class BaseSql {
	protected final static String TAG_NAME = "<NAME>";
	protected final static String TAG_CATEGORY = "<CATEGORY>";
	protected final static String TAG_GROUP = "<GROUP>";
	protected final static String TAG_REWRITE = "<REWRITE>";
	protected final static String TAG_SHOP = "<SHOP>";
	protected final static String TAG_LANG = "<LANG>";
	protected final static String TAG_PARENT = "<PARENT>";
	protected final static String TAG_DEPTH = "<DEPTH>";
	protected final static String TAG_LEFT = "<LEFT>";
	protected final static String TAG_RIGHT = "<RIGHT>";
	protected final static String TAG_DEC_RIGHT = "<DEC_RIGHT>";
	protected final static String TAG_PRODUCT = "<PRODUCT>";
	

	public static String createInsertStatement(String table, HashMap<String,String> params)
	{
		String arguments = "";
		String values = "";
		for (Entry<String,String> row : params.entrySet())
		{
			arguments += ","+row.getKey();
			values += ","+row.getValue();
		}
		arguments = arguments.replaceFirst(",", "");
		values = values.replaceFirst(",","");
		
		String statement = "INSERT INTO " + table + "(" + arguments + ") VALUES ("+ values + ");";
		return statement;
	}
	
	public static String prepareLink(String name)
	{
		return name.replace(" ", "-").replace(".", "-");
	}
}

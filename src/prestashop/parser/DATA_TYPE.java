package prestashop.parser;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ParseException;

public enum DATA_TYPE {
	BRAKEPADS_FRONT("klocki hamulcowe przednie", "BF"),
	BRAKEPADS_BACK("klocki hamulcowe tylne", "BB"),
	SHIELDS_FRONT("tarcze hamulcowe przednie", "SF"),
	SHIELDS_BACK("tarcze hamulcowe tylne", "SB"),
	PRICELIST("cennik", "C");
	
	DATA_TYPE(String title, String id)
	{
		this.title = title;
		this.id = id;
	}
	
	public static DATA_TYPE GetValue(String value) throws ParseException
	{
		for (DATA_TYPE type: DATA_TYPE.values()) {
			if (type.id.equals(value)) {
				return type;
			}
		}
		
		System.out.println("Dostepne typy danych:");
		for (DATA_TYPE type: DATA_TYPE.values()) {
			System.out.println("- "+type.id);
		}
			
		throw new ParseException("Unknown type! "+value);
		
	}
	
	public String getDbName()
	{
		return this.title;
	}
	
	String title;
	String id;
}

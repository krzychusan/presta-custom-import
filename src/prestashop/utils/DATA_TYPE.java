package prestashop.utils;


public enum DATA_TYPE {
	BRAKEPADS_FRONT("klocki hamulcowe przednie", "BF"),
	BRAKEPADS_BACK("klocki hamulcowe tylne", "BB"),
	SHIELDS_FRONT("tarcze hamulcowe przednie", "SF"),
	SHIELDS_BACK("tarcze hamulcowe tylne", "SB"),
	PRICELIST("cennik", "C"),
	DESCRIPTIONS("opisy", "D"),
	ROOT("Root", "ROOT"),
	UNKNOWN("ZLY TYP", "!!!!!!!!!!");
	
	DATA_TYPE(String title, String id)
	{
		this.title = title;
		this.id = id;
	}
	
	public static DATA_TYPE GetValue(String value)
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
			
		System.out.println("Nieznany typ arkusza: "+value);
		System.exit(1);
		return DATA_TYPE.UNKNOWN;
	}
	
	public String getDbName()
	{
		return this.title;
	}
	
	private String title;
	private String id;
}

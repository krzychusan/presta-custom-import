package prestashop.utils;


public enum DATA_TYPE {
	BRAKEPADS_FRONT("Klocki hamulcowe", "Oś przednia", "BF"),
	BRAKEPADS_BACK("Klocki hamulcowe", "Oś tylna", "BB"),
	SHIELDS_FRONT("Tarcze hamulcowe", "Oś przednia", "SF"),
	SHIELDS_BACK("Tarcze hamulcowe", "Oś tylna", "SB"),
	PRICELIST("?", "cennik", "C"),
	DESCRIPTIONS("?", "opisy", "D"),
	ROOT("0", "Root", "ROOT"),
	UNKNOWN("ERROR", "ZLY TYP", "!!!!!!!!!!");
	
	DATA_TYPE(String category, String title, String id)
	{
		this.category = category;
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
	
	public String getCategory() {
		return category.toUpperCase();
	}
	
	public String getDbName()
	{
		return this.title;
	}
	
	private String title;
	private String id;
	private String category;
}

package prestashop.utils;


public enum DATA_TYPE {
	BRAKEPADS_FRONT(DATA_TYPE.KLOCKI_HAMULCOWE, "Oś przednia", "BF"),
	BRAKEPADS_BACK(DATA_TYPE.KLOCKI_HAMULCOWE, "Oś tylna", "BB"),
	SHIELDS_FRONT(DATA_TYPE.TARCZE_HAMULCOWE, "Oś przednia", "SF"),
	SHIELDS_BACK(DATA_TYPE.TARCZE_HAMULCOWE, "Oś tylna", "SB"),
	PRICELIST("?", "cennik", "C"),
	DESCRIPTIONS("?", "opisy", "D"),
	ROOT("Root", "NONE", "ROOT"),
	UNKNOWN("NONE", "ZLY TYP", "!!!!!!!!!!");
	
	public static final String TARCZE_HAMULCOWE = "Tarcze hamulcowe";
	public static final String KLOCKI_HAMULCOWE = "Klocki hamulcowe";
	public static final String SHOP = "BrakeParts";
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
		return category;
	}
	
	public String getDbName()
	{
		return this.title;
	}
	
	private String title;
	private String id;
	private String category;
}

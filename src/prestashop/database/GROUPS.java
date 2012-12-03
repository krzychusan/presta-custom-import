package prestashop.database;

public enum GROUPS {
	GROUP1(1),
	GROUP2(2),
	GROUP3(3);

	GROUPS(int value)
	{
		this.value = value;
	}
	
	public int value()
	{
		return value;
	}
	
	public String toString()
	{
		return Integer.toString(value);
	}
	
	private int value;
}

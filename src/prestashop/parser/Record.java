package prestashop.parser;

import java.util.HashMap;
import java.util.Iterator;

public class Record {
	private String brand;
	private String model;
	private String year;
	private String comment;
	private HashMap <String, String[]> products = new HashMap<String, String[]>();
	
	public void print(){
		System.out.println("Rekord: " + brand + "," + model + "," + year + "," + comment);
		Iterator<String> it = products.keySet().iterator();
		while (it.hasNext())
		{
			String key = it.next();
			String tmp [] = products.get(key);
			System.out.print(key + ":");
			if (tmp != null)
			for (int i = 0; i < tmp.length; ++i)
				System.out.print(tmp[i] + ", ");
			System.out.println();
		}
	}
	
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getBrand() {
		return brand;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getModel() {
		return model;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getYear() {
		return year;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getComment() {
		return comment;
	}

	
	public void setProducts(HashMap <String, String[]> products) {
		this.products = products;
	}
	public HashMap <String, String[]> getProducts() {
		return products;
	}

}

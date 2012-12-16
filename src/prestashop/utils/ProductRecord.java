package prestashop.utils;

import java.util.HashMap;
import java.util.Iterator;

import prestashop.interfaces.Record;

public class ProductRecord implements Record {
	private String brand;
	private String model;
	private String engine;
	private String comment;
	private HashMap <String, String[]> products = new HashMap<String, String[]>();

	@Override
	public void print(){
		System.out.println("Rekord: " + brand + "," + model + "," + engine + "," + comment);
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
	public void setEngine(String engine) {
		this.engine = engine;
	}
	public String getEngine() {
		return engine;
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

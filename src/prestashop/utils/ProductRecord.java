package prestashop.utils;

import java.util.HashMap;
import java.util.Iterator;

import prestashop.interfaces.Record;

public class ProductRecord implements Record {
	private String brand;
	private String model;
	private String engine;
	private String comment;
	private String width = null;
	private String height = null;
	private String depth = null;
	private String weight = null;
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

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getDepth() {
		return depth;
	}

	public void setDepth(String depth) {
		this.depth = depth;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}
}

package prestashop.utils;

import prestashop.interfaces.Record;

public class PricelistRecord implements Record {
	private String name;
	private String price;
	private String quantity;
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getPrice() {
		return price;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getQuantity() {
		return quantity;
	}
	@Override
	public void print() {
		System.out.println(this.toString());
	}
	
	@Override
	public String toString() {
		return "PricelistRecord("+name+","+price+","+quantity+")";
	}
}

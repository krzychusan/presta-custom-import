package prestashop.utils;

import prestashop.interfaces.Record;

public class DescriptionRecord implements Record {

	private String name;
	private String width;
	private String height;
	private String depth;
	private String weight;
	private String description;
	private String shortDescription;
	
	 
	@Override
	public void print() {
		System.out.println(this.toString());
	}
	
	@Override
	public String toString() {
		return "Record(\""+name+"\","+width+","+height+","+depth+","+weight+",\""+description+"\")";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

}

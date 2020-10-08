package model;

public class Book implements Product{
	private int weight;
	private final String TYPE = "book";
	
	public Book(int weight) {
		this.weight = weight;
	}
	
	public int getWeight() {
		return this.weight;
	}
	
	public String getStringRepresentation() {
		String representation = new String();
		representation += ("Type: " + this.TYPE + "\n" + "Weight: " + Integer.toString(this.weight));
		return representation;
	}
}

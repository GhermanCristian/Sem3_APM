package model;

public class Cake implements Product{
	private int weight;
	private final String TYPE = "cake";
	
	public Cake(int weight) {
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

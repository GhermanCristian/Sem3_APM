package model;

public class PearTree implements Tree {
	private int age;
	private final String TYPE = "pear";
	
	@Override
	public int getAge() {
		return this.age;
	}
	
	@Override
	public String getType() {
		return this.TYPE;
	}
	
	@Override
	public String getStringRepresentation() {
		String representation = new String();
		representation += "Age: ";
		representation += Integer.toString(this.age);
		representation += "\nType: Pear tree\n";
		return representation;
	}
	
	public PearTree(int age) {
		this.age = age;
	}
}

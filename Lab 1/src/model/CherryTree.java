package model;

public class CherryTree implements Tree {
	private int age;
	private final String TYPE = "cherry";
	
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
		representation += "\nType: Cherry tree\n";
		return representation;
	}
	
	public CherryTree(int age) {
		this.age = age;
	}
}

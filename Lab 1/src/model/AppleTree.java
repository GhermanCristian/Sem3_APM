package model;

public class AppleTree implements Tree {
	private int age;
	private final String TYPE = "apple";
	
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
		representation += "\nType: Apple tree\n";
		return representation;
	}
	
	public AppleTree(int age) {
		this.age = age;
	}
}

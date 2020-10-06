package model;

public class PearTree implements TreeInterface {
	private int age;
	
	@Override
	public boolean isOlderThan3Years() {
		return this.age >= TreeInterface.TREE_AGE;
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

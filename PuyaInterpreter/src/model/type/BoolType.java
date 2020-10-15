package model.type;

public class BoolType implements TypeInterface{
	@Override
	public boolean equals(Object another) {
		return (another instanceof BoolType);
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += "bool";
		return representation;
	}
}

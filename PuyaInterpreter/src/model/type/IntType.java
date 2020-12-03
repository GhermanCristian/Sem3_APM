package model.type;

import model.value.IntValue;
import model.value.ValueInterface;

public class IntType implements TypeInterface{
	@Override
	public boolean equals(Object another) {
		return (another instanceof IntType);
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += "int";
		return representation;
	}

	@Override
	public ValueInterface getDefaultValue() {
		return new IntValue(); // the default constructor sets the value to the default one
	}
}

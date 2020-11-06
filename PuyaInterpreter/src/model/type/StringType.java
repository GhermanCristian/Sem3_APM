package model.type;

import model.value.StringValue;
import model.value.ValueInterface;

public class StringType implements TypeInterface{
	@Override
	public boolean equals(Object another) {
		return (another instanceof StringType);
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += "String";
		return representation;
	}
	
	@Override
	public ValueInterface getDefaultValue() {
		return new StringValue(); // the default constructor sets the value to the default one
	}
}

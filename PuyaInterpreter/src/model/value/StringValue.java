package model.value;

import model.type.StringType;
import model.type.TypeInterface;

public class StringValue implements ValueInterface{
	private final String value;
	public static final String DEFAULT_STRING_VALUE = "";
	
	public StringValue() {
		this.value = StringValue.DEFAULT_STRING_VALUE;
	}
	
	public StringValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public String toString() {
		String representation = "";
		representation += this.value;
		return representation;
	}
	
	@Override
	public TypeInterface getType() {
		return new StringType();
	}

}

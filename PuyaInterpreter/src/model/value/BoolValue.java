package model.value;

import model.type.BoolType;
import model.type.TypeInterface;

public class BoolValue implements ValueInterface {
	private final boolean value;
	
	public BoolValue(boolean value) {
		this.value = value;
	}
	
	public boolean getValue() {
		return this.value;
	}
	
	public String toString() {
		String representation = "";
		representation += String.valueOf(this.value);
		return representation;
	}
	
	@Override
	public TypeInterface getType() {
		return new BoolType();
	}
}

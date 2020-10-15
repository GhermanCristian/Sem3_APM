package model.value;

import model.type.IntType;
import model.type.TypeInterface;

public class IntValue implements ValueInterface{
	private final int value;
	
	public IntValue(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public String toString() {
		String representation = "";
		representation += String.valueOf(this.value);
		return representation;
	}
	
	public TypeInterface getType() {
		return new IntType();
	}
}

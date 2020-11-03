package model.value;

import model.type.IntType;
import model.type.TypeInterface;

public class IntValue implements ValueInterface{
	private final int value;
	public static final int DEFAULT_INT_VALUE = 0;
	
	public IntValue() {
		this.value = IntValue.DEFAULT_INT_VALUE;
	}
	
	public IntValue(int value) {
		this.value = value;
	}
	
	@Override
	public boolean equals(Object another) {
		return (another instanceof IntValue && ((IntValue)another).getValue() == this.value);
	}
	
	public int getValue() {
		return this.value;
	}
	
	public String toString() {
		String representation = "";
		representation += String.valueOf(this.value);
		return representation;
	}
	
	@Override
	public TypeInterface getType() {
		return new IntType();
	}
}

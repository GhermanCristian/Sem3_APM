package model.value;

import model.type.BoolType;
import model.type.TypeInterface;

public class BoolValue implements ValueInterface {
	private final boolean value;
	public static final boolean DEFAULT_BOOL_VALUE = false;
	
	public BoolValue() {
		this.value = BoolValue.DEFAULT_BOOL_VALUE;
	}
	
	public BoolValue(boolean value) {
		this.value = value;
	}
	
	@Override
	public boolean equals(Object another) {
		return (another instanceof BoolValue && ((BoolValue)another).getValue() == this.value);
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

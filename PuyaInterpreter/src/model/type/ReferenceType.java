package model.type;

import model.value.ReferenceValue;
import model.value.ValueInterface;

public class ReferenceType implements TypeInterface{
	private final TypeInterface innerType;
	
	public ReferenceType(TypeInterface innerType) {
		this.innerType = innerType;
	}
	
	@Override
	public boolean equals(Object another) {
		if (another instanceof ReferenceType) {
			return this.innerType.equals(((ReferenceType)another).getInnerType());
		}
		return false;
	}

	@Override
	public ValueInterface getDefaultValue() {
		// when the heap address is not provided, the ReferenceValue is initialised with the default one (0)
		return new ReferenceValue(this.innerType);
	}
	
	public TypeInterface getInnerType() {
		return this.innerType;
	}
	
	public String toString() {
		String representation = "";
		representation += ("Ref(" + this.innerType.toString() + ")");
		return representation;
	}
}

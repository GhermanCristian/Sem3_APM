package model.value;

import model.type.TypeInterface;

public class ReferenceValue implements ValueInterface{
	private final int heapAddress;
	private final TypeInterface referencedType;
	
	public ReferenceValue(int heapAddress, TypeInterface referencedType) {
		this.heapAddress = heapAddress;
		this.referencedType = referencedType;
	}
	
	@Override
	public boolean equals(Object another) {
		return (another instanceof ReferenceValue && ((ReferenceValue)another).getHeapAddress() == this.heapAddress);
	}
	
	public int getHeapAddress() {
		return this.heapAddress;
	}
	
	public TypeInterface getReferencedType() {
		return this.referencedType;
	}
	
	@Override
	public TypeInterface getType() {
		return new ReferenceType(this.referencedType);
	}
	
	public String toString() {
		String representation = "";
		representation += Integer.toHexString(this.heapAddress);
		return representation;
	}

}

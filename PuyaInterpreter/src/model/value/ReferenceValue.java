package model.value;

import model.type.ReferenceType;
import model.type.TypeInterface;

public class ReferenceValue implements ValueInterface{
	private final int heapAddress;
	public static final int DEFAULT_HEAP_ADDRESS = 0;
	private final TypeInterface referencedType;
	
	public ReferenceValue(int heapAddress, TypeInterface referencedType) {
		this.heapAddress = heapAddress;
		this.referencedType = referencedType;
	}
	
	public ReferenceValue(TypeInterface referencedType) {
		this.heapAddress = ReferenceValue.DEFAULT_HEAP_ADDRESS;
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
		representation += ("(0x" + Integer.toHexString(this.heapAddress) + ", " + this.referencedType.toString() + ")");
		return representation;
	}
}
